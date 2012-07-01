<%-- 
    Document   : login
    Created on : Aug 7, 2010, 1:48:40 PM
    Author     : wadetollefson
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Model-2 Login</title>
    </head>
    <body>
        <%
            String initial;
            if (request.getAttribute("initial") != (String)("off"))
                initial = "block";
            else
                initial = "none";

            String retry;
            if ((request.getAttribute("retry") != (String)("off"))
                    && (request.getAttribute("retry") != null))
                retry = "block";
            else
                retry = "none";

            String success;
            if ((request.getAttribute("success") != (String)("off"))
                    && (request.getAttribute("success") != null))
                success = "block";
            else
                success = "none";

            String failure;
            if ((request.getAttribute("failure") != (String)("off"))
                    && (request.getAttribute("failure") != null))
                failure = "block";
            else
                failure = "none";
        %>
        <div id="initial" style="display:<%=initial%>">
        <form action="authenticate" method="post">
            Username: <input type="text" name="username">
            Password: <input type="password" name="password">
            <input type="submit" value="Login">
        </form>
        </div>
        <div id="retry" style="display:<%=retry%>">
        <form action="authenticate" method="post">
            <i>Authentication Failed</i><br><br>
            Username: <input type="text" name="username">
            Password: <input type="password" name="password">
            <input type="submit" value="Login">
        </form>
        </div>
        <div id="success" style="display:<%=success%>">
        <b><u>Authentication Successful</u></b><br><br>
        <u>Request Parameters:</u><br>
        <%
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements())
                {
                    String parameterName = (String) parameterNames.nextElement();
                    String parameterValue = request.getParameter(parameterName);
        %>
        <%=parameterName%> has value <%=parameterValue%><br>
        <%}%><br>
        <u>Request Attributes:</u><br>
        <%
            Enumeration attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements())
                {
                    String attributeName = (String) attributeNames.nextElement();
                    String attributeValue = (String) request.getAttribute(attributeName);
        %>
        <%=attributeName%> has value <%=attributeValue%><br>
        <%}%><br>
        <u>Request Headers:</u><br>
        <%
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements())
                {
                    String headerName = (String) headerNames.nextElement();
                    String headerValue = (String) request.getHeader(headerName);
        %>
        <%=headerName%> has value <%=headerValue%><br>
        <%}%><br>
        <u>Session Values:</u><br>
        <%
        if ((success != null) && (success != "none")){
            String value = session.getAttribute("countdown").toString();
        %>
        <%="countdown"%> has value <%=value%> (meaning available login attempts)<br>
        <%}%>
        </div>
        <div id="failure" style="display:<%=failure%>">
        <u><b>Authentication Failed</b></u>
        </div>
    </body>
</html>
