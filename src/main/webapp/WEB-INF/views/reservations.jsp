<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
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
            width: 100vw;
            height: 100vh;
            background: rgba(44, 62, 80, 0.18);
            z-index: 1000;
        }
        .modal-content {
            background: linear-gradient(135deg, #f8fafb 80%, #e3e9f0 100%);
            width: 96%;
            max-width: 420px;
            margin: 60px auto;
            padding: 36px 32px 28px 32px;
            border-radius: 18px;
            position: relative;
            box-shadow: 0 8px 32px rgba(44,62,80,0.13), 0 1.5px 8px rgba(23,162,184,0.08);
            border: 1.5px solid #e3e9f0;
        }
        .close {
            position: absolute;
            right: 18px;
            top: 18px;
            font-size: 28px;
            cursor: pointer;
            color: #b0b0b0;
            transition: color 0.2s;
        }
        .close:hover {
            color: #138496;
        }
        .review-form {
            display: flex;
            flex-direction: column;
            gap: 22px;
        }
        .review-form h2 {
            font-family: 'Josefin Sans', sans-serif;
            font-size: 1.7rem;
            margin-bottom: 8px;
            color: #193E15;
            letter-spacing: -1px;
        }
        .criteria-row {
            display: flex;
            flex-wrap: wrap;
            gap: 18px 12px;
            margin-bottom: 0;
        }
        .criteria-group {
            flex: 1 1 180px;
            display: flex;
            flex-direction: column;
            min-width: 140px;
        }
        .criteria-group label {
            font-weight: 600;
            color: #145c1b;
            margin-bottom: 4px;
            font-size: 1.04rem;
            letter-spacing: -0.5px;
        }
        .criteria-group select {
            padding: 9px 12px;
            border: 1.5px solid #dbe6e4;
            border-radius: 7px;
            background: #fafdff;
            font-size: 1.01rem;
            font-family: inherit;
            transition: border 0.2s;
        }
        .criteria-group select:focus {
            border: 1.5px solid #17A2B8;
            outline: none;
        }
        .review-form textarea {
            width: 100%;
            padding: 12px;
            border: 1.5px solid #dbe6e4;
            border-radius: 7px;
            font-family: inherit;
            resize: vertical;
            min-height: 90px;
            background: #fafdff;
            font-size: 1.04rem;
            transition: border 0.2s;
        }
        .review-form textarea:focus {
            border: 1.5px solid #17A2B8;
            outline: none;
        }
        .review-form label[for="comment"] {
            font-weight: 600;
            color: #145c1b;
            margin-bottom: 4px;
            font-size: 1.04rem;
        }
        .review-form .btn-review {
            background: linear-gradient(90deg, #17A2B8 60%, #7EA780 100%);
            color: white;
            padding: 14px 0;
            border: none;
            border-radius: 9px;
            font-family: 'Josefin Sans', sans-serif;
            font-size: 1.13rem;
            font-weight: bold;
            cursor: pointer;
            margin-top: 8px;
            transition: background 0.2s, box-shadow 0.2s;
            box-shadow: 0 2px 8px rgba(23,162,184,0.10);
            letter-spacing: 0.5px;
        }
        .review-form .btn-review:hover {
            background: linear-gradient(90deg, #138496 60%, #5e8e5e 100%);
            box-shadow: 0 4px 16px rgba(23,162,184,0.13);
        }
        @media (max-width: 600px) {
            .modal-content {
                padding: 16px 6px 12px 6px;
            }
            .criteria-row {
                flex-direction: column;
                gap: 10px 0;
            }
        }

        /* Chat Styles */
        .chat-container {
            margin-top: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
            display: none;
        }
        .chat-header {
            background: #f8f9fa;
            padding: 10px 15px;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .chat-messages {
            height: 300px;
            overflow-y: auto;
            padding: 15px;
            background: #fff;
        }
        .message {
            margin-bottom: 15px;
            max-width: 80%;
        }
        .message.sent {
            margin-left: auto;
            background: #007bff;
            color: white;
            padding: 10px;
            border-radius: 15px 15px 0 15px;
        }
        .message.received {
            margin-right: auto;
            background: #e9ecef;
            padding: 10px;
            border-radius: 15px 15px 15px 0;
        }
        .chat-input {
            display: flex;
            padding: 10px;
            background: #f8f9fa;
            border-top: 1px solid #ddd;
        }
        .chat-input input {
            flex: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-right: 10px;
        }
        .chat-input button {
            padding: 8px 16px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .chat-input button:hover {
            background: #0056b3;
        }
        .message-time {
            font-size: 0.8em;
            color: #666;
            margin-top: 5px;
        }
        .message.sent .message-time {
            color: #e9ecef;
        }
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="#" onclick="location.reload(); return false;">My Reservations</a>
        <a href="/customer.html">Back to Account</a>
    </div>

    <h1>📋 My Reservations</h1>
    
    <script>
        // Add current user ID
        const currentUserId = "${param.userId}";
    </script>
    
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
                <c:if test="${reservation.status eq 'PENDING' or reservation.status eq 'ACCEPTED'}">
                    <button class="btn btn-cancel" 
                            data-reservation-id="${reservation.id}"
                            onclick="cancelReservation('${reservation.id}')">
                        Cancel Booking
                    </button>
                </c:if>
                <c:if test="${reservation.status eq 'COMPLETED'}">
                    <button class="btn btn-review" onclick="showReviewModal('${reservation.housing.id}')">
                        Leave a Review
                    </button>
                </c:if>
                <c:if test="${reservation.status eq 'PENDING' or reservation.status eq 'ACCEPTED'}">
                    <button class="btn" style="background: #007bff; color: white;" onclick="toggleChat('${reservation.id}')">Chat</button>
                </c:if>
                <c:if test="${reservation.status eq 'ACCEPTED' and reservation.housing.owner.id eq param.userId}">
                    <button class="btn btn-review" onclick="showOwnerReviewModal('${reservation.id}', '${reservation.user.id}', '${reservation.user.name}')">
                        Mark as Complete
                    </button>
                </c:if>
            </div>

            <!-- Chat Container -->
            <c:if test="${reservation.status == 'PENDING' || reservation.status == 'ACCEPTED'}">
                <div class="chat-container" id="chat-${reservation.id}" style="display: none;">
                    <div class="chat-header" data-owner-id="${reservation.housing.owner.id}">
                        Chat with ${reservation.status == 'PENDING' ? reservation.housing.owner.name : reservation.user.name}
                    </div>
                    <div class="chat-messages" id="messages-${reservation.id}">
                        <!-- Messages will be loaded here -->
                    </div>
                    <c:if test="${not empty reservation.housing.owner}">
                        <div class="chat-input">
                            <input type="text" id="message-input-${reservation.id}" 
                                   placeholder="Type your message..." 
                                   onkeypress="handleKeyPress(event, '${reservation.id}', '${reservation.housing.owner.id}')">
                            <button onclick="sendMessage('${reservation.id}', '${reservation.housing.owner.id}')">Send</button>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>
    </c:forEach>

    <!-- Review Modal -->
    <div id="reviewModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeReviewModal()">&times;</span>
            <form id="reviewForm" class="review-form">
                <h2>Write a Review</h2>
                <input type="hidden" id="housingId" name="housingId" />
                <div class="criteria-row">
                  <div class="criteria-group">
                    <label for="cleanlinessRating">Cleanliness</label>
                    <select id="cleanlinessRating" name="cleanlinessRating" required>
                      <option value="5">5 ⭐️ - Excellent</option>
                      <option value="4">4 ⭐️ - Very Good</option>
                      <option value="3">3 ⭐️ - Good</option>
                      <option value="2">2 ⭐️ - Fair</option>
                      <option value="1">1 ⭐️ - Poor</option>
                    </select>
                  </div>
                  <div class="criteria-group">
                    <label for="locationRating">Location</label>
                    <select id="locationRating" name="locationRating" required>
                      <option value="5">5 ⭐️ - Excellent</option>
                      <option value="4">4 ⭐️ - Very Good</option>
                      <option value="3">3 ⭐️ - Good</option>
                      <option value="2">2 ⭐️ - Fair</option>
                      <option value="1">1 ⭐️ - Poor</option>
                    </select>
                  </div>
                </div>
                <div class="criteria-row">
                  <div class="criteria-group">
                    <label for="checkinExperienceRating">Check-In Experience</label>
                    <select id="checkinExperienceRating" name="checkinExperienceRating" required>
                      <option value="5">5 ⭐️ - Excellent</option>
                      <option value="4">4 ⭐️ - Very Good</option>
                      <option value="3">3 ⭐️ - Good</option>
                      <option value="2">2 ⭐️ - Fair</option>
                      <option value="1">1 ⭐️ - Poor</option>
                    </select>
                  </div>
                  <div class="criteria-group">
                    <label for="valueForMoneyRating">Value for Money</label>
                    <select id="valueForMoneyRating" name="valueForMoneyRating" required>
                      <option value="5">5 ⭐️ - Excellent</option>
                      <option value="4">4 ⭐️ - Very Good</option>
                      <option value="3">3 ⭐️ - Good</option>
                      <option value="2">2 ⭐️ - Fair</option>
                      <option value="1">1 ⭐️ - Poor</option>
                    </select>
                  </div>
                </div>
                <div>
                  <label for="comment">Your Review:</label>
                  <textarea id="comment" name="comment" required placeholder="Share your experience..."></textarea>
                </div>
                <button type="submit" class="btn btn-review">Submit Review</button>
            </form>
        </div>
    </div>

    <!-- Owner Review Modal -->
    <div id="ownerReviewModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeOwnerReviewModal()">&times;</span>
            <form id="ownerReviewForm" class="review-form">
                <h2>Review Tenant</h2>
                <input type="hidden" id="ownerReviewReservationId" name="reservationId" />
                <input type="hidden" id="ownerReviewTenantId" name="tenantId" />
                <div class="criteria-row">
                    <div class="criteria-group">
                        <label for="tenantRating">Rating</label>
                        <select id="tenantRating" name="tenantRating" required>
                            <option value="5">5 ⭐️ - Excellent</option>
                            <option value="4">4 ⭐️ - Very Good</option>
                            <option value="3">3 ⭐️ - Good</option>
                            <option value="2">2 ⭐️ - Fair</option>
                            <option value="1">1 ⭐️ - Poor</option>
                        </select>
                    </div>
                </div>
                <div>
                    <label for="tenantComment">Comment (optional):</label>
                    <textarea id="tenantComment" name="tenantComment" placeholder="Share your experience with this tenant..."></textarea>
                </div>
                <button type="submit" class="btn btn-review">Submit Review & Complete</button>
            </form>
        </div>
    </div>

    <script>
        function cancelReservation(reservationId) {
            // Validate reservation ID
            if (!reservationId || isNaN(reservationId)) {
                alert('Invalid reservation ID');
                return;
            }
            
            if (confirm('Are you sure you want to cancel this booking?')) {
                // Construct the URL without using template literals
                const baseUrl = window.location.origin;
                const path = '/api/reservations/' + reservationId + '/cancel';
                const url = baseUrl + path;
                
                fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
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
            const data = {
                housingId: formData.get('housingId'),
                cleanlinessRating: parseInt(formData.get('cleanlinessRating')),
                locationRating: parseInt(formData.get('locationRating')),
                checkinExperienceRating: parseInt(formData.get('checkinExperienceRating')),
                valueForMoneyRating: parseInt(formData.get('valueForMoneyRating')),
                comment: formData.get('comment')
            };
            
            fetch('/api/reviews/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
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

        // Chat Functions
        function loadMessages(reservationId, ownerId) {
            const messagesContainer = document.getElementById('messages-' + reservationId);
            if (!currentUserId) {
                console.error('Current user ID is not defined');
                return;
            }
            
            fetch('/api/messages/' + currentUserId + '?ownerId=' + ownerId)
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
                    messagesContainer.innerHTML = '';
                    messages.forEach(message => {
                        const messageDiv = document.createElement('div');
                        messageDiv.className = 'message ' + (message.sender.id === currentUserId ? 'sent' : 'received');
                        messageDiv.innerHTML = 
                            '<div class="message-content">' + message.content + '</div>' +
                            '<div class="message-time">' + new Date(message.createdAt).toLocaleString() + '</div>';
                        messagesContainer.appendChild(messageDiv);
                    });
                    messagesContainer.scrollTop = messagesContainer.scrollHeight;
                })
                .catch(error => {
                    console.error('Error loading messages:', error);
                    messagesContainer.innerHTML = '<div class="message received">Error loading messages. Please try again.</div>';
                });
        }

        function sendMessage(reservationId, ownerId) {
            const input = document.getElementById('message-input-' + reservationId);
            if (!input) {
                console.error('Message input element not found');
                return;
            }
            
            const content = input.value.trim();
            if (!content) return;

            if (!currentUserId) {
                console.error('Current user ID is not defined');
                return;
            }

            fetch('/api/messages/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                },
                body: JSON.stringify({
                    senderId: currentUserId,
                    receiverId: ownerId,
                    content: content
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to send message');
                }
                return response.json();
            })
            .then(() => {
                input.value = '';
                loadMessages(reservationId, ownerId);
            })
            .catch(error => {
                console.error('Error sending message:', error);
                alert('Failed to send message. Please try again.');
            });
        }

        function handleKeyPress(event, reservationId, ownerId) {
            if (event.key === 'Enter') {
                event.preventDefault();
                sendMessage(reservationId, ownerId);
            }
        }

        function toggleChat(reservationId) {
            const chatContainer = document.getElementById('chat-' + reservationId);
            if (chatContainer.style.display === 'none') {
                chatContainer.style.display = 'block';
                const ownerId = chatContainer.querySelector('.chat-header').getAttribute('data-owner-id');
                if (ownerId && currentUserId) {
                    loadMessages(reservationId, ownerId);
                }
            } else {
                chatContainer.style.display = 'none';
            }
        }

        // Initialize chat for pending and accepted reservations
        document.addEventListener('DOMContentLoaded', function() {
            const chatContainers = document.querySelectorAll('.chat-container');
            chatContainers.forEach(container => {
                const reservationId = container.id.split('-')[1];
                const ownerId = container.querySelector('.chat-header').getAttribute('data-owner-id');
                loadMessages(reservationId, ownerId);
            });
        });

        // Poll for new messages every 5 seconds
        setInterval(() => {
            const visibleChatContainers = document.querySelectorAll('.chat-container[style*="display: block"]');
            visibleChatContainers.forEach(container => {
                const reservationId = container.id.split('-')[1];
                const ownerId = container.querySelector('.chat-header').getAttribute('data-owner-id');
                if (ownerId && currentUserId) {
                    loadMessages(reservationId, ownerId);
                }
            });
        }, 5000);

        function showOwnerReviewModal(reservationId, tenantId, tenantName) {
            document.getElementById('ownerReviewReservationId').value = reservationId;
            document.getElementById('ownerReviewTenantId').value = tenantId;
            document.getElementById('ownerReviewModal').style.display = 'block';
        }
        function closeOwnerReviewModal() {
            document.getElementById('ownerReviewModal').style.display = 'none';
        }
        document.getElementById('ownerReviewForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const reservationId = document.getElementById('ownerReviewReservationId').value;
            const tenantId = document.getElementById('ownerReviewTenantId').value;
            const rating = document.getElementById('tenantRating').value;
            const comment = document.getElementById('tenantComment').value;
            // 1. Complete the reservation
            fetch('/api/reservations/' + reservationId + '/complete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            })
            .then(response => {
                if (response.ok) {
                    // 2. Submit the review
                    return fetch('/api/reviews/add-tenant', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        credentials: 'include',
                        body: JSON.stringify({
                            reservationId: reservationId,
                            tenantId: tenantId,
                            rating: parseInt(rating),
                            comment: comment
                        })
                    });
                } else {
                    throw new Error('Failed to complete reservation');
                }
            })
            .then(response => {
                if (response.ok) {
                    alert('Review submitted and reservation marked as complete!');
                    closeOwnerReviewModal();
                    window.location.reload();
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(error => {
                alert('Error: ' + error.message);
            });
        });
    </script>
</body>
</html>
