package com.remototech.remototechapi.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.JobStatus;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.JobsRepository;
import com.remototech.remototechapi.vos.JobsFilter;

@Service
public class AdminJobsService {

	@Autowired
	private JobsRepository repository;

	@Autowired
	private CandidateService candidateService;

	public Page<Job> findAllByFilter(JobsFilter filter, int pageIndex, Integer resultSize) {
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

		Specification<Job> tenantSpec = new Specification<Job>() {
			private static final long serialVersionUID = -6945498614404875034L;

			@Override
			public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "tenant" ).isNull();
			}
		};
		query = query == null ? tenantSpec : query.and( tenantSpec );

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by( "jobStatus" ).descending().and( Sort.by( "createdDate" ).descending() ) ) );
	}

	public Job create(Job job) {
		job.setJobStatus( JobStatus.OPEN );
		return repository.save( job );
	}

	public Job findById(UUID uuid) {
		return repository.findById( uuid ).orElse( null );
	}

	@Transactional
	public void removeByUuid(UUID uuid) {
		repository.deleteById( uuid );
	}

	public Set<Candidate> getCandidatesFrom(UUID jobUuid) {
		return candidateService.findAllByJobUuid(jobUuid);
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

	public void close(UUID uuid) throws GlobalException {
		Job job = repository.findByUuidOrderByJobStatusDesc( uuid );
		if (job != null) {
			job.setJobStatus( JobStatus.CLOSED );
			repository.save( job );
		} else {
			throw new GlobalException( "Vaga não encontrada!" );
		}
	}

	public void reopen(UUID uuid) throws GlobalException {
		Job job = repository.findByUuidOrderByJobStatusDesc( uuid );
		if (job != null) {
			job.setJobStatus( JobStatus.OPEN );
			repository.save( job );
		} else {
			throw new GlobalException( "Vaga não encontrada!" );
		}
	}

}
