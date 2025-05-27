<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Reservations</title>
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
        .reservation-card {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .reservation-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }
        .reservation-title {
            font-size: 1.2em;
            font-weight: bold;
            color: #2A2729;
        }
        .reservation-status {
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
        
        .reservation-details {
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
        .btn:hover {
            opacity: 0.9;
        }
        .btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        /* Review Modal Styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
        }
        .modal-content {
            background: white;
            width: 90%;
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 8px;
            position: relative;
        }
        .close {
            position: absolute;
            right: 20px;
            top: 20px;
            font-size: 24px;
            cursor: pointer;
        }
        .review-form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .review-form label {
            font-weight: bold;
        }
        .review-form select,
        .review-form textarea {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .review-form textarea {
            min-height: 100px;
            resize: vertical;
        }
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="/reservations?userId=${param.userId}">My Reservations</a>
        <a href="/customer.html">Back to Account</a>
    </div>

    <h1>📋 My Reservations</h1>
    
    <c:forEach var="reservation" items="${reservations}">
        <div class="reservation-card">
            <div class="reservation-header">
                <div class="reservation-title">${reservation.housing.title}</div>
                <div class="reservation-status status-${reservation.status.name().toLowerCase()}">
                    ${reservation.status}
                </div>
            </div>
            
            <div class="reservation-details">
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
            </div>
            
            <div class="action-buttons">
                <c:if test="${reservation.status == 'PENDING' || reservation.status == 'ACCEPTED'}">
                    <button class="btn btn-cancel" onclick="cancelReservation('${reservation.id}')">
                        Cancel Booking
                    </button>
                </c:if>
                <c:if test="${reservation.status == 'COMPLETED'}">
                    <button class="btn btn-review" onclick="showReviewModal('${reservation.housing.id}')">
                        Leave a Review
                    </button>
                </c:if>
            </div>
        </div>
    </c:forEach>

    <!-- Review Modal -->
    <div id="reviewModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeReviewModal()">&times;</span>
            <h2>Write a Review</h2>
            <form id="reviewForm" class="review-form">
                <input type="hidden" id="housingId" name="housingId">
                <div>
                    <label for="rating">Rating:</label>
                    <select id="rating" name="rating" required>
                        <option value="5">5 ⭐️ - Excellent</option>
                        <option value="4">4 ⭐️ - Very Good</option>
                        <option value="3">3 ⭐️ - Good</option>
                        <option value="2">2 ⭐️ - Fair</option>
                        <option value="1">1 ⭐️ - Poor</option>
                    </select>
                </div>
                <div>
                    <label for="comment">Your Review:</label>
                    <textarea id="comment" name="comment" required placeholder="Share your experience..."></textarea>
                </div>
                <button type="submit" class="btn btn-review">Submit Review</button>
            </form>
        </div>
    </div>

    <script>
        function cancelReservation(reservationId) {
            if (confirm('Are you sure you want to cancel this booking?')) {
                fetch(`/api/reservations/${reservationId}/cancel`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
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
            document.getElementById('housingId').value = housingId;
            document.getElementById('reviewModal').style.display = 'block';
        }

        function closeReviewModal() {
            document.getElementById('reviewModal').style.display = 'none';
        }

        document.getElementById('reviewForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = new FormData(this);
            
            fetch('/api/reviews/add', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    alert('Review submitted successfully!');
                    closeReviewModal();
                    window.location.reload();
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(error => {
                alert('Error submitting review: ' + error.message);
            });
        });

        // Close modal when clicking outside
        window.onclick = function(event) {
            const modal = document.getElementById('reviewModal');
            if (event.target == modal) {
                closeReviewModal();
            }
        }
    </script>
</body>
</html>
