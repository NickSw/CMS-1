<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>List of users.</h1>

<table border="1" cellspacing="0">
    <tr bgcolor="#6495ed">
        <th>id</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Login</th>
        <th>e-mail</th>
        <th>password</th>
        <th>role</th>
    </tr>

    <c:forEach var="user" items="${users}" >
        <tr>
            <td>${user.getId()}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.login}</td>
            <td>${user.email}</td>
            <td>${user.password}</td>
            <td>${user.role}</td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
