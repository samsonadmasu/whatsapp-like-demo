CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          chatroom_id BIGINT NOT NULL,
                          content TEXT NOT NULL,
                          reply_to_id BIGINT, -- Reference to another message (optional)
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (chatroom_id) REFERENCES chatrooms(id),
                          FOREIGN KEY (reply_to_id) REFERENCES messages(id) -- Add foreign key constraint for reply_to_id
);