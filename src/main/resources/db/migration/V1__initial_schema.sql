-- V1__initial_schema.sql : Création de la structure initiale de la base de données DomuSwap

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    role VARCHAR(20),
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    rating FLOAT,
    profile_picture VARCHAR(255)
);

CREATE TABLE housing (
    housing_id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    photo_1 VARCHAR(255),
    photo_2 VARCHAR(255),
    photo_3 VARCHAR(255),
    constraint_text TEXT,
    amenities TEXT,
    max_guests INTEGER NOT NULL,
    user_id INTEGER REFERENCES users(user_id)
);

CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    housing_id INTEGER NOT NULL REFERENCES housing(housing_id),
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    guests INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    sender_id INTEGER REFERENCES users(user_id),
    receiver_id INTEGER REFERENCES users(user_id),
    content TEXT NOT NULL,
    created_at TIMESTAMP,
    status VARCHAR(20)
);

CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    cleanliness_rating INTEGER NOT NULL,
    location_rating INTEGER NOT NULL,
    checkin_experience_rating INTEGER NOT NULL,
    value_for_money_rating INTEGER NOT NULL,
    comment VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    housing_id INTEGER NOT NULL REFERENCES housing(housing_id),
    user_id INTEGER NOT NULL REFERENCES users(user_id)
);

CREATE TABLE owner_reviews (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id),
    owner_id INTEGER NOT NULL REFERENCES users(user_id),
    customer_id INTEGER NOT NULL REFERENCES users(user_id),
    rating INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE saved_listings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    housing_id INTEGER REFERENCES housing(housing_id)
);

CREATE TABLE requested_service (
    id SERIAL PRIMARY KEY,
    housing_id INTEGER REFERENCES housing(housing_id),
    title VARCHAR(255)
);

CREATE TABLE password_reset_tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL
); 