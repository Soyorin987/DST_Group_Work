<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>访问计数器</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app.css" />
    <style>
        body {
            background: linear-gradient(135deg, #fff9db, #ffe066);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .counter-card {
            max-width: 480px;
            width: 100%;
            border-radius: 20px;
            border: none;
            background: #fffdf5;
            box-shadow: 0 20px 40px rgba(255, 193, 7, 0.25);
            padding: 30px;
            transition: transform 0.2s ease;
        }

        .counter-card:hover {
            transform: translateY(-5px);
        }

        .card-title {
            font-weight: 700;
            color: #795300;
            margin-bottom: 20px;
        }

        .counter-value {
            font-size: 4rem;
            font-weight: 800;
            color: #ffb703;
            margin: 20px 0;
        }

        .text-muted {
            font-size: 1.1rem;
            color: #a67c00 !important;
        }
    </style>
</head>
<body class="bg-light">
<div class="container">
    <div class="card counter-card shadow-sm text-center">
        <div class="card-body">
            <h1 class="card-title">站点访问计数器</h1>
            <p class="counter-value" id="counterValue">${counter}</p>
            <p class="text-muted">页面已被访问 <strong>${counter}</strong> 次</p>
        </div>
    </div>
</div>
</body>
</html>

