package vn.attendance.service.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO {

   Integer id;

   String userName;

   String name;

   String className;

   String slot;

   String subjectCode;

   String description;

   String status;

   LocalDateTime createdAt;

   String createdBy;

   LocalDateTime modifiedAt;

   String modifiedBy;

   String avatar;

   // Constructor from IAttendanceDTO interface
   public AttendanceDTO(IAttendanceDTO attendanceDTO) {
      this.id = attendanceDTO.getId();
      this.userName = attendanceDTO.getUserName();
      this.name = attendanceDTO.getName();
      this.className = attendanceDTO.getClassName();
      this.slot = attendanceDTO.getSlot();
      this.subjectCode = attendanceDTO.getSubjectCode();
      this.description = attendanceDTO.getDescription();
      this.status = attendanceDTO.getStatus();
      this.createdAt = attendanceDTO.getCreatedAt();
      this.createdBy = attendanceDTO.getCreatedBy();
      this.modifiedAt = attendanceDTO.getModifiedAt();
      this.modifiedBy = attendanceDTO.getModifiedBy();
      this.avatar = attendanceDTO.getAvatar() != null ? new String(attendanceDTO.getAvatar()) : null;;
   }
}
