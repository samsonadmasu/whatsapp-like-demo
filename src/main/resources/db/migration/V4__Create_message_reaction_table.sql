CREATE TABLE message_reactions (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   message_id BIGINT NOT NULL,
                                   user_id BIGINT NOT NULL,
                                   reaction_type VARCHAR(50) NOT NULL,
                                   FOREIGN KEY (message_id) REFERENCES messages(id),
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);
