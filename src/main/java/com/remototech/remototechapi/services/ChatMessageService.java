package com.remototech.remototechapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Role;
import com.remototech.remototechapi.entities.chat.ChatMessage;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.ChatMessageRepository;

@Service
public class ChatMessageService {

	@Autowired
	private ChatMessageRepository repository;

	@Autowired
	private CandidatureService candidatureService;

	public ChatMessage save(ChatMessage chatMessage, Login loggedUser) throws GlobalException {
		validateSave( chatMessage, loggedUser );
		return repository.save( chatMessage );
	}

	private void validateSave(ChatMessage chatMessage, Login loggedUser) throws GlobalException {
		if (Role.ADMIN.equals( loggedUser.getRole() ))
			return;

		UUID senderUuid = chatMessage.getLoginUuid();
		UUID candidatureUuid = chatMessage.getCandidatureUuid();
		UUID tenantUuid = loggedUser.getTenant() != null ? loggedUser.getTenant().getUuid() : null;

		boolean candidateOrCreator = candidatureService.isCandidateOrCreator( senderUuid, tenantUuid, candidatureUuid );
		if (!candidateOrCreator)
			throw new GlobalException( "Usuário não possui permissão para ver esta candidatura" );
	}

	private void validateRead(UUID candidatureUuid, Login loggedUser) throws GlobalException {
		if (Role.ADMIN.equals( loggedUser.getRole() ))
			return;

		UUID tenantUuid = loggedUser.getTenant() != null ? loggedUser.getTenant().getUuid() : null;

		boolean candidateOrCreator = candidatureService.isCandidateOrCreator( loggedUser.getUuid(), tenantUuid, candidatureUuid );
		if (!candidateOrCreator)
			throw new GlobalException( "Usuário não possui permissão para ver mensagens desta candidatura" );
	}

	public List<ChatMessage> findAllFromCandidature(UUID candidatureUuid, Login loggedUser) throws GlobalException {
		validateRead( candidatureUuid, loggedUser );
		return repository.findAllByCandidatureUuid( candidatureUuid );
	}

}
