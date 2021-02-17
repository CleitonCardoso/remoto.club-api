package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepositoryImplementation<ChatMessage, UUID> {

	List<ChatMessage> findAllByCandidatureUuid(UUID candidatureUuid);

}
