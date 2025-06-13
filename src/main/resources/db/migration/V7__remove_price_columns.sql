-- Remove price_per_night from housing
ALTER TABLE housing DROP COLUMN IF EXISTS price_per_night;

-- Remove total_price from reservations
ALTER TABLE reservations DROP COLUMN IF EXISTS total_price; 