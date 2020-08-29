package com.remototech.remototechapi.entities;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Job {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	private String title;

	private String company;

	private String contractType;

	private String experienceRequired;

	@Column(length = 1024)
	private String description;

	private BigDecimal salary;

	@OneToOne
	@JoinColumn(name = "tenant_uuid", referencedColumnName = "uuid", updatable = false)
	private Tenant tenant;

	@ManyToMany(mappedBy = "jobs")
	private Set<Candidate> candidates;

}
