package com.remototech.remototechapi.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRecovery {

	@Id
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID uuid;
	@Version
	private Long version;

	private boolean active;

	@ManyToOne
	private Login login;

	@OneToOne
	private Email email;

	private String recoveryHash;

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
