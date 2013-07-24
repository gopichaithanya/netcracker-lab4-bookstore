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
    <h1><a href="bookStore">Books Store WEB site</a></h1>
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
                <c:if test="${requestScope.error}">
            <h2 style="font-family: cursive; color: red"> Please, choose existing author from the list.  </h2>
            </c:if>
            </p>
            <h2 style="font-family: cursive; color: green">Enter author parameters:</h2>
            <table>
                <tr>
                    <td>author's first name:</td>
                    <td>
                        <select name="firstName">
                            <c:forEach items="${requestScope.authorsList}" var="author">
                                <option value="${author.firstName}">
                                        ${author.firstName}
                                </option>
                            </c:forEach>
                        </select>
                    <td>
                </tr>
                <tr>
                    <td>author's last name:</td>
                    <td>
                        <select name="lastName">
                            <c:forEach items="${requestScope.authorsList}" var="author">
                                <option value="${author.lastName}">
                                        ${author.lastName}
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <br>
            <input type="submit" value="Delete Author">
            <input type="reset" value="Reset" style="margin-left: 10px">
        </FORM>
    </div>
</div>
</body>
</html>