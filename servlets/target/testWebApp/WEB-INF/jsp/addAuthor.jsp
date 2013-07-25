


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>BookStore</title>
    <link rel="stylesheet" type="text/css" media="screen,projection" href="css/mainPage.css"/>
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

<div id="content">
    <div>
        <FORM METHOD=POST>
            <p>
                <c:if test="${requestScope.alreadyExist}">
            <h2 style="font-family: cursive; color: red"> Author with entered first and last names are already exist in the database!  </h2>
            </c:if>
            </p>
            <p>
                <c:if test="${requestScope.error}">
            <h2 style="font-family: cursive; color: red"> Please, enter authors the first and the last names.  </h2>
            </c:if>
            </p>
            <h2 style="font-family: cursive; color: green">Enter new author parameters:</h2>
            <table>
                <tr>
                    <td>author's first name:</td>
                    <td><input type="text" name="firstName" value="${requestScope.author.firstName}"></td>
                </tr>
                <tr>
                    <td>author's last name:</td>
                    <td><input type="text" name="lastName" value="${requestScope.author.lastName}"></td>
                </tr>
            </table>
            <br>
            <input type="submit" value="Add Author">
            <input type="reset" value="Reset" style="margin-left: 10px">
        </FORM>
    </div>
</div>
</body>
</html>