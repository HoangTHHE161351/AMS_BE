package vn.attendance.util;

import com.sun.jna.Memory;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class AvataUtils {

    /**
     * Phương thức để giải mã ảnh từ chuỗi base64 và trích xuất các thông số ảnh
     *
     * @param base64String chuỗi base64 của ảnh
     * @return đối tượng AvataInfo chứa các thông số ảnh
     * @throws IOException nếu có lỗi khi giải mã hoặc đọc ảnh
     */
    public static AvataInfo decodeBase64AndExtractInfo(String base64String) throws IOException {
        // Tách phần base64 data
        String base64Image = base64String.split(";base64,")[1];

        // Giải mã base64 thành mảng byte
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Đọc hình ảnh từ mảng byte
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);

        // Lấy chiều rộng và chiều dài của hình ảnh
        int width = image.getWidth();
        int height = image.getHeight();

        AvataInfo avataInfo = new AvataInfo();
        avataInfo.setPictureLeng(imageBytes.length);
        avataInfo.setWidth(width);
        avataInfo.setHeight(height);
        var  memory = new Memory(imageBytes.length);
        memory.write(0, imageBytes, 0, imageBytes.length);
        avataInfo.setMemory(memory);

        return avataInfo;
    }

    public static String convertImageToBase64String(byte[] image) {
        // Mã hóa byte thành chuỗi Base64
        String encodedString = Base64.getEncoder().encodeToString(image);
        return encodedString;

    }

    /**
     * Định nghĩa lớp AvataInfo để lưu trữ thông tin ảnh
     */
    @Getter
    @Setter
    public static class AvataInfo {
        private int pictureLeng;
        private int width;
        private int height;
        private Memory memory;
    }
}

