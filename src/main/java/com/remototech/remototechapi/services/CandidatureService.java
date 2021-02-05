package com.remototech.remototechapi.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.remototech.remototechapi.entities.*;
import com.remototech.remototechapi.repositories.CandidateRepository;
import com.remototech.remototechapi.repositories.JobsRepository;
import com.remototech.remototechapi.repositories.LoginRepository;
import com.remototech.remototechapi.vos.JobsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.repositories.CandidatureRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class CandidatureService {

	@Autowired
	private CandidatureRepository repository;
	@Autowired
	private CandidateService candidateService;
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private JobsRepository jobsRepository;

	public Candidature findByJobUuidAndCandidate(UUID jobUuid, Login loggedUser) {
		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );
		return repository.findByJobUuidAndCandidate( jobUuid, candidate );
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
				return root.get("job").get( "contractType" ).in( contractTypes );
			}

		};

		Specification<Candidature> keyWordsSpec = new Specification<Candidature>() {
			private static final long serialVersionUID = -4185378444194323781L;

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

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize, Sort.by(  "job.createdDate" ).descending() ) );
	}
}
