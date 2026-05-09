<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Favorite Drugs</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">
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
            <jsp:param name="active" value="favorites"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Favorite Drugs</h2>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                        ${error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty drugs}">
                    <div class="alert alert-secondary">
                        No favorite drugs yet.
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>id</th>
                                <th>name</th>
                                <th>obj_cls</th>
                                <th>drug_url</th>
                                <th>biomarker</th>
                                <th>Action</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${drugs}" var="drug">
                                <tr id="favorite-row-${drug.id}">
                                    <td>${drug.id}</td>
                                    <td>${drug.name}</td>
                                    <td>${drug.objCls}</td>
                                    <td>
                                        <a href="${drug.drugUrl}" target="_blank">${drug.drugUrl}</a>
                                    </td>
                                    <td>${drug.biomarker}</td>
                                    <td>
                                        <button
                                                type="button"
                                                class="btn btn-sm btn-outline-danger remove-btn"
                                                data-resource-type="drug"
                                                data-resource-id="${drug.id}">
                                            Remove
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>

        </main>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const contextPath = "<%= request.getContextPath() %>";

        document.querySelectorAll(".remove-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                const resourceType = btn.getAttribute("data-resource-type");
                const resourceId = btn.getAttribute("data-resource-id");

                const body = "resourceType=" + encodeURIComponent(resourceType)
                    + "&resourceId=" + encodeURIComponent(resourceId);

                fetch(contextPath + "/favorites/remove", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                    },
                    body: body
                })
                    .then(function (response) {
                        if (response.status === 401) {
                            window.location.href = contextPath + "/login";
                            return null;
                        }

                        if (!response.ok) {
                            throw new Error("HTTP " + response.status);
                        }

                        return response.json();
                    })
                    .then(function (data) {
                        if (data === null) {
                            return;
                        }

                        if (data.ok) {
                            const row = document.getElementById("favorite-row-" + resourceId);
                            if (row) {
                                row.remove();
                            }
                        } else {
                            alert(data.msg || "Failed to remove favorite.");
                        }
                    })
                    .catch(function (error) {
                        console.error(error);
                        alert("网络错误，请稍后重试");
                    });
            });
        });
    });
</script>

</body>
</html>