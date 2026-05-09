<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Drugs</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .favorite-btn {
            width: 26px;
            height: 26px;
            border-radius: 50%;
            border: 1px solid #999;
            background: #ffffff;
            cursor: pointer;
            font-size: 14px;
            line-height: 22px;
            text-align: center;
            color: #ffffff;
        }

        .favorite-btn.favorited {
            background: #007bff;
            border-color: #007bff;
            color: #ffffff;
        }

        .favorite-btn:hover {
            border-color: #007bff;
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

            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th>Favorite</th>
                        <th>id</th>
                        <th>name</th>
                        <th>obj_cls</th>
                        <th>drug_url</th>
                        <th>biomarker</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${drugs}" var="drug">
                        <tr>
                            <td>
                                <button
                                        type="button"
                                        class="favorite-btn ${drug.favorited ? 'favorited' : ''}"
                                        data-resource-type="drug"
                                        data-resource-id="${drug.id}"
                                        data-favorited="${drug.favorited}">
                                        ${drug.favorited ? '✓' : ''}
                                </button>
                            </td>
                            <td>${drug.id}</td>
                            <td>${drug.name}</td>
                            <td>${drug.objCls}</td>
                            <td>
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
    document.addEventListener("DOMContentLoaded", function () {
        const contextPath = "<%= request.getContextPath() %>";

        document.querySelectorAll(".favorite-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                const resourceType = btn.getAttribute("data-resource-type");
                const resourceId = btn.getAttribute("data-resource-id");
                const isFavorited = btn.getAttribute("data-favorited") === "true";

                const url = isFavorited
                    ? contextPath + "/favorites/remove"
                    : contextPath + "/favorites/add";

                const body = "resourceType=" + encodeURIComponent(resourceType)
                    + "&resourceId=" + encodeURIComponent(resourceId);

                fetch(url, {
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
                            if (isFavorited) {
                                btn.classList.remove("favorited");
                                btn.setAttribute("data-favorited", "false");
                                btn.textContent = "";
                            } else {
                                btn.classList.add("favorited");
                                btn.setAttribute("data-favorited", "true");
                                btn.textContent = "✓";
                            }
                        } else {
                            alert(data.msg || "Failed to update favorite.");
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