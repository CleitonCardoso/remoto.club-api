package com.remototech.remototechapi.controllers.priv;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Notification;
import com.remototech.remototechapi.services.NotificationService;

@RestController
@RequestMapping("private/notification")
public class NotificationController extends LoggedInController {

	@Autowired
	private NotificationService notificationService;

	@GetMapping
	public Page<Notification> getNotifications(final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize) {
		return notificationService.getNotifications( getLoggedUser(), pageIndex, resultSize );
	}
	
	@PostMapping("mark-as-read")
	public void markAsRead(@RequestParam(value="notification-uuid") List<UUID> notificationUuids) {
		notificationService.markAsRead(notificationUuids);
	}

}
