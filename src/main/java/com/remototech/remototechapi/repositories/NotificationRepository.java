package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remototech.remototechapi.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

	@Query("Select notification from Notification notification where notification.login.uuid = :login_uuid")
	public Page<Notification> findLast10NotificationsFor(@Param("login_uuid") UUID loginUuid, Pageable pageable);

	@Query("UPDATE Notification notification SET notification.read = true WHERE notification.uuid in (:notification_uuids)")
	@Modifying
	public void markAsRead(@Param("notification_uuids") List<UUID> notificationUuids);

}
