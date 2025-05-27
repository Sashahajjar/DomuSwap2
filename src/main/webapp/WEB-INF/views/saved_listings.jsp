<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Saved Listings & Bookings</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #FAF9F7;
        }
        .nav-menu {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .nav-menu a {
            color: #2A2729;
            text-decoration: none;
            margin-right: 20px;
            font-weight: bold;
        }
        .nav-menu a:hover {
            color: #666;
        }
        h1 { color: #2A2729; }
        
        .container {
            display: flex;
            gap: 20px;
        }
        
        .bookings-section {
            flex: 1;
        }
        
        .saved-listings-section {
            flex: 1;
        }
        
        .booking-card, .listing-card {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        
        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }
        
        .card-title {
            font-size: 1.2em;
            font-weight: bold;
            color: #2A2729;
        }
        
        .booking-status {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.9em;
            font-weight: bold;
        }
        
        .status-pending { background: #FFF3CD; color: #856404; }
        .status-accepted { background: #D4EDDA; color: #155724; }
        .status-rejected { background: #F8D7DA; color: #721C24; }
        .status-completed { background: #CCE5FF; color: #004085; }
        .status-cancelled { background: #E2E3E5; color: #383D41; }
        
        .card-details {
            margin-bottom: 15px;
        }
        
        .detail-row {
            display: flex;
            margin-bottom: 8px;
        }
        
        .detail-label {
            width: 120px;
            font-weight: bold;
            color: #666;
        }
        
        .detail-value {
            flex: 1;
        }
        
        .action-buttons {
            display: flex;
            gap: 10px;
        }
        
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.2s;
        }
        
        .btn-cancel {
            background: #DC3545;
            color: white;
        }
        
        .btn-review {
            background: #17A2B8;
            color: white;
        }
        
        .btn-chat {
            background: #007bff;
            color: white;
        }
        
        .btn:hover {
            opacity: 0.9;
        }
        
        .btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }
        
        .listing-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        
        .listing-price {
            font-size: 1.2em;
            font-weight: bold;
            color: #28A745;
            margin-bottom: 10px;
        }
        
        .listing-description {
            color: #666;
            margin-bottom: 15px;
        }
        
        .reservation-status {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.9em;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="/customer">Dashboard</a>
        <a href="/saved_listings?userId=${param.userId}">Saved Listings & Bookings</a>
        <a href="/search">Search Listings</a>
        <a href="/" onclick="logout()">Logout</a>
    </div>

    <h1>📋 My Saved Listings & Bookings</h1>
    
    <div class="container">
        <div class="bookings-section">
            <h2>My Bookings</h2>
            <c:forEach var="reservation" items="${reservations}">
                <div class="booking-card">
                    <div class="card-header">
                        <div class="card-title">${reservation.housing.title}</div>
                        <div class="reservation-status ${reservation.status.name().toLowerCase()}">
                            ${reservation.status}
                        </div>
                    </div>
                    
                    <div class="card-details">
                        <div class="detail-row">
                            <div class="detail-label">Check-in:</div>
                            <div class="detail-value">
                                <fmt:formatDate value="${reservation.checkInAsDate}" pattern="MMMM d, yyyy"/>
                            </div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Check-out:</div>
                            <div class="detail-value">
                                <fmt:formatDate value="${reservation.checkOutAsDate}" pattern="MMMM d, yyyy"/>
                            </div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Guests:</div>
                            <div class="detail-value">${reservation.guestCount}</div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Total Price:</div>
                            <div class="detail-value">$${reservation.totalPrice}</div>
                        </div>
                    </div>
                    
                    <div class="action-buttons">
                        <c:if test="${reservation.status == 'PENDING' || reservation.status == 'ACCEPTED'}">
                            <button class="btn btn-cancel" onclick="cancelReservation(${reservation.id})">
                                Cancel Booking
                            </button>
                        </c:if>
                        <c:if test="${reservation.status == 'COMPLETED'}">
                            <button class="btn btn-review" onclick="showReviewModal(${reservation.housing.id})">
                                Leave a Review
                            </button>
                        </c:if>
                        <button class="btn btn-chat" onclick="startChat(${reservation.housing.owner.id})">
                            Chat with Owner
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <div class="saved-listings-section">
            <h2>Saved Listings</h2>
            <c:forEach var="listing" items="${savedListings}">
                <div class="listing-card">
                    <img src="${listing.imageUrl}" alt="${listing.title}" class="listing-image">
                    <div class="card-title">${listing.title}</div>
                    <div class="listing-price">$${listing.price} per night</div>
                    <div class="listing-description">${listing.description}</div>
                    <div class="action-buttons">
                        <button class="btn btn-chat" onclick="startChat(${listing.owner.id})">
                            Contact Owner
                        </button>
                        <button class="btn" onclick="removeSavedListing(${listing.id})">
                            Remove from Saved
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script>
        function cancelReservation(reservationId) {
            if (confirm('Are you sure you want to cancel this booking?')) {
                fetch(`/api/reservations/${reservationId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        throw new Error('Failed to cancel reservation');
                    }
                })
                .catch(error => {
                    alert('Error: ' + error.message);
                });
            }
        }
        
        function showReviewModal(housingId) {
            // TODO: Implement review modal
            alert('Review functionality coming soon!');
        }
        
        function startChat(ownerId) {
            window.location.href = `/chat?userId=${ownerId}`;
        }
        
        function removeSavedListing(listingId) {
            if (confirm('Remove this listing from your saved items?')) {
                fetch(`/api/saved-listings/${listingId}`, {
                    method: 'DELETE'
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        throw new Error('Failed to remove listing');
                    }
                })
                .catch(error => {
                    alert('Error: ' + error.message);
                });
            }
        }
    </script>
</body>
</html> 