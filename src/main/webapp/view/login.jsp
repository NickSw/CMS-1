<%--
  Created by IntelliJ IDEA.
  User: Sergey
  Date: 07.06.2015
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html >

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>32Seconds2Demo</title>
  <style type="text/css">
    body {
      background:#eeeeef;
    }
    #main {
      width:260px;
      margin:100px
      auto 0 auto;
      padding:10px;
      background:#fff;
      box-shadow: 0 0 10px rgba(0,0,0,0.5);}

    .error {
      color:#F00;
    }

  </style>
</head>

<body>

<div id="main">
  <div  class="error">
    ${error}
  </div>
  <br>

  <form action="/login" method="POST">
    Username<br>
    <input type="text" name="login" maxlength="24" size="30" /><br />
    <br />
    Password<br />
    <input type="password" name="password" maxlength="24" size="30" /><br />
    <br />
    <input type="submit" value=" Log in ">
  </form>
</div>

</body>
</html>
