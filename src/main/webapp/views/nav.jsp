<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 17:04
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activeMenu" value="${param.active != null ? param.active : active}" />

<nav class="col-md-2 d-none d-md-block bg-light sidebar">
    <div class="sidebar-sticky">

        <div class="sidebar-top" style="padding:16px;">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a class="btn btn-primary btn-block" href="<%= request.getContextPath() %>/login">Log in</a>
                </c:when>

                <c:otherwise>
                    <div style="margin-bottom:8px;">
                        Welcome,<br/>
                            ${sessionScope.username}
                    </div>
                    <a class="btn btn-outline-secondary btn-block" href="<%= request.getContextPath() %>/logout">Logout</a>
                </c:otherwise>
            </c:choose>
        </div>

        <ul class="nav flex-column">

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'dashboard' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/">
                    Dashboard
                </a>
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'matching' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/matchingIndex">
                    Matching
                </a>
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'samples' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/samples">
                    Samples
                </a>
            </li>

            <li class="nav-item mt-4 text-muted small pl-3">
                PRECISION MEDICINE<br/>
                KNOWLEDGE BASE
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'drugs' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/drugs">
                    Drugs
                </a>
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'favorites' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/favorites">
                    Favorite
                </a>
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'drug_labels' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/drugLabels">
                    Drug Labels
                </a>
            </li>

            <li class="nav-item">
                <a class="nav-link ${activeMenu == 'dosing_guideline' ? 'active' : ''}"
                   href="<%= request.getContextPath() %>/dosingGuideline">
                    Dosing Guideline
                </a>
            </li>

        </ul>
    </div>
</nav>