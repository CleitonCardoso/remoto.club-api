package com.remototech.remototechapi.entities.chat;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

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
public class ChatMessage {

	@Id
	@Column(columnDefinition = "uuid", updatable = false)
	@GeneratedValue(generator = "system-uuid", strategy = GenerationType.AUTO)
	private UUID uuid;

	@Column(length = 500)
	private String sender;

	@Column(length = 5000)
	private String content;

	private UUID loginUuid;
	private UUID candidatureUuid;

	@Column(updatable = false)
	private LocalDateTime createdDate;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
	}

}
