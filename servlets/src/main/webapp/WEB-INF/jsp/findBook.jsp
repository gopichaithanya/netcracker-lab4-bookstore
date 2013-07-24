<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--JSTL definition--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
    <title>Search books page</title>
    <link type="text/css" rel="stylesheet" href="/css/searchPage.css">
</head>
<body>
<h1>Search Books Service</h1>
<h3><i>Enter please search criteria</i></h3>


<c:if test="${not empty requestScope.errors}">
    <div id="errors">
        <ul>
            <c:forEach items="${requestScope.errors}" var="error">
                <li>${error}</li>
            </c:forEach>
        </ul>
    </div>
</c:if>


<div id="searchBar">
    <table>
        <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Publisher</th>
            <th>Genre</th>
            <th>Year</th>
        </tr>
        </thead>
        <tbody>
        <form method="post" action="/searchBooks">
            <tr>
                <td><input type="text" name="title" value="${requestScope.validParams.title}"></td>
                <td><input type="text" name="author" value="${requestScope.validParams.author}"></td>
                <td><input type="text" name="publisher" value="${requestScope.validParams.publisher}"></td>
                <td>
                    <select name="genre" size="1">
                        <option value="any">Any</option>
                        <c:forEach items="${requestScope.genres}" var="genre">
                            <c:choose>
                                <c:when test="${genre.genreName eq requestScope.validParams.genre}">
                                    <option value="${genre.genreName}" selected="">${genre.genreName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${genre.genreName}">${genre.genreName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </td>
                <td><input type="text" name="year" size="5"></td>
            </tr>
            <tr>
                <td><button type="submit">Find</button></td>
                <td><button type="reset">Reset</button></td>
            </tr>
        </form>

        </tbody>
    </table>
</div>


<c:if test="${(param.title != null) && (empty requestScope.searchResults)}">
    <div id="searchInfo">
        <h2>There aren't results that match the search criteria </h2>
    </div>
</c:if>


<c:if test="${param.title != null}">
    <div id="searchRequest">
        <ul>
            <li>Title: <span>${param.title}</span></li>
            <li>Author: <span>${param.author}</span></li>
            <li>Publisher: <span>${param.publisher}</span></li>
            <li>Genre: <span>${param.genre}</span></li>
            <li>Year: <span>${param.year}</span></li>
        </ul>
    </div>
</c:if>


<c:if test="${not empty searchResults}">
    <div id="searchResults">
        <table>
            <tbody>
            <h3>There are
                <span style="color: red; text-decoration: underline">${fn:length(requestScope.searchResults)}</span>
                results that match the search criteria
            </h3>
            <c:forEach items="${requestScope.searchResults}" var="book">
                <tr>
                    <td class="imgCol"><img src="/images/${book.imgRef}"/></td>
                    <td class="dataCol">
                            ${book.title}<br>
                        <a href="/showMore?bookId=${book.bookId}">ShowMore</a><br>
                            <%--<a href="/modifyBook?bookId=${book.bookId}">Modif</a>--%>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>


<div id="footer">
    <a href="/bookStore/books">Go to main page</a>
</div>

</body>
</html>