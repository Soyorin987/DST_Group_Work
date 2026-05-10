<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Drugs</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .search-bar {
            margin-bottom: 18px;
        }

        .favorite-btn {
            border: none;
            background: transparent;
            font-size: 18px;
            cursor: pointer;
            color: #999;
        }

        .favorite-btn.favorited {
            color: #007bff;
            font-weight: bold;
        }

        .drug-url-cell {
            max-width: 360px;
            word-break: break-all;
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
            <jsp:param name="active" value="drugs"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Drugs</h2>
            </div>

            <form class="search-bar" method="get" action="<%= request.getContextPath() %>/drugs">
                <div class="input-group">
                    <input
                            type="text"
                            name="keyword"
                            class="form-control"
                            placeholder="Search by drug ID, name, class, URL or biomarker"
                            value="${keyword}">
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="submit">Search</button>
                        <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/drugs">Reset</a>
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
                        <th>Favorite</th>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Class</th>
                        <th>Drug URL</th>
                        <th>Biomarker</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${drugs}" var="drug">
                        <tr>
                            <td>
                                <button
                                        type="button"
                                        class="favorite-btn ${drug.favorited ? 'favorited' : ''}"
                                        data-resource-id="${drug.id}">
                                        ${drug.favorited ? '✓' : '☆'}
                                </button>
                            </td>
                            <td>${drug.id}</td>
                            <td>${drug.name}</td>
                            <td>${drug.objCls}</td>
                            <td class="drug-url-cell">
                                <a href="${drug.drugUrl}" target="_blank">${drug.drugUrl}</a>
                            </td>
                            <td>${drug.biomarker}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </main>
    </div>
</div>

<script>
    $(document).on("click", ".favorite-btn", function () {
        const button = $(this);
        const resourceId = button.data("resource-id");
        const isFavorited = button.hasClass("favorited");

        const url = isFavorited
            ? "<%= request.getContextPath() %>/favorites/remove"
            : "<%= request.getContextPath() %>/favorites/add";

        $.ajax({
            url: url,
            method: "POST",
            data: {
                resourceType: "drug",
                resourceId: resourceId
            },
            success: function () {
                if (isFavorited) {
                    button.removeClass("favorited");
                    button.text("☆");
                } else {
                    button.addClass("favorited");
                    button.text("✓");
                }
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.href = "<%= request.getContextPath() %>/login";
                } else {
                    alert("Failed to update favorite status.");
                }
            }
        });
    });
</script>

</body>
</html>