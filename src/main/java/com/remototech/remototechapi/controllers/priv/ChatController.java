package com.remototech.remototechapi.controllers.priv;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.remototech.remototechapi.controllers.LoggedInController;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.chat.ChatMessage;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.LoginService;

@Controller
public class ChatController extends LoggedInController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/chat")
	public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) throws GlobalException {
		String username = headerAccessor.getSessionAttributes().get( "username" ).toString();
		String candidature = headerAccessor.getSessionAttributes().get( "candidature" ).toString();
		Login login = this.loginService.findByUsername( username );

		chatMessage.setCandidatureUuid( UUID.fromString( candidature ) );
		chatMessage.setSender( login.getUser().getName() );
		chatMessage.setLoginUuid( login.getUuid() );

		simpMessagingTemplate.convertAndSend( "/queue/messages/" + candidature, chatMessage );
	}

}
