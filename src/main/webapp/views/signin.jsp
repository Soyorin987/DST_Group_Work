<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>

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

        .signin-card {
            width: 760px;
            max-width: 90%;
            background: linear-gradient(180deg, #f7fbff, #eaf4ff);
            border-radius: 16px;
            padding: 48px 64px;
            box-shadow: 0 18px 45px rgba(15, 23, 42, 0.12);
            border: 1px solid rgba(15, 23, 42, 0.06);
        }

        .signin-title {
            margin: 0;
            text-align: center;
            font-size: 42px;
            font-weight: 700;
            letter-spacing: 1px;
        }

        .signin-subtitle {
            margin-top: 12px;
            margin-bottom: 32px;
            text-align: center;
            font-size: 18px;
            color: #6b7280;
        }

        .form-area {
            max-width: 470px;
            margin: 0 auto;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 17px;
            font-weight: 600;
            color: #374151;
        }

        .form-group input {
            width: 100%;
            height: 48px;
            padding: 0 16px;
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

        .message-success {
            margin-bottom: 18px;
            padding: 12px 14px;
            border-radius: 8px;
            background: #dcfce7;
            color: #166534;
            font-size: 14px;
        }

        .message-error {
            margin-bottom: 18px;
            padding: 12px 14px;
            border-radius: 8px;
            background: #fee2e2;
            color: #b91c1c;
            font-size: 14px;
        }

        .button-row {
            margin-top: 26px;
            display: flex;
            justify-content: center;
        }

        .signin-button {
            min-width: 160px;
            height: 48px;
            border: none;
            border-radius: 10px;
            background: #2563eb;
            color: #ffffff;
            font-size: 17px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s, transform 0.1s;
        }

        .signin-button:hover {
            background: #1d4ed8;
        }

        .signin-button:active {
            transform: translateY(1px);
        }

        .helper-row {
            margin-top: 28px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 16px;
            color: #6b7280;
        }

        .helper-row a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 600;
        }

        .helper-row a:hover {
            text-decoration: underline;
        }

        @media (max-width: 600px) {
            .signin-card {
                padding: 36px 28px;
            }

            .signin-title {
                font-size: 34px;
            }

            .helper-row {
                flex-direction: column;
                gap: 12px;
            }
        }
    </style>
</head>

<body>
<div class="signin-card">
    <h2 class="signin-title">Sign In</h2>
    <p class="signin-subtitle">Welcome back — please sign in to continue</p>

    <div class="form-area">

        <c:if test="${not empty param.registered}">
            <div class="message-success">
                Registration successful. Please sign in.
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="message-error">
                    ${error}
            </div>
        </c:if>

        <form method="post" action="<%= request.getContextPath() %>/login">
            <div class="form-group">
                <label for="username">Username</label>
                <input id="username" name="username" required autofocus />
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input id="password" name="password" type="password" required />
            </div>

            <div class="button-row">
                <button class="signin-button" type="submit">Sign in</button>
            </div>

            <div class="helper-row">
                <div>
                    New?
                    <a href="<%= request.getContextPath() %>/register">Registration</a>
                </div>

                <div>
                    <a href="#">Forgot?</a>
                </div>
            </div>
        </form>

    </div>
</div>
</body>
</html>