package com.remototech.remototechapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Notification;
import com.remototech.remototechapi.repositories.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public List<Notification> getLast10Notifications() {
		return null;
	}

	public Page<Notification> getLast10Notifications(Login loggedUser) {
		return notificationRepository.findLast10NotificationsFor( loggedUser.getUuid(), PageRequest.of( 0, 10, Sort.by( "createdDate" ).descending() ) );
	}

	public Page<Notification> getNotifications(Login loggedUser, Integer pageIndex, Integer resultSize) {
		return notificationRepository.findLast10NotificationsFor( loggedUser.getUuid(), PageRequest.of( pageIndex, resultSize, Sort.by( "createdDate" ).descending() ) );
	}

	public void markAsRead(List<UUID> notificationUuids) {
		notificationRepository.markAsRead( notificationUuids );
	}

}
