package com.remototech.remototechapi.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class AppUser {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	@NotBlank(message = "Nome é obrigatório.")
	private String name;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "login_uuid", referencedColumnName = "uuid", updatable = false, nullable = false)
	private Login login;

	@OneToOne
	@JoinColumn(name = "tenant_uuid", referencedColumnName = "uuid", updatable = false, nullable = true)
	private Tenant tenant;

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
