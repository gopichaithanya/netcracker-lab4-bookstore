


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
            <h2 style="font-family: cursive; color: red"> Genre with entered name already exist in the database!  </h2>
            </c:if>
            </p>
            <p>
                <c:if test="${requestScope.error}">
            <h2 style="font-family: cursive; color: red"> Please, enter genre name.  </h2>
            </c:if>
            </p>
            <h2 style="font-family: cursive; color: green">Create new genre:</h2>
            <table>
                <tr>
                    <td>Genre of book:</td>
                    <td><input type="text" name="name" value="${requestScope.genre.genreName}"></td>
                </tr>

            </table>
            <br>
            <input type="submit" value="Add Genre">
            <input type="reset" value="Reset" style="margin-left: 10px">
        </FORM>
    </div>
</div>
</body>
</html>