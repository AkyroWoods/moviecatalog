<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Select Movie</title>
    <link rel="stylesheet" href="<c:url value='/main/webapp/css/theme.css' />">
</head>

<body>

<div class="container">

    <h1>Multiple movies found for "${title}"</h1>
    <p class="sub-info" style="text-align:center; margin-bottom:20px;">
        Please select the correct one:
    </p>

    <ul class="multi-list">
        <c:forEach var="m" items="${matches}">
            <li class="multi-item">

                <div class="multi-info">
                    <span class="multi-title">${m.title}</span>

                    <c:if test="${m.releaseYear != null}">
                        <span class="multi-year">(${m.releaseYear})</span>
                    </c:if>

                    <c:if test="${not empty m.edition}">
                        <span class="multi-edition">– Edition: ${m.edition}</span>
                    </c:if>

                    <c:if test="${m.discCount != null}">
                        <span class="multi-discs">– Disc Count: ${m.discCount}</span>
                    </c:if>
                </div>

                <a href="${pageContext.request.contextPath}/search?movieId=${m.movieId}" class="view-btn">View</a>

            </li>
        </c:forEach>
    </ul>

    <a href="${pageContext.request.contextPath}/list" class="back-btn">Back to Catalog</a>

</div>

</body>
</html>