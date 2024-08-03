package vn.attendance.service.server.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDto {
    Integer id;
    String pic;
    LocalDateTime time;

    public ImageDto(IImageDto iImageDto){
        this.id = iImageDto.getId();
        this.pic = iImageDto.getPic() != null ? new String(iImageDto.getPic()) : null;
        this.time = iImageDto.getTime();
    }
}
