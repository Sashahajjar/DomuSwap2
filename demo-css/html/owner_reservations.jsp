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
    </style>
</head>
<body>
    <div class="nav-menu">
        <a href="/owner">Dashboard</a>
        <a href="/owner/reservations?ownerId=${param.ownerId}">Reservations</a>
        <a href="/portfolio">My Listings</a>
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

    <script>
        function updateStatus(reservationId, action) {
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
    </script>
</body>
</html> 