package vn.attendance.util;

import com.sun.jna.Memory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        byte[] imageBytes = Base64.getDecoder().decode(base64String);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        AvataInfo avataInfo = new AvataInfo();
        avataInfo.setPictureLeng(imageBytes.length);
        avataInfo.setWidth(image.getWidth());
        avataInfo.setHeight(image.getHeight());
        avataInfo.setMemory(new Memory(imageBytes.length));

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
    public static class AvataInfo {
        private int pictureLeng;
        private int width;
        private int height;
        private Memory memory;

        public int getPictureLeng() {
            return pictureLeng;
        }

        public void setPictureLeng(int pictureLeng) {
            this.pictureLeng = pictureLeng;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Memory getMemory() {
            return memory;
        }

        public void setMemory(Memory memory) {
            this.memory = memory;
        }
    }
}

