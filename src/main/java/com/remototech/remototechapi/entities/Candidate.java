package com.remototech.remototechapi.entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	private String nome;
	private String linkedInUrl;

	@OneToOne
	@JsonIgnore
	private Login login;

	@ManyToMany
	@JoinTable(name = "jobs_candidates", joinColumns = @JoinColumn(name = "candidate_uuid"), inverseJoinColumns = @JoinColumn(name = "job_uuid"))
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@Builder.Default
	private Set<Job> jobs = new LinkedHashSet<Job>();

}
