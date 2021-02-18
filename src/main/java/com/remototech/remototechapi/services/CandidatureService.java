package com.remototech.remototechapi.services;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import com.remototech.remototechapi.dtos.CandidateDTO;
import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.CandidatureRepository;
import com.remototech.remototechapi.vos.JobsFilter;

@Service
public class CandidatureService {

	@Autowired
	private CandidatureRepository repository;
	@Autowired
	private CandidateService candidateService;

	public Candidature findByJobUuidAndCandidate(UUID jobUuid, UUID candidateUuid) {
		return repository.findByJobUuidAndCandidateUuid( jobUuid, candidateUuid );
	}

	public Candidature save(Candidature candidature) {
		return repository.save( candidature );
	}

	public Page<Candidature> findAllByFilterAndCandidature(JobsFilter filter, int pageIndex, Integer resultSize, Login loggedUser) {
		List<String> contractTypes = filter.getContractTypes();
		List<String> keyWords = filter.getKeyWords();
		List<String> experienceTypes = filter.getExperienceTypes();

		Specification<Candidature> contractTypesSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = 3803200087874276381L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "job" ).get( "contractType" ).in( contractTypes );
			}

		};

		Specification<Candidature> keyWordsSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = -4185378444194323781L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate predicate = null;
				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "job" ).get( "title" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "job" ).get( "description" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}

				for (String keyWord : keyWords) {
					Predicate insidePredicate = criteriaBuilder.like( criteriaBuilder.upper( root.get( "job" ).get( "company" ) ), "%" + keyWord.toUpperCase() + "%" );
					predicate = predicate == null ? insidePredicate : criteriaBuilder.or( predicate, insidePredicate );
				}
				return predicate;
			}

		};

		Specification<Candidature> experienceTypesSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = -8070614658947065570L;

			@Override
			public Predicate toPredicate(Root<Candidature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get( "job" ).get( "experienceRequired" ).in( experienceTypes );
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
				return root.get( "candidate" ).get( "uuid" ).in( candidate.getUuid() );
			}

		};

		query = query == null ? candidatesSpec : query.and( candidatesSpec );

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by( "job.createdDate" ).descending() ) );
	}

	public Candidature findByJobUuidTenantAndCandidate(UUID jobUuid, UUID tenantUuid, UUID candidateUuid) {
		return repository.findByJobUuidTenantAndCandidate( jobUuid, tenantUuid, candidateUuid );
	}

	public boolean isCandidateOrCreator(UUID loginUuid, UUID tenantUuid, UUID candidatureUuid) {
		if (tenantUuid == null)
			return repository.isCandidate( loginUuid, candidatureUuid );
		else
			return repository.isCreator( tenantUuid, candidatureUuid );
	}

	public Set<CandidateDTO> findByJobUuidAndTenantUuid(UUID jobUuid, UUID tenantUuid) throws GlobalException {
		Set<Candidature> candidatures = repository.findByJobUuidAndJobTenantUuid( jobUuid, tenantUuid );
		Set<CandidateDTO> candidates = new LinkedHashSet<>();
		candidatures.forEach( candidature -> candidates.add( CandidateDTO.builder()
				.candidatureStatus( candidature.getStatus().getDescription() )
				.name( candidature.getCandidate().getName() )
				.dateApplied( candidature.getCreatedDate() )
				.linkedInUrl( candidature.getCandidate().getLinkedInUrl() )
				.uuid( candidature.getCandidate().getUuid() )
				.build() ) );
		return candidates;
	}
}
