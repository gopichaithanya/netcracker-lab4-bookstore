<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%--JSTL definitions--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>books</title>
    <link rel="stylesheet" type="text/css" media="screen,projection" href="css/mainPage.css" />
</head>

<body>


    <div id="header">
        <h1><a href="">Books Store WEB site</a></h1>
    </div>

    <div id="navigation">
        <ul>
            <li><a href="">Home</a></li>
            <li><a href="find">Find books</a></li>
            <li><a href="addBook">Add book</a></li>
            <li><a href="addAuthor">Add author</a></li>
            <li><a href="deleteAuthor">Delete author</a></li>
            <li><a href="auth">Authorization</a></li>
            <li><a href="">About</a></li>
        </ul>
    </div>


    <%--Declate variables genreId that represent the chosen genre--%>
    <c:choose>
        <c:when test="${not empty selGenre.genreId}">
            <c:set var="genreId" value="${selGenre.genreId}" scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="genreId" value="-1" scope="page"/>
        </c:otherwise>
    </c:choose>

    <%--Declate variables authorId that represent the chosen author--%>
    <c:choose>
        <c:when test="${not empty selAuthor.authorId}">
            <c:set var="authorId" value="${selAuthor.authorId}" scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="authorId" value="-1" scope="page"/>
        </c:otherwise>
    </c:choose>


    <!-- List of authors -->
    <div id="left">
        <h3>Authors</h3>
        <ul>
            <li><a href="books?author=-1&genre=${genreId}">Any</a></li>
            <c:forEach items="${requestScope.authorsList}" var="author">
                <c:url value="books" var="authorUrl" scope="page">
                    <c:param name="author" value="${author.id}"/>
                    <c:param name="genre" value="${genreId}"/>
                </c:url>
                <li><a href="${authorUrl}">${author.info}</a></li>
            </c:forEach>
        </ul>
    </div>


    <!-- List of genres -->
    <div id="right">
        <h3>Genres</h3>
        <ul>
            <li><a href="books?author=${authorId}&genre=-1">Any</a></li>
            <c:forEach items="${requestScope.genresList}" var="genre">
                <c:url value="books" var="genreUrl" scope="page">
                    <c:param name="author" value="${authorId}"/>
                    <c:param name="genre" value="${genre.id}"/>
                </c:url>
                <li><a href="${genreUrl}">${genre.info}</a></li>
            </c:forEach>
        </ul>
    </div>

    <!-- Founded books that match the criteria -->
    <div id="content">
        <%--Generate information about selected author and genre--%>
        <h3>
            <%--Show selected author--%>
            <span style="text-decoration: underline">Author</span>
            <span style="font-family: cursive;">
                <c:out value="${selAuthor.authorFirstName} ${selAuthor.authorLastName}"/>
            </span> &middot;

            <%--Show selected genre--%>
            <span style="text-decoration: underline">Genre</span>
            <span style="font-family: cursive;">
                <c:out value="${selGenre.genreName}"/>
            </span>
        </h3>

        <!--This table contains information about founded books-->
        <table>
            <thead></thead>
            <tbody>
                <c:forEach items="${requestScope.searchResult}"
                           var="book" begin="${requestScope.lowBookIndex}" end="${requestScope.topBookIndex}">
                    <tr>
                        <td class="imgCol"><img src="images/${book.addInfo}"/></td>
                        <td class="dataCol">
                            ${book.info}<br>
                            <a href="showMore?bookId=${book.id}">ShowMore</a><br>
                            <c:if test="${not empty sessionScope.authorize}">
                                <a href="modify?bookId=${book.id}">Modificate</a><br>
                                <a href="delete?bookId=${book.id}">Delete</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Page scrolling facility -->
    <div id="footer">
        <table>
            <tr>
                <%--Add page list to the page--%>
                <c:forEach var="pageNum" begin="1" end="${requestScope.pagesAmount}">
                    <td>
                        <c:url var="pageRef" value="/books" scope="page">
                            <c:param name="author" value="${authorId}"/>
                            <c:param name="genre" value="${genreId}"/>
                            <c:param name="page" value="${pageNum}"/>
                        </c:url>
                        <a href="${pageRef}">${pageNum}</a>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </div>

</body>
</html>