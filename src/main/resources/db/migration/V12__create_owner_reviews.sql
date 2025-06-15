CREATE TABLE owner_reviews (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    rating INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_owner_review UNIQUE (reservation_id, owner_id),
    CONSTRAINT fk_owner_review_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    CONSTRAINT fk_owner_review_owner FOREIGN KEY (owner_id) REFERENCES users(user_id),
    CONSTRAINT fk_owner_review_customer FOREIGN KEY (customer_id) REFERENCES users(user_id)
); 