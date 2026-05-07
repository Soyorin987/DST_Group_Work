<!-- html -->
<!-- File: src/main/webapp/views/register.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><meta charset="utf-8"><title>Register</title></head>
<body>
<h2>Register</h2>
<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
    <div>
        <label>Username</label>
        <input name="username" required />
    </div>
    <div>
        <label>Password</label>
        <input name="password" type="password" required />
    </div>
    <div>
        <label>Authorization</label>
        <input name="authorization" />
    </div>
    <div>
        <button type="submit">Register</button>
        <a href="${pageContext.request.contextPath}/login">Back to Login</a>
    </div>
</form>
</body>
</html>
