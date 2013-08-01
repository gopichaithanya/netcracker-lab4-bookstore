<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--JSTL definitions--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--External functions definition--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="funcs"%>

<html>
<head>
    <title>BookStore</title>
    <link rel="stylesheet" type="text/css" media="screen,projection" href="css/mainPage.css" />
</head>

<body>

<div id="header">
    <h1><a href="books">Books Store WEB site</a></h1>
</div>

<div id="navigation">
    <ul>
        <li><a href="/bookStore/books">Home</a></li>
    </ul>
</div>

<!-- Founded books that match the criteria -->
<div id="content">
    <div>
        <FORM METHOD = POST>
            <p>
                <c:if test="${requestScope.error}">
            <h2 style="font-family: cursive; color: red"> No user with such login and password. Please, check entered data.  </h2>
            </c:if>
            </p>

            <table >
                <tr>
                    <h2 style="font-family: cursive; color: green">Please, enter your nick and password:</h2>
                </tr>
                <tr>
                    <td>
                        nick:
                    </td>
                    <td>
                        <input type="text" name="nick">
                    </td>
                </tr>
                <tr>
                    <td>
                        password:
                    </td>
                    <td>
                        <input type="password" name="password">
                    <td>
                </tr>
                <tr>
                    <td>
                        status:
                    </td>
                    <td>
                        <input type="checkbox" name="status" value="admin" >Admin <br>
                    <td>
                </tr>


            </table><br>
            <input type="submit" value="Authorize"><br>
            <input type="reset" value="Reset">
        </FORM>
    </div>
</div>



</body>
</html>