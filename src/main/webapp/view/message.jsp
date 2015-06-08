<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>32Seconds2Demo</title>
  <link href="/styles/styleadmin.css" rel="stylesheet" type="text/css" />
</head>

<body>

<div id="container">

  <div id="header">
    <div id="logo"></div>
  </div>

  <%@ include file="view_frag/top_nav.jspf" %>

  <!--left side bar-->
  <div id="left_side_bar">
    <%@ include file="view_frag/left_nav.jspf" %>
  </div>

  <div class="content">
    <%@ include file="view_frag/message_block.jspf" %>
  </div>


  <div id="footer"> &copy; 2015 All rights reserved. </div>

</div>

</body>
</html>
