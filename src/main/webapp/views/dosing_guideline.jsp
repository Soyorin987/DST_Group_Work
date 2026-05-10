<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Dosing Guidelines</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .search-bar {
            margin-bottom: 18px;
        }

        .summary-cell {
            max-width: 420px;
            white-space: normal;
            line-height: 1.55;
        }

        .text-muted {
            color: #6c757d;
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
            <jsp:param name="active" value="dosing_guideline"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Dosing Guidelines</h2>
            </div>

            <form class="search-bar" method="get" action="<%= request.getContextPath() %>/dosingGuideline">
                <div class="input-group">
                    <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            placeholder="Search by guideline ID, name, drug ID, source or summary"
                            value="${keyword}">
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="submit">Search</button>
                        <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/dosingGuideline">Reset</a>
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
                        <th>ID</th>
                        <th>Name</th>
                        <th>Recommendation</th>
                        <th>Drug ID</th>
                        <th>Source</th>
                        <th>Summary Markdown</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${dosingGuidelines}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.name}</td>
                            <td>${item.recommendation}</td>
                            <td>${item.drugId}</td>
                            <td>${item.source}</td>
                            <td class="summary-cell">
                                <c:choose>
                                    <c:when test="${not empty item.summaryMarkdown}">
                                        ${item.summaryMarkdown}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Not available</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
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