package com.remototech.remototechapi.services;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.repositories.JobsRepository;
import com.remototech.remototechapi.vos.JobsFilter;

@Service
public class JobsService {

	@Autowired
	private JobsRepository repository;

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
		}

		return repository.findAll( query, PageRequest.of( pageIndex, resultSize ) );
	}

	public Job create(Job job, Tenant tenant) {
		job.setTenant( tenant );
		return repository.save( job );
	}

}
