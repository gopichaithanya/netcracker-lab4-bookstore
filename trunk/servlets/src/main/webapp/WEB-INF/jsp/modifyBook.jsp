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
                <c:if test="${requestScope.error}">
            <h2 style="font-family: cursive; color: red"> Please, check entered data.  </h2>
            </c:if>
            </p>
            <h2 style="font-family: cursive; color: green">Modify book parameters:</h2>
            <table>
                <tr>
                    <td>title:</td>
                    <td><input type="text" name="title" ></td>
                </tr>

                <%--List of all Publishers--%>
                <tr>
                    <td> Publisher: </td>
                    <td>
                        <select name="publisherId">
                            <c:forEach items="${requestScope.publishersList}" var="publisher">
                                <option value="${publisher.id}">
                                        ${publisher.info} ${publisher.addInfo}
                                </option>
                            </c:forEach>
                        </select>
                        <a href="/bookStore/addPublisher"> Add new Publisher </a>
                    <td>
                </tr>



                <%--List of all Genres--%>
                <tr>
                    <td>Genre: </td>
                    <td>
                        <select name="genreId">
                            <c:forEach items="${requestScope.genresList}" var="genre">
                                <option value="${genre.id}">
                                        ${genre.info}
                                </option>
                            </c:forEach>
                        </select>
                        <a href="/bookStore/addGenre"> Add new Genre </a>
                    <td>
                </tr>

                <%--List of all authors--%>
                <tr>
                    <td>author's  name:</td>
                    <td>
                        <select name="authorId">
                            <c:forEach items="${requestScope.authorsList}" var="author">
                                <option value="${author.id}">
                                        ${author.info} ${author.addInfo}
                                </option>
                            </c:forEach>
                        </select>
                        <a href="/bookStore/addAuthor">  Add new Author </a>
                    </td>

                </tr>


                <td>description:</td>
                <td><textarea type="text" rows="10" cols="50" name="descript"></textarea></td>
                </tr>
                <tr>
                    <td>image:</td>
                    <td><input type="text" name="imgRef"></td>
                </tr>
                <tr>
                    <td>year:</td>
                    <td><input type="number" name="year" ></td>
                </tr>
            </table>
            <br>
            <input type="submit" value="Add Book">
            <input type="reset" value="Reset" style="margin-left: 10px">
        </FORM>
    </div>
</div>
</body>
</html>