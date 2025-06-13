-- Drop the existing messages table
DROP TABLE IF EXISTS messages CASCADE;

-- Recreate the messages table with the correct structure
CREATE TABLE messages (
    message_id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP,
    receiver_id BIGINT REFERENCES users(user_id),
    sender_id BIGINT REFERENCES users(user_id),
    status VARCHAR(20)
); 