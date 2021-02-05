package com.remototech.remototechapi.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.remototech.remototechapi.repositories.CandidatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.CandidatureStatus;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.JobStatus;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.JobsRepository;
import com.remototech.remototechapi.vos.JobsFilter;

@Service
public class JobsService {

	@Autowired
	private JobsRepository repository;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CandidatureService candidatureService;

	@Autowired
	private CandidatureRepository candidatureRepository;

	public Page<Job> findAllByFilter(JobsFilter filter, Integer pageIndex, Integer resultSize) {
		return findAllByFilterAndTenant( filter, pageIndex, resultSize, null );
	}

	public Page<Job> findAllByFilterAndTenant(JobsFilter filter, int pageIndex, Integer resultSize, Tenant tenant) {
		List<String> contractTypes = filter.getContractTypes();
		List<String> keyWords = filter.getKeyWords();
		List<String> experienceTypes = filter.getExperienceTypes();

		Specification<Job> contractTypesSpec = new Specification<Job>() {
			private static final long serialVersionUID = -6943225890349201334L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "contractType" ).in( contractTypes );
			}

		};

		Specification<Job> keyWordsSpec = new Specification<Job>() {
			private static final long serialVersionUID = 9164462953718514652L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate predicate = null;
				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "title" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "description" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "company" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}
				return predicate;
			}

		};

		Specification<Job> experienceTypesSpec = new Specification<Job>() {
			private static final long serialVersionUID = 9164462953718514652L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "experienceRequired" ).in( experienceTypes );
			}

		};

		Specification<Job> query = null;

		if (contractTypes != null && !contractTypes.isEmpty()) {
			query = contractTypesSpec;
		}

		if (keyWords != null && !keyWords.isEmpty()) {
			query = query == null ? keyWordsSpec : query.and( keyWordsSpec );
		}

		if (experienceTypes != null && !experienceTypes.isEmpty()) {
			query = query == null ? experienceTypesSpec : query.and( experienceTypesSpec );
		}

		if (tenant != null) {
			Specification<Job> tenantSpec = new Specification<Job>() {
				private static final long serialVersionUID = -6945498614404875034L;

				@Override
				public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					return root.get( "tenant" ).in( tenant );
				}

			};

			query = query == null ? tenantSpec : query.and( tenantSpec );
		} else {
			Specification<Job> statusSpec = new Specification<Job>() {
				private static final long serialVersionUID = -6945498614404875034L;

				@Override
				public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					return root.get( "jobStatus" ).in( JobStatus.OPEN );
				}

			};
			query = query == null ? statusSpec : query.and( statusSpec );
		}

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by( "jobStatus" ).descending().and( Sort.by( "createdDate" ).descending() ) ) );
	}

	public Job create(Job job, Tenant tenant) {
		job.setTenant( tenant );
		job.setCompany( tenant.getCompanyName() );
		job.setJobStatus( JobStatus.OPEN );
		return repository.save( job );
	}

	public Job findByIdAndTenant(UUID uuid, Tenant tenant) {
		return repository.findByUuidAndTenantOrderByJobStatusDesc( uuid, tenant );
	}

	@Transactional
	public void removeByUuidAndTenant(UUID uuid, Tenant tenant) {
		repository.removeByUuidAndTenant( uuid, tenant );
	}

	public void apply(UUID jobUuid, Login login) throws GlobalException {
		Candidate candidate = candidateService.getOrCreateIfNotExists( login );

		Optional<Job> jobOptional = repository.findById( jobUuid );

		if (jobOptional.isPresent()) {
			boolean alreadyApplied = repository.existsByUuidAndCandidatesIn( jobUuid, Arrays.asList( candidate ) );

			if (alreadyApplied) {
				throw new GlobalException( "Você já se aplicou para esta vaga" );
			}

			Job job = jobOptional.get();

			Candidature candidature = Candidature.builder()
					.candidate( candidate )
					.job( job )
					.status( CandidatureStatus.APPLIED )
					.build();

			candidatureService.save( candidature );
		}
	}

	public Set<Candidate> getCandidatesFrom(UUID jobUuid, Tenant tenant) {
		Job job = repository.findByUuidAndTenantOrderByJobStatusDesc( jobUuid, tenant );
		if (job != null) {
			return job.getCandidates();
		}
		return null;
	}

	public Page<Job> findAllByFilterAndCandidate(JobsFilter filter, int pageIndex, Integer resultSize, Login loggedUser) {
		List<String> contractTypes = filter.getContractTypes();
		List<String> keyWords = filter.getKeyWords();
		List<String> experienceTypes = filter.getExperienceTypes();

		Specification<Job> contractTypesSpec = new Specification<Job>() {
			private static final long serialVersionUID = -6943225890349201334L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "contractType" ).in( contractTypes );
			}

		};

		Specification<Job> keyWordsSpec = new Specification<Job>() {
			private static final long serialVersionUID = 9164462953718514652L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate predicate = null;
				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "title" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "description" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "company" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}
				return predicate;
			}

		};

		Specification<Job> experienceTypesSpec = new Specification<Job>() {
			private static final long serialVersionUID = 9164462953718514652L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "experienceRequired" ).in( experienceTypes );
			}

		};

		Specification<Job> query = null;

		if (contractTypes != null && !contractTypes.isEmpty()) {
			query = contractTypesSpec;
		}

		if (keyWords != null && !keyWords.isEmpty()) {
			query = query == null ? keyWordsSpec : query.and( keyWordsSpec );
		}

		if (experienceTypes != null && !experienceTypes.isEmpty()) {
			query = query == null ? experienceTypesSpec : query.and( experienceTypesSpec );
		}

		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );

		Specification<Job> candidatesSpec = new Specification<Job>() {
			private static final long serialVersionUID = -6945498614404875034L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "uuid" ).in( candidate.getJobs()
						.stream()
						.map( candidate -> candidate.getUuid() )
						.collect( Collectors.toList() ) );
			}

		};

		query = query == null ? candidatesSpec : query.and( candidatesSpec );

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by( "createdDate" ).descending() ) );
	}

	public void close(UUID uuid, Tenant tenant) throws GlobalException {
		Job job = repository.findByUuidAndTenantOrderByJobStatusDesc( uuid, tenant );
		if (job != null) {
			job.setJobStatus( JobStatus.CLOSED );
			repository.save( job );
		} else {
			throw new GlobalException( "Vaga não encontrada!" );
		}
	}

	public void reopen(UUID uuid, Tenant tenant) throws GlobalException {
		Job job = repository.findByUuidAndTenantOrderByJobStatusDesc( uuid, tenant );
		if (job != null) {
			job.setJobStatus( JobStatus.OPEN );
			repository.save( job );
		} else {
			throw new GlobalException( "Vaga não encontrada!" );
		}
	}

	public Page<Candidature> findAllByFilterAndCandidature(JobsFilter filter, int pageIndex, Integer resultSize, Login loggedUser) {
		List<String> contractTypes = filter.getContractTypes();
		List<String> keyWords = filter.getKeyWords();
		List<String> experienceTypes = filter.getExperienceTypes();

		Specification<Candidature> contractTypesSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = 3803200087874276381L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("job").get( "contractType" ).in( contractTypes );
			}

		};

		Specification<Candidature> keyWordsSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = 9164462953718514652L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate predicate = null;
				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "title" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "description" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "company" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}
				return predicate;
			}

		};

		Specification<Candidature> experienceTypesSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = -8070614658947065570L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("job").get( "experienceRequired" ).in( experienceTypes );
			}

		};

		Specification<Candidature> query = null;

		if (contractTypes != null && !contractTypes.isEmpty()) {
			query = contractTypesSpec;
		}

		if (keyWords != null && !keyWords.isEmpty()) {
			query = query == null ? keyWordsSpec : query.and( keyWordsSpec );
		}

		if (experienceTypes != null && !experienceTypes.isEmpty()) {
			query = query == null ? experienceTypesSpec : query.and( experienceTypesSpec );
		}

		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );

		Specification<Candidature> candidatesSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = 388268013111678193L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("job").get( "uuid" ).in( candidate.getJobs()
						.stream()
						.map( candidate -> candidate.getUuid() )
						.collect( Collectors.toList() ) );
			}

		};

		query = query == null ? candidatesSpec : query.and( candidatesSpec );

		return candidatureRepository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by(  "job.createdDate" ).descending() ) );
	}

}
