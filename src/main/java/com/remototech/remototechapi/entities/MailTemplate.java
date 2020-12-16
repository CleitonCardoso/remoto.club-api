package com.remototech.remototechapi.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class MailTemplate {

	@Id
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID uuid;
	@Version
	private Long version;
	
	private String title;
	
	@Column(length = 9254)
	private String design;

}