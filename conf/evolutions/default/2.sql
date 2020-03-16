# Post schema

# --- !Ups

CREATE TABLE posts (
    id varchar(36) NOT NULL,
    user_id varchar(36) NOT NULL,
    text varchar(255) NOT NULL,
    parent_post_id varchar(36),
    comment_count integer DEFAULT 0,
    posted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (parent_post_id) REFERENCES posts(id)
);

# --- !Downs

DROP TABLE IF EXISTS posts;
