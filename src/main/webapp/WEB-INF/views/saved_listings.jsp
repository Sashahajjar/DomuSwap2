<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Saved Listings</title>
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
        
        .saved-listings-section {
            max-width: 800px;
            margin: 0 auto;
        }
        
        .listing-card {
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
        
        .listing-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        
        .listing-description {
            color: #666;
            margin-bottom: 15px;
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
        
        .btn-chat {
            background: #007bff;
            color: white;
        }
        
        .btn-unsave {
            background: #dc3545;
            color: white;
        }
        
        .btn:hover {
            opacity: 0.9;
        }
        
        .empty-state {
            text-align: center;
            padding: 40px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        
        .empty-state p {
            color: #666;
            margin: 10px 0;
        }
    </style>
</head>

<body>
    <div class="nav-menu">
        <a href="customer.html">Dashboard</a>
        <a href="/saved_listings?userId=${param.userId}">Saved Listings</a>
    </div>

    <h1>📋 My Saved Listings</h1>
    
    <div class="saved-listings-section">
        <c:if test="${empty savedListings}">
            <div class="empty-state">
                <h2>No Saved Listings Yet</h2>
                <p>Start exploring and save your favorite listings!</p>
                <a href="house_selection.html" class="btn btn-chat">Browse Listings</a>
            </div>
        </c:if>
        
        <c:forEach var="listing" items="${savedListings}">
            ID: ${listing.id} <br>
            <div class="listing-card">
                <img src="${listing.photo_1}" alt="${listing.title}" class="listing-image" onerror="this.src='/images/logo.png'">
                <div class="card-header">
                    <div class="card-title">${listing.title}</div>
                </div>
                <div class="listing-description">${listing.description}</div>
                <div class="action-buttons">
                    <button class="btn btn-unsave" onclick="removeSavedListing('${listing.id}')">
                        Remove from Saved
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>

    <script>
        function startChat(ownerId) {
            console.log('Starting chat with owner ID:', ownerId); // Debug log
            
            if (!ownerId || isNaN(ownerId)) {
                alert('Error: Invalid owner ID');
                return;
            }
            
            const user = JSON.parse(localStorage.getItem('loggedInUser'));
            console.log('Logged in user:', user); // Debug log
            
            if (!user || !user.id) {
                alert('Please log in to start a chat');
                window.location.href = '/main_page.html';
                return;
            }
            
            // Ensure we have a valid user ID
            const userId = user.id;
            if (!userId || isNaN(userId)) {
                alert('Error: Invalid user ID. Please log in again.');
                localStorage.removeItem('loggedInUser');
                window.location.href = '/main_page.html';
                return;
            }
            
            // Construct the URL with both parameters
            const chatUrl = `/chat?userId=${userId}&receiverId=${ownerId}`;
            console.log('Redirecting to chat:', chatUrl); // Debug log
            window.location.href = chatUrl;
        }
        
        function removeSavedListing(listingId) {
            console.log('Raw listingId:', listingId, 'type:', typeof listingId);
            const housingId = parseInt(listingId);
            console.log('Parsed housingId:', housingId, 'type:', typeof housingId);
            const userId = parseInt(new URLSearchParams(window.location.search).get('userId'));
            if (isNaN(housingId) || isNaN(userId)) {
                alert('Error: Invalid ID values');
                return;
            }
            const url = "/api/saved-listings?userId=" + userId + "&housingId=" + housingId;
            console.log('DELETE URL:', url);
            if (confirm('Remove this listing from your saved items?')) {
                fetch(url, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        throw new Error('Failed to remove listing');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error: ' + error.message);
                });
            }
        }
    </script>
</body>
</html> 