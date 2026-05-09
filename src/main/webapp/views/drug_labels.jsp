<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Drug Labels</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .search-bar {
            margin-bottom: 18px;
        }

        .summary-cell {
            max-width: 360px;
            white-space: normal;
        }

        .extended-cell {
            max-width: 360px;
            white-space: normal;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="<%= request.getContextPath() %>/">
        Precision Medicine Matching System
    </a>
</nav>

<div class="container-fluid">
    <div class="row">

        <jsp:include page="nav.jsp">
            <jsp:param name="active" value="drug_labels"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Drug Labels</h2>
            </div>

            <form class="search-bar" method="get" action="<%= request.getContextPath() %>/drugLabels">
                <div class="input-group">
                    <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            placeholder="Search by label ID, source, summary, efficacy, warning, alternative drug or drug ID"
                            value="${keyword}">
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="submit">Search</button>
                        <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/drugLabels">Reset</a>
                    </div>
                </div>
            </form>

            <c:if test="${not empty keyword}">
                <p class="text-muted">
                    Search result for: <strong>${keyword}</strong>
                </p>
            </c:if>

            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Source</th>
                        <th>Dosing Information</th>
                        <th>Summary Markdown</th>
                        <th>Efficacy Summary</th>
                        <th>Response Warning</th>
                        <th>Alternative Drug</th>
                        <th>Drug Id</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${drugLabels}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.source}</td>
                            <td>${item.dosingInformation}</td>
                            <td class="summary-cell">${item.summaryMarkdown}</td>
                            <td class="extended-cell">${item.efficacySummary}</td>
                            <td class="extended-cell">${item.responseWarning}</td>
                            <td class="extended-cell">${item.alternativeDrug}</td>
                            <td>${item.drugId}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </main>
    </div>
</div>

</body>
</html>