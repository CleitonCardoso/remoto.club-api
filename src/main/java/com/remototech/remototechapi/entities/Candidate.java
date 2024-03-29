package com.remototech.remototechapi.entities;

import java.time.LocalDateTime;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
@NamedEntityGraph(name = "Candidate.login", attributeNodes = @NamedAttributeNode("login"))
public class Candidate {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	private String name;
	private String linkedInUrl;

	@OneToOne
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Login login;

	@ManyToMany
	@JoinTable(name = "jobs_candidates", joinColumns = @JoinColumn(name = "candidate_uuid"), inverseJoinColumns = @JoinColumn(name = "job_uuid"))
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	@Builder.Default
	private Set<Job> jobs = new LinkedHashSet<Job>();

	@Column(updatable = false)
	private LocalDateTime createdDate;

	private LocalDateTime lastUpdate;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		lastUpdate = LocalDateTime.now();
	}

}
