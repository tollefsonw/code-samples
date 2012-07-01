<%-- 
    Document   : index
    Created on : Aug 13, 2010, 9:08:18 AM
    Author     : wadetollefson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homework 4</title>
    </head>
    <body>
        <%
            String invalid = (String)request.getAttribute("invalid");
            if (invalid == null)
                invalid = "";
            String name = (String)request.getAttribute("name");
            if (name == null)
                name = "";
            String gender = (String)request.getAttribute("gender");
            if (gender == null)
                gender = "";
            String age = (String)request.getAttribute("age");
            if (age == null)
                age = "";
            String weight = (String)request.getAttribute("weight");
            if (weight == null)
                weight = "";
        %>
        <%= invalid %>
        <form action="hounds-add" method="post">
            <table cellspacing="5" border="1">
                <tr>
                    <td>Name</td>
                    <td><input type="text" name="name" value="<%=name%>"></td>
                </tr>
                <tr>
                    <td>Gender (M or F)</td>
                    <td><input type="text" name="gender" value="<%=gender%>"></td>
                </tr>
                <tr>
                    <td>Age</td>
                    <td><input type="text" name="age" value="<%=age%>"></td>
                </tr>
                <tr>
                    <td>Weight</td>
                    <td><input type="text" name="weight" value="<%=weight%>"></td>
                </tr>
            </table><br>
            <table><tr><td>
                <input type="submit" value="submit">
            <td><tr></table>
        </form>
    </body>
</html>
