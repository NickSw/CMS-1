<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html >

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>32Seconds2Demo</title>
  <link href="styles/style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div id="container">
  <div id="header">
    <div id="logo"></div>
  </div>

  <%@include file="view_frag/top_nav.jspf" %>

  <div class="content">
    <%@ include file="view_frag/post_full_block.jspf" %>
  </div>

  <div id="bars">
    <!--left side bar-->
    <div id="left_side_bar">
      <%@ include file="view_frag/recent_block.jspf" %>
      <%@ include file="view_frag/popular_block.jspf" %>
    </div>

    <!--right side bar-->
    <div id="right_side_bar">
      <%@ include file="view_frag/search_block.jspf" %>
      <%@include file="view_frag/tags_block.jspf" %>
      <%@ include file="view_frag/archive_block.jspf" %>
    </div>
  </div>

  <div id="footer"> &copy; 2015 All rights reserved. </div>
</div>
</body>
</html>

