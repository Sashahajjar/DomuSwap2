<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
        }

        .message-bubble {
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 10px;
            max-width: 80%;
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

        .send-form {
            display: flex;
            gap: 10px;
            margin-top: 20px;
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
            <c:out value="${msg.content}" />
        </div>
    </c:forEach>

    <!-- Message sending form -->
    <form class="send-form" method="post" action="/send-message">
        <input type="hidden" name="senderId" value="${currentUser.id}" />
        <input type="hidden" name="receiverId" value="${receiver.id}" />
        <input type="hidden" name="housingId" value="${housing.id}" />
        <input type="text" name="content" placeholder="Type your message..." required />
        <button type="submit">Send</button>
    </form>
</div>
</body>
</html>
