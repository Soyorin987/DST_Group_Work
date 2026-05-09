<!-- html -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Sign In</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app.css">
    <style>
        :root {
            --bg: #f5f8fb;
            --card-bg: #eaf4ff; /* 淡蓝色卡片背景 */
            --card-gradient: linear-gradient(180deg,#f7fbff,#eaf4ff);
            --accent: #0d6efd;
            --muted: #6c757d;
            --radius: 14px;
            --maxw: 560px;
            --base-font: 18px;
        }
        html, body {
            height: 100%;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial;
            font-size: var(--base-font);
            background: var(--bg);
            color: #222;
            margin: 0;
        }
        /* 使用视口高度保证垂直居中 */
        .page-wrap {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }
        .signin-card {
            width: 100%;
            max-width: var(--maxw);
            background: var(--card-gradient);
            border-radius: var(--radius);
            box-shadow: 0 12px 40px rgba(12,20,40,0.08);
            padding: 34px 40px;
            border: 1px solid rgba(13,54,92,0.06);
        }
        .signin-header {
            text-align: center;
            margin-bottom: 18px;
        }
        .signin-header h2 {
            margin: 0;
            font-size: 2rem;
            font-weight: 600;
            color: #0b1220;
        }
        .lead {
            color: var(--muted);
            font-size: 1rem;
            margin-top: 8px;
        }
        .form-control {
            font-size: 1rem;
            padding: 0.95rem 1rem;
            border-radius: 10px;
            border: 1px solid rgba(0,0,0,0.12);
            background: rgba(255,255,255,0.9);
        }
        .btn-primary {
            font-size: 1rem;
            padding: 0.75rem;
            border-radius: 10px;
            box-shadow: none;
            background-image: linear-gradient(180deg, var(--accent), #084dbb);
            border: none;
        }
        .form-group + .form-group { margin-top: 14px; }
        .helper-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 14px;
            font-size: 0.95rem;
        }
        .helper-row a { color: var(--accent); text-decoration: none; }
        .small-link { color: var(--muted); font-size: 0.95rem; }
        @media (max-width: 576px) {
            :root { --base-font: 16px; }
            .signin-card { padding: 20px; border-radius: 12px; }
        }
    </style>
</head>
<body>
<div class="page-wrap">
    <div class="signin-card">
        <div class="signin-header">
            <h2>Sign In</h2>
            <div class="lead">Welcome back — please sign in to continue</div>
        </div>

        <c:if test="${not empty param.registered}">
            <div class="alert alert-success" role="alert">Registration successful. Please sign in.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" class="mt-2">
            <div class="form-group">
                <label class="sr-only">Username</label>
                <input name="username" class="form-control" required autofocus placeholder="Username"/>
            </div>
            <div class="form-group">
                <label class="sr-only">Password</label>
                <input name="password" type="password" class="form-control" required placeholder="Password"/>
            </div>

            <div class="form-group" style="margin-top:14px;">
                <button class="btn btn-primary btn-block w-100" type="submit">Sign in</button>
            </div>

            <div class="helper-row">
                <div class="small-link">New? <a href="${pageContext.request.contextPath}/views/register.jsp">Registration</a></div>
                <div><a href="#" class="text-muted">Forgot?</a></div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
