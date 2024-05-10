CREATE TABLE chatrooms
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    is_group BOOLEAN      NOT NULL,
    status   VARCHAR(20)  NOT NULL -- Status field to represent the chatroom status
);