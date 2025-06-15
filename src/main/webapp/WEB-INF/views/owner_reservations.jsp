<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Owner Dashboard - Reservations</title>
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
        .btn-accept {
            background: #28A745;
            color: white;
        }
        .btn-reject {
            background: #DC3545;
            color: white;
        }
        .btn-complete {
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
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
        }

        .modal-content {
            position: relative;
            background-color: #fff;
            margin: 15% auto;
            padding: 20px;
            width: 400px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .close {
            position: absolute;
            right: 20px;
            top: 10px;
            font-size: 24px;
            font-weight: bold;
            cursor: pointer;
            color: #666;
        }

        .close:hover {
            color: #000;
        }

        .rating-container {
            text-align: center;
            margin: 20px 0;
        }

        .stars {
            display: inline-block;
            font-size: 30px;
            cursor: pointer;
        }

        .star {
            color: #ddd;
            transition: color 0.2s;
        }

        .star.active {
            color: #FFD700;
        }

        .submit-review {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #17A2B8;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            margin-top: 20px;
        }

        .submit-review:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="/owner.html">Dashboard</a>
        <a href="/owner_reservations.html?ownerId=${param.ownerId}">Reservations</a>
        <a href="/portfolio.html">My Listings</a>
    </div>

    <h1>📋 Owner Dashboard - Reservations</h1>
    
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
                    <div class="detail-label">Guest:</div>
                    <div class="detail-value">${reservation.user.name}</div>
                </div>
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
                <c:if test="${reservation.status.name() == 'PENDING'}">
                    <button class="btn btn-accept" onclick="updateStatus('${reservation.id}', 'accept')">
                        Accept
                    </button>
                    <button class="btn btn-reject" onclick="updateStatus('${reservation.id}', 'reject')">
                        Reject
                    </button>
                </c:if>
                <c:if test="${reservation.status.name() == 'ACCEPTED'}">
                    <button class="btn btn-complete" onclick="updateStatus('${reservation.id}', 'complete')">
                        Mark as Completed
                    </button>
                </c:if>
            </div>
        </div>
    </c:forEach>

    <!-- Review Modal -->
    <div id="reviewModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Review Customer</h2>
            <div class="rating-container">
                <div class="stars">
                    <span class="star" data-rating="1">★</span>
                    <span class="star" data-rating="2">★</span>
                    <span class="star" data-rating="3">★</span>
                    <span class="star" data-rating="4">★</span>
                    <span class="star" data-rating="5">★</span>
                </div>
            </div>
            <button class="submit-review" onclick="submitReview()">Submit Review</button>
        </div>
    </div>

    <script>
        let currentReservationId = null;
        let selectedRating = 0;

        function updateStatus(reservationId, action) {
            if (action === 'complete') {
                currentReservationId = reservationId;
                showReviewModal();
            } else {
                const endpoint = '/api/reservations/' + reservationId + '/' + action;
                
                fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        throw new Error('Failed to update status');
                    }
                })
                .catch(error => {
                    alert('Error: ' + error.message);
                });
            }
        }

        // Modal functionality
        const modal = document.getElementById('reviewModal');
        const closeBtn = document.getElementsByClassName('close')[0];
        const stars = document.getElementsByClassName('star');

        function showReviewModal() {
            modal.style.display = 'block';
            selectedRating = 0;
            updateStars();
        }

        closeBtn.onclick = function() {
            modal.style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        // Star rating functionality
        Array.from(stars).forEach(star => {
            star.addEventListener('click', function() {
                selectedRating = parseInt(this.getAttribute('data-rating'));
                updateStars();
            });

            star.addEventListener('mouseover', function() {
                const rating = parseInt(this.getAttribute('data-rating'));
                highlightStars(rating);
            });

            star.addEventListener('mouseout', function() {
                updateStars();
            });
        });

        function highlightStars(rating) {
            Array.from(stars).forEach(star => {
                const starRating = parseInt(star.getAttribute('data-rating'));
                star.classList.toggle('active', starRating <= rating);
            });
        }

        function updateStars() {
            highlightStars(selectedRating);
        }

        function submitReview() {
            if (selectedRating === 0) {
                alert('Please select a rating before submitting.');
                return;
            }

            // First complete the reservation
            const endpoint = '/api/reservations/' + currentReservationId + '/complete';
            
            fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            })
            .then(response => {
                if (response.ok) {
                    // Now save the review
                    return fetch('/api/reviews/add', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        credentials: 'include',
                        body: JSON.stringify({
                            housingId: currentReservationId,
                            cleanlinessRating: selectedRating,
                            locationRating: selectedRating,
                            checkinExperienceRating: selectedRating,
                            valueForMoneyRating: selectedRating,
                            comment: "Owner review"
                        })
                    });
                } else {
                    throw new Error('Failed to complete reservation');
                }
            })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(error => {
                alert('Error: ' + error.message);
            });
        }
    </script>
</body>
</html> 