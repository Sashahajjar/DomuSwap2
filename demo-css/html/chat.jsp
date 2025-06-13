<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat with Owner</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #FAF9F7;
            padding: 20px;
        }

        .chat-container {
            background: white;
            border-radius: 8px;
            padding: 20px;
            max-width: 600px;
            margin: auto;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .chat-header {
            font-weight: bold;
            font-size: 1.2rem;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }

        .message-bubble {
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 10px;
            max-width: 80%;
            position: relative;
        }

        .sent {
            background: #7EA780;
            color: white;
            margin-left: auto;
            text-align: right;
        }

        .received {
            background: #E4D9C7;
            color: #000;
            margin-right: auto;
            text-align: left;
        }

        .message-time {
            font-size: 0.7em;
            opacity: 0.7;
            margin-top: 5px;
        }

        .send-form {
            display: flex;
            gap: 10px;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .send-form input[type="text"] {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .send-form button {
            padding: 10px 16px;
            border: none;
            background-color: #7EA780;
            color: white;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
        }

        .send-form button:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <div class="chat-header">
        Chat with <c:out value="${receiver.name}" /> about "<c:out value="${housing.title}" />"
    </div>

    <!-- Real Messages Display -->
    <c:forEach var="msg" items="${messages}">
        <div class="message-bubble ${msg.sender.id == currentUser.id ? 'sent' : 'received'}">
            <div class="message-content"><c:out value="${msg.content}" /></div>
            <div class="message-time">
                <fmt:formatDate value="${msg.createdAt}" pattern="MMM d, yyyy h:mm a" />
            </div>
        </div>
    </c:forEach>

    <!-- Message sending form -->
    <form class="send-form" method="post" action="/api/messages/send">
        <input type="hidden" name="senderId" value="${currentUser.id}" />
        <input type="hidden" name="receiverId" value="${receiver.id}" />
        <input type="hidden" name="housingId" value="${housing.id}" />
        <input type="text" name="content" placeholder="Type your message..." required />
        <button type="submit">Send</button>
    </form>
</div>

<script>
    // Auto-scroll to bottom of messages
    window.onload = function() {
        const messages = document.querySelectorAll('.message-bubble');
        if (messages.length > 0) {
            messages[messages.length - 1].scrollIntoView({ behavior: 'smooth' });
        }
    };

    // Handle form submission
    document.querySelector('.send-form').addEventListener('submit', function(e) {
        e.preventDefault();
        const form = this;
        const formData = new FormData(form);
        
        fetch('/api/messages/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                senderId: formData.get('senderId'),
                receiverId: formData.get('receiverId'),
                housingId: formData.get('housingId'),
                content: formData.get('content')
            })
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                throw new Error('Failed to send message');
            }
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    });
</script>
</body>
</html>
