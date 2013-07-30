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
            <h2 style="font-family: cursive; color: green">Select author from the list:</h2>
            <table>
                <tr>
                    <td>author's  name:</td>
                    <td>
                        <select name="Id">
                            <c:forEach items="${requestScope.authorsList}" var="author">
                                <option value="${author.id}">
                                        ${author.info} ${author.addInfo}
                                </option>
                            </c:forEach>
                        </select>
                    <td>
                </tr>

            </table>
            <br>
            <input type="submit" value="Delete Author">
        </FORM>
    </div>
</div>
</body>
</html>