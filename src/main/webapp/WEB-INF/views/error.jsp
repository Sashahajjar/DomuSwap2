<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #FAF9F7;
            padding: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .error-container {
            background: white;
            border-radius: 8px;
            padding: 30px;
            max-width: 500px;
            width: 100%;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .error-icon {
            font-size: 48px;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .error-title {
            color: #2A2729;
            font-size: 24px;
            margin-bottom: 15px;
        }

        .error-message {
            color: #666;
            margin-bottom: 25px;
        }

        .back-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #7EA780;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: opacity 0.2s;
        }

        .back-button:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1 class="error-title">Oops! Something went wrong</h1>
        <p class="error-message">
            <c:out value="${error}" default="An unexpected error occurred. Please try again." />
        </p>
        <a href="javascript:history.back()" class="back-button">Go Back</a>
    </div>
</body>
</html> 