ALTER TABLE reviews
    ADD COLUMN cleanliness_rating INTEGER NOT NULL DEFAULT 5,
    ADD COLUMN location_rating INTEGER NOT NULL DEFAULT 5,
    ADD COLUMN checkin_experience_rating INTEGER NOT NULL DEFAULT 5,
    ADD COLUMN value_for_money_rating INTEGER NOT NULL DEFAULT 5;

-- Optionally, migrate existing 'rating' column to all new columns for legacy data (if needed)
-- UPDATE reviews SET cleanliness_rating = rating, location_rating = rating, checkin_experience_rating = rating, value_for_money_rating = rating; 