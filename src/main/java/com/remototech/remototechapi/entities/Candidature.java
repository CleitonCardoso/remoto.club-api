package com.remototech.remototechapi.entities;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "jobs_candidates")
@AllArgsConstructor
@NoArgsConstructor
public class Candidature {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	@OneToOne
	private Job job;
	@OneToOne
	private Candidate candidate;
	@Enumerated(EnumType.STRING)
	private CandidatureStatus status;

	private ArrayList<String> messages;

}
