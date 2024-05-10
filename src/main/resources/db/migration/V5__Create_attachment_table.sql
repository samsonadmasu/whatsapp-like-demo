CREATE TABLE attachments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             message_id BIGINT NOT NULL,
                             file_name VARCHAR(255) NOT NULL,
                             file_type VARCHAR(50) NOT NULL,
                             file_path VARCHAR(255) NOT NULL,
                             FOREIGN KEY (message_id) REFERENCES messages(id)
);
