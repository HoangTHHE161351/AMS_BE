package vn.attendance.service.notify.entity;

import java.time.LocalDateTime;

public interface NotifyDto {

     Integer getId();

     String getTitle();

     String getContent();

     String getDestinationPage();

     String getPageParams();

     LocalDateTime getTime();

     Integer getIsRead();
}
