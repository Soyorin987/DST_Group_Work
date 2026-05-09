<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: Arial, Helvetica, sans-serif;
            background: #f4f7fb;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #1f2937;
        }

        .register-card {
            width: 480px;
            max-width: 90%;
            background: #ffffff;
            border-radius: 16px;
            padding: 42px 48px;
            box-shadow: 0 18px 45px rgba(15, 23, 42, 0.12);
        }

        .register-title {
            margin: 0;
            text-align: center;
            font-size: 36px;
            font-weight: 700;
            letter-spacing: 1px;
        }

        .register-subtitle {
            margin-top: 10px;
            margin-bottom: 30px;
            text-align: center;
            font-size: 16px;
            color: #6b7280;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 15px;
            font-weight: 600;
            color: #374151;
        }

        .form-group input {
            width: 100%;
            height: 44px;
            padding: 0 14px;
            border: 1px solid #d1d5db;
            border-radius: 10px;
            font-size: 16px;
            background: #f9fafb;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s, background 0.2s;
        }

        .form-group input:focus {
            border-color: #2563eb;
            background: #ffffff;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .error-message {
            margin-bottom: 18px;
            padding: 10px 12px;
            border-radius: 8px;
            background: #fee2e2;
            color: #b91c1c;
            font-size: 14px;
        }

        .button-row {
            margin-top: 24px;
            display: flex;
            justify-content: center;
        }

        .register-button {
            min-width: 140px;
            height: 44px;
            border: none;
            border-radius: 10px;
            background: #2563eb;
            color: #ffffff;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s, transform 0.1s;
        }

        .register-button:hover {
            background: #1d4ed8;
        }

        .register-button:active {
            transform: translateY(1px);
        }

        .login-link-row {
            margin-top: 22px;
            text-align: center;
            font-size: 15px;
            color: #6b7280;
        }

        .login-link-row a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 600;
        }

        .login-link-row a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>
<div class="register-card">
    <h2 class="register-title">Register</h2>
    <p class="register-subtitle">Create your account to continue</p>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <form method="post" action="<%= request.getContextPath() %>/register">
        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" name="username" required />
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" required />
        </div>

        <div class="form-group">
            <label for="authorization">Authorization</label>
            <input id="authorization" name="authorization" placeholder="Enter authorization code if required" />
        </div>

        <div class="button-row">
            <button class="register-button" type="submit">Register</button>
        </div>

        <div class="login-link-row">
            Already have an account?
            <a href="<%= request.getContextPath() %>/views/signin.jsp">Back to Login</a>
        </div>
    </form>
</div>
</body>
</html>