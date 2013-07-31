
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>BookStore</title>
    <link rel="stylesheet" type="text/css" media="screen,projection" href="css/mainPage.css"/>
    <style>
        textarea {
            resize: none;
        }
    </style>
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
        <h2 style="font-family: cursive; color: green">Short book description:</h2>
        <table >
            <tr>
                <td>title:</td>
                <td>
                    <p>${requestScope.searchedBook.title}<p>
                </td>
            </tr>
            <tr>
                <td>Authors:</td>
                <td>
                    <c:forEach items="${requestScope.authors}" var="author">
                        ${author.firstName} ${author.lastName}
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>genre:</td>
                <td>
                    <p>${requestScope.searchedBook.genre.name}<p>
                <td>
            </tr>
            <tr>
                <td>description:</td>
                <td>
                    <textarea cols="50" rows="10" readonly>${requestScope.searchedBook.description}</textarea>
                <td>
            </tr>
            <tr>
                <td>img:</td>
                <td>
                    <img src="images/${requestScope.searchedBook.imageReference}" style="height: 200px; width: 200px"/>
                <td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>