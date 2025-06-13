-- Drop the existing sequence and column
ALTER TABLE messages DROP COLUMN message_id;
DROP SEQUENCE IF EXISTS message_sequence;

-- Recreate the column with BIGINT type
ALTER TABLE messages ADD COLUMN message_id BIGSERIAL PRIMARY KEY; 