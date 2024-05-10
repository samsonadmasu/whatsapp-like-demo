CREATE TABLE chat_room_members
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT                      NOT NULL,
    chatroom_id BIGINT                      NOT NULL,
    status      ENUM ('ACTIVE', 'INACTIVE') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (chatroom_id) REFERENCES chatrooms (id)
);