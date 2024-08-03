ALTER TABLE notify
    ADD COLUMN title VARCHAR(100);

ALTER TABLE notify
    ADD COLUMN content VARCHAR(200);

ALTER TABLE notify
    ADD COLUMN destination_page VARCHAR(255);

ALTER TABLE notify
    ADD COLUMN page_params TEXT;

-- Xóa cột message cũ nếu cần thiết
ALTER TABLE notify
DROP COLUMN message;

-- Xóa ràng buộc nếu cần thiết
ALTER TABLE notify
DROP CONSTRAINT notify_fb_1;

-- Xóa cột room_id
ALTER TABLE notify
DROP COLUMN room_id;
