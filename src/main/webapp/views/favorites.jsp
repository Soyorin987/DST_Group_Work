<!-- language: html -->
<!-- File: src/main/webapp/views/favorites.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Favorite</title>
    <link href="<%=request.getContextPath()%>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/app.css" rel="stylesheet">
    <style>
        .fav-btn { background: transparent; border: none; cursor: pointer; padding: 4px 6px; font-size: 16px; color: #888; line-height: 1; }
        .fav-btn.active { color: #e74c3c; }
        .fav-btn .circle { display:inline-block; width:18px; height:18px; text-align:center; }
        .grid-table th, .grid-table td { border:1px solid #e6e6e6; padding:6px 10px; vertical-align:middle; }
        .grid-table { border-collapse:collapse; width:100%; }
        .grid-table thead th { background:#f8f9fa; font-weight:600; text-align:left; }
        .col-fav { width:56px; text-align:center; }
        .col-id { width:120px; }
        .col-name { width:220px; }
        .col-url { width:240px; max-width:240px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
    </style>
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%=request.getContextPath()%>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Precision Medicine Matching System</a>
</nav>

<div class="container-fluid" style="padding-top:56px;">
    <div class="row">
        <jsp:include page="nav.jsp">
            <jsp:param name="active" value="favorites" />
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
            <div class="d-flex justify-content-between align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2 class="h4 mb-0">Favorite</h2>
            </div>

            <div class="table-responsive">
                <table class="grid-table table-sm table-striped table-hover">
                    <thead>
                    <tr>
                        <th class="col-fav">Favorite</th>
                        <th class="col-id">id</th>
                        <th class="col-name">name</th>
                        <th class="col-objcls">obj_cls</th>
                        <th class="col-url">drug_url</th>
                        <th class="col-biomarker">biomarker</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${drugs}" var="drug">
                        <tr>
                            <td class="col-fav">
                                <button class="fav-btn ${drug.favorited ? 'active' : ''}" data-type="drug" data-id="${drug.id}" title="收藏">
                                    <span class="circle">${drug.favorited ? '●' : '○'}</span>
                                </button>
                            </td>
                            <td class="col-id"><c:out value="${drug.id}" /></td>
                            <td class="col-name"><c:out value="${drug.name}" /></td>
                            <td class="col-objcls"><c:out value="${drug.objCls}" /></td>
                            <td class="col-url">
                                <a href="${drug.drugUrl}" target="_blank" rel="noopener noreferrer"><c:out value="${drug.drugUrl}" /></a>
                            </td>
                            <td class="col-biomarker"><c:out value="${drug.biomarker}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</div>

<script>
    $(function(){
        $(document).on('click', '.fav-btn', function(e){
            e.preventDefault();
            var btn = $(this);
            var type = btn.data('type');
            var id = btn.data('id');
            var isActive = btn.hasClass('active');
            var url = isActive ? '<%=request.getContextPath()%>/favorites/remove' : '<%=request.getContextPath()%>/favorites/add';
            $.post(url, { resourceType: type, resourceId: id })
                .done(function(res){
                    if (res && res.ok) {
                        // 取消收藏后从列表中移除（在 favorites 页面更自然）
                        if (isActive) {
                            btn.closest('tr').remove();
                        } else {
                            btn.toggleClass('active');
                            btn.find('.circle').text(btn.hasClass('active') ? '●' : '○');
                        }
                    } else {
                        alert('操作失败，请先登录或重试');
                        if (res && res.msg === 'not logged in') {
                            window.location = '<%=request.getContextPath()%>/signin';
                        }
                    }
                })
                .fail(function(){ alert('网络错误，请稍后重试'); });
        });
    });
</script>
</body>
</html>
