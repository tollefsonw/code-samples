<%-- 
    Document   : listHounds.jsp
    Created on : Aug 13, 2010, 8:53:50 PM
    Author     : wadetollefson
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="se452" uri="/WEB-INF/tlds/se452.tld" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homework 4</title>
    </head>
    <body>
        <c:forEach var="hound" items="${applicationScope.houndList}">
            <se452:displayHound />
        </c:forEach>
    </body>
</html>
