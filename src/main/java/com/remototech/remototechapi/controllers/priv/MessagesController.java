package com.remototech.remototechapi.controllers.priv;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.chat.ChatMessage;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.ChatMessageService;

@RestController
@RequestMapping("private/messages")
public class MessagesController extends LoggedInController {

	@Autowired
	private ChatMessageService chatMessageService;

	@GetMapping("/candidature/{candidature_uuid}")
	public List<ChatMessage> getMessagesFromCandidature(@PathVariable("candidature_uuid") UUID candidatureUuid) throws GlobalException {
		return chatMessageService.findAllFromCandidature( candidatureUuid, getLoggedUser() );
	}
}
