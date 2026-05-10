<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="cn.edu.zju.bean.User" %>
<%@ page import="cn.edu.zju.bean.Drug" %>
<%@ page import="cn.edu.zju.dao.FavoriteDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    List<Drug> favoriteDrugs = Collections.emptyList();
    String loadError = null;

    try {
        FavoriteDao favoriteDao = new FavoriteDao();
        favoriteDrugs = favoriteDao.findFavoriteDrugsByUserId(currentUser.getId());
    } catch (Exception e) {
        loadError = e.getMessage();
        e.printStackTrace();
    }

    request.setAttribute("favoriteDrugs", favoriteDrugs);
    request.setAttribute("loadError", loadError);
    request.setAttribute("currentUserId", currentUser.getId());
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Favorite Drugs</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .page-description {
            color: #6c757d;
            margin-bottom: 18px;
            line-height: 1.6;
        }

        .empty-box {
            margin-top: 24px;
            padding: 28px;
            border: 1px solid #e5e7eb;
            border-radius: 10px;
            background: #f8f9fa;
            color: #6c757d;
        }

        .drug-url-cell {
            max-width: 420px;
            word-break: break-all;
        }

        .favorite-note {
            font-size: 14px;
            color: #6c757d;
            margin-bottom: 12px;
        }

        .favorite-badge {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 999px;
            background-color: #eaf2ff;
            color: #0056b3;
            font-size: 13px;
            font-weight: 600;
        }

        .remove-btn {
            border: none;
            background: #dc3545;
            color: #ffffff;
            border-radius: 6px;
            padding: 4px 10px;
            cursor: pointer;
            font-size: 13px;
        }

        .remove-btn:hover {
            background: #c82333;
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
            <jsp:param name="active" value="favorites"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Favorite Drugs</h2>
            </div>

            <p class="page-description">
                This page displays the drugs saved by the current user. The records are generated from the
                <strong>favorites</strong> table and linked back to the corresponding drug information in the
                <strong>drug</strong> table.
            </p>

            <c:if test="${not empty loadError}">
                <div class="alert alert-danger">
                    Failed to load favorite drugs: ${loadError}
                </div>
            </c:if>

            <c:choose>

                <c:when test="${empty favoriteDrugs}">
                    <div class="empty-box">
                        <h5>No favorite drugs yet.</h5>
                        <p>
                            You have not saved any drugs to your Favorite list. You can go to the Drugs page
                            and click the star button to add drugs here.
                        </p>
                        <a class="btn btn-primary btn-sm" href="<%= request.getContextPath() %>/drugs">
                            Go to Drugs
                        </a>
                    </div>
                </c:when>

                <c:otherwise>

                    <div class="favorite-note">
                        <span class="favorite-badge">
                            ${fn:length(favoriteDrugs)} favorite drug(s)
                        </span>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-striped table-sm">
                            <thead>
                            <tr>
                                <th>Drug ID</th>
                                <th>Name</th>
                                <th>Class</th>
                                <th>Drug URL</th>
                                <th>Biomarker</th>
                                <th>Action</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${favoriteDrugs}" var="drug">
                                <tr>
                                    <td>${drug.id}</td>
                                    <td>${drug.name}</td>
                                    <td>${drug.objCls}</td>
                                    <td class="drug-url-cell">
                                        <a href="${drug.drugUrl}" target="_blank">
                                                ${drug.drugUrl}
                                        </a>
                                    </td>
                                    <td>${drug.biomarker}</td>
                                    <td>
                                        <button
                                                type="button"
                                                class="remove-btn"
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
    $(document).on("click", ".remove-btn", function () {
        const button = $(this);
        const resourceId = button.data("resource-id");

        $.ajax({
            url: "<%= request.getContextPath() %>/favorites/remove",
            method: "POST",
            data: {
                resourceType: "drug",
                resourceId: resourceId
            },
            success: function () {
                window.location.reload();
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.href = "<%= request.getContextPath() %>/login";
                } else {
                    alert("Failed to remove favorite drug.");
                }
            }
        });
    });
</script>

</body>
</html>