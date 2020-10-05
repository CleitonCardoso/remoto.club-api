package com.remototech.remototechapi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class Job {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	@NotBlank(message = "Título é obrigatório.")
	private String title;

	private String company;

	@NotBlank(message = "Tipo de contrato é obrigatório.")
	private String contractType;

	@NotBlank(message = "Experiência desejada é obrigatório.")
	private String experienceRequired;

	@Column(length = 2048)
	@NotBlank(message = "Descrição é obrigatório.")
	private String description;

	@NotNull(message = "Salário é obrigatório.")
	private BigDecimal salary;

	private CompensationType compensationType;

	@OneToOne
	@JoinColumn(name = "tenant_uuid", referencedColumnName = "uuid", updatable = false)
	@JsonIgnore
	private Tenant tenant;

	@ManyToMany(mappedBy = "jobs")
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	private Set<Candidate> candidates;

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
