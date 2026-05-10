<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Matching Result</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.3/examples/dashboard/">

    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

    <style>
        body {
            font-size: .875rem;
        }

        .sidebar {
            position: fixed;
            top: 56px;
            bottom: 0;
            left: 0;
            z-index: 100;
            padding: 48px 0 0;
            box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
        }

        .sidebar-sticky {
            position: relative;
            top: 0;
            height: calc(100vh - 48px);
            padding-top: .5rem;
            overflow-x: hidden;
            overflow-y: auto;
        }

        .main-content {
            padding-top: 80px;
        }

        .summary-cell {
            max-width: 650px;
            white-space: normal;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0"
       href="${pageContext.request.contextPath}/">
        Precision Medicine Matching System
    </a>
</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp"/>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4 main-content">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">Matching Result</h1>
            </div>

            <div class="alert alert-info">
                The matched drug labels are provided as pharmacogenomic reference information only.
                They should not be interpreted as direct clinical recommendations.
            </div>

            <h4>Sample Info #${sample.id}</h4>
            <p>
                <strong>Uploaded at:</strong> ${sample.createdAt}<br>
                <strong>Uploaded by:</strong> ${sample.uploadedBy}
            </p>

            <hr>

            <h4>Matched Drug Labels</h4>

            <c:choose>
                <c:when test="${empty matched}">
                    <div class="alert alert-warning">
                        No matched drug labels were found for this sample.
                    </div>
                </c:when>

                <c:otherwise>
                    <table class="table table-striped table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <th style="width: 60px;">#</th>
                            <th style="width: 220px;">Name</th>
                            <th style="width: 120px;">Source</th>
                            <th>Summary</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${matched}" var="item" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${item.name}</td>
                                <td>${item.source}</td>
                                <td class="summary-cell">${item.summaryMarkdown}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/samples">
                Back to Samples
            </a>
        </main>
    </div>
</div>

</body>
</html>