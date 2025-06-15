<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Client Messages & Bookings</title>
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
        
        .chat-section {
            flex: 1;
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        
        .booking-card {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        
        .booking-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }
        
        .booking-title {
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
        
        .booking-details {
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
        
        /* Chat Styles */
        .chat-messages {
            height: 400px;
            overflow-y: auto;
            margin-bottom: 20px;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        
        .message {
            margin-bottom: 10px;
            padding: 10px;
            border-radius: 8px;
            max-width: 80%;
        }
        
        .message-sent {
            background: #007bff;
            color: white;
            margin-left: auto;
        }
        
        .message-received {
            background: #e9ecef;
            color: #212529;
        }
        
        .chat-input {
            display: flex;
            gap: 10px;
        }
        
        .chat-input input {
            flex: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .chat-input button {
            padding: 8px 16px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="/owner.html">Dashboard</a>
        <a href="/portfolio.html">My Listings</a>
    </div>

    <h1>📋 Client Messages & Bookings</h1>
    
    <div class="container">
        <div class="bookings-section">
            <h2>Booking Requests</h2>
            <c:forEach var="reservation" items="${reservations}">
                <div class="booking-card">
                    <div class="booking-header">
                        <div class="booking-title">${reservation.housing.title}</div>
                        <div class="booking-status status-${reservation.status.name().toLowerCase()}">
                            ${reservation.status}
                        </div>
                    </div>
                    
                    <div class="booking-details">
                        <div class="detail-row">
                            <div class="detail-label">Guest:</div>
                            <div class="detail-value" data-user-id="${reservation.user.id}">${reservation.user.name}</div>
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
                        <c:if test="${reservation.status == 'PENDING'}">
                            <button class="btn btn-accept" 
                                    data-reservation-id="${reservation.id}"
                                    onclick="updateStatus(this.getAttribute('data-reservation-id'), 'accept')">
                                Accept
                            </button>
                            <button class="btn btn-reject" 
                                    data-reservation-id="${reservation.id}"
                                    onclick="updateStatus(this.getAttribute('data-reservation-id'), 'reject')">
                                Reject
                            </button>
                        </c:if>
                        <c:if test="${reservation.status == 'ACCEPTED'}">
                            <button class="btn btn-complete" 
                                    data-reservation-id="${reservation.id}"
                                    data-user-id="${reservation.user.id}"
                                    data-user-name="${reservation.user.name}"
                                    onclick="handleCompleteClick(this)">
                                Mark as Completed
                            </button>
                        </c:if>
                        <button class="btn" onclick="selectChat(${reservation.user.id}, '${reservation.user.name}')">
                            Chat
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <div class="chat-section">
            <h2>Chat with <span id="currentChatUser">Select a booking to start chatting</span></h2>
            <div class="chat-messages" id="chatMessages">
                <!-- Messages will be loaded here -->
            </div>
            <div class="chat-input">
                <input type="text" id="messageInput" placeholder="Type your message..." disabled>
                <button onclick="sendMessage()" id="sendButton" disabled>Send</button>
            </div>
        </div>
    </div>

    <!-- Review Modal -->
    <div id="reviewModal" class="modal" style="display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4);">
        <div class="modal-content" style="background-color: #FAF9F7; margin: 15% auto; padding: 30px; border: none; width: 90%; max-width: 400px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
            <h2 style="color: #2A2729; margin-bottom: 20px; text-align: center;">Review Tenant</h2>
            <p style="color: #666; text-align: center; margin-bottom: 25px;">How was your experience with <span id="reviewUserName" style="font-weight: bold; color: #2A2729;"></span>?</p>
            
            <form id="reviewForm" onsubmit="submitReview(event)" style="text-align: center;">
                <input type="hidden" id="reviewReservationId" name="reservationId">
                <input type="hidden" id="reviewUserId" name="userId">
                <input type="hidden" id="rating" name="rating" value="0">
                
                <div class="star-rating" style="margin-bottom: 30px;">
                    <span class="star" data-rating="1" style="font-size: 2em; color: #ddd; cursor: pointer; margin: 0 5px;">★</span>
                    <span class="star" data-rating="2" style="font-size: 2em; color: #ddd; cursor: pointer; margin: 0 5px;">★</span>
                    <span class="star" data-rating="3" style="font-size: 2em; color: #ddd; cursor: pointer; margin: 0 5px;">★</span>
                    <span class="star" data-rating="4" style="font-size: 2em; color: #ddd; cursor: pointer; margin: 0 5px;">★</span>
                    <span class="star" data-rating="5" style="font-size: 2em; color: #ddd; cursor: pointer; margin: 0 5px;">★</span>
                </div>
                
                <div style="text-align: center;">
                    <button type="button" onclick="closeReviewModal()" style="padding: 10px 20px; margin-right: 10px; background: #6c757d; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: bold;">Cancel</button>
                    <button type="submit" style="padding: 10px 20px; background: #28a745; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: bold;">Submit Review</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Global variables
        const ownerId = ${param.ownerId};
        let currentUserId = null;
        
        // Get CSRF token from meta tag
        function getCsrfToken() {
            return document.querySelector('meta[name="_csrf"]').getAttribute('content');
        }
        
        // Handle the complete button click
        function handleCompleteClick(button) {
            console.log('Complete button clicked');
            const reservationId = button.getAttribute('data-reservation-id');
            const userId = button.getAttribute('data-user-id');
            const userName = button.getAttribute('data-user-name');
            
            console.log('Reservation ID:', reservationId);
            console.log('User ID:', userId);
            console.log('User Name:', userName);
            
            openReviewModal(reservationId, userId, userName);
        }
        
        // Utility functions
        function updateStatus(reservationId, action) {
            console.log('Updating status:', { reservationId, action });
            
            if (action === 'complete') {
                // For complete action, we'll handle it in handleCompleteClick
                return Promise.resolve();
            }
            
            // For other actions (accept/reject), proceed as normal
            const endpoint = '/api/reservations/' + reservationId + '/' + action;
            
            return fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': getCsrfToken()
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
                console.error('Error updating status:', error);
                alert('Error: ' + error.message);
            });
        }
        
        function selectChat(userId, userName) {
            currentUserId = userId;
            document.getElementById('currentChatUser').textContent = userName;
            document.getElementById('messageInput').disabled = false;
            document.getElementById('sendButton').disabled = false;
            loadMessages(userId);
        }
        
        function loadMessages(userId) {
            if (!userId) {
                console.log('No user selected for chat');
                return;
            }
            
            fetch('/api/messages/' + userId + '?ownerId=' + ownerId)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to load messages');
                    }
                    return response.json();
                })
                .then(messages => {
                    if (!Array.isArray(messages)) {
                        console.error('Expected array of messages but got:', messages);
                        return;
                    }
                    const chatMessages = document.getElementById('chatMessages');
                    let messagesHtml = '';
                    for (let i = 0; i < messages.length; i++) {
                        const msg = messages[i];
                        const messageClass = msg.sender.id == ownerId ? 'message-sent' : 'message-received';
                        messagesHtml += '<div class="message ' + messageClass + '">' +
                            '<div class="message-content">' + msg.content + '</div>' +
                            '<div class="message-time">' + msg.createdAt + '</div>' +
                            '</div>';
                    }
                    chatMessages.innerHTML = messagesHtml;
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                })
                .catch(error => {
                    console.error('Error loading messages:', error);
                    const chatMessages = document.getElementById('chatMessages');
                    chatMessages.innerHTML = '<div class="message message-received">Error loading messages. Please try again.</div>';
                });
        }
        
        function sendMessage() {
            const input = document.getElementById('messageInput');
            const content = input.value.trim();
            
            if (!content || !currentUserId) {
                return;
            }
            
            fetch('/api/messages/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': getCsrfToken()
                },
                body: JSON.stringify({
                    senderId: ownerId,
                    receiverId: currentUserId,
                    content: content
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to send message');
                }
                return response.json();
            })
            .then(savedMessage => {
                input.value = '';
                loadMessages(currentUserId);
            })
            .catch(error => {
                console.error('Error sending message:', error);
                alert('Failed to send message. Please try again.');
            });
        }
        
        function logout() {
            localStorage.removeItem('loggedInUser');
            window.location.href = '/main_page.html';
        }
        
        // Event Listeners
        document.addEventListener('DOMContentLoaded', function() {
            // Handle Enter key in message input
            document.getElementById('messageInput').addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    sendMessage();
                }
            });
            
            // Poll for new messages every 5 seconds
            setInterval(() => {
                if (currentUserId) {
                    loadMessages(currentUserId);
                }
            }, 5000);
        });

        // Review Modal Functions
        function openReviewModal(reservationId, userId, userName) {
            console.log('Opening review modal for:', reservationId, userId, userName);
            document.getElementById('reviewReservationId').value = reservationId;
            document.getElementById('reviewUserId').value = userId;
            document.getElementById('reviewUserName').textContent = userName;
            document.getElementById('reviewModal').style.display = 'block';
        }
        
        function closeReviewModal() {
            document.getElementById('reviewModal').style.display = 'none';
        }
        
        // Star Rating Functionality
        document.addEventListener('DOMContentLoaded', function() {
            const stars = document.querySelectorAll('.star');
            const ratingInput = document.getElementById('rating');
            
            stars.forEach(star => {
                star.addEventListener('mouseover', function() {
                    const rating = this.getAttribute('data-rating');
                    highlightStars(rating);
                });
                
                star.addEventListener('mouseout', function() {
                    const currentRating = ratingInput.value;
                    highlightStars(currentRating);
                });
                
                star.addEventListener('click', function() {
                    const rating = this.getAttribute('data-rating');
                    ratingInput.value = rating;
                    highlightStars(rating);
                });
            });
        });
        
        function highlightStars(rating) {
            const stars = document.querySelectorAll('.star');
            stars.forEach(star => {
                const starRating = star.getAttribute('data-rating');
                if (starRating <= rating) {
                    star.style.color = '#FFD700'; // Gold color for selected stars
                } else {
                    star.style.color = '#ddd'; // Gray color for unselected stars
                }
            });
        }
        
        function submitReview(event) {
            event.preventDefault();
            console.log('Submitting review...');
            
            const reservationId = document.getElementById('reviewReservationId').value;
            const userId = document.getElementById('reviewUserId').value;
            const rating = document.getElementById('rating').value;
            
            if (rating === '0') {
                alert('Please select a rating before submitting.');
                return;
            }
            
            console.log('Review data:', { reservationId, userId, rating });
            
            // Submit the owner-to-customer review
            fetch('/api/reviews/customer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': getCsrfToken()
                },
                body: JSON.stringify({
                    reservationId: reservationId,
                    customerId: userId,
                    rating: parseInt(rating)
                })
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                console.log('Review submitted successfully');
                // After review is submitted, update the reservation status
                return fetch('/api/reservations/' + reservationId + '/complete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': getCsrfToken()
                    }
                });
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                console.log('Status updated successfully');
                // Hide the review modal
                closeReviewModal();
                // Hide the "Mark as Complete" button
                const completeButton = document.querySelector(`button[data-reservation-id="${reservationId}"]`);
                if (completeButton) {
                    completeButton.style.display = 'none';
                }
                // Show success message
                alert('Review submitted successfully and reservation marked as complete!');
                // Reload the page to reflect changes
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to submit review: ' + error.message);
            });
        }
    </script>
</body>
</html> 