<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Search Result</title>
    <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<h1>Search Result</h1>

<c:if test="${not empty movie}">
    <div class="container">
        <h2>${movie.title}</h2>

        <img src="${movie.posterUrl}" alt="${movie.title} Poster" class="poster">
        <p><strong>Year:</strong> ${movie.releaseYear}</p>
        <p><strong>Format:</strong> ${movie.format}</p>

        <a href="${pageContext.request.contextPath}/details?movieId=${movie.movieId}"
           class="add-btn">Go to Full Details</a>

        <a href="${pageContext.request.contextPath}/list" class="back-btn">Back to Catalog</a>
    </div>
</c:if>

<c:if test="${empty movie}">
    <h2>No movie found.</h2>
    <a href="${pageContext.request.contextPath}/list" class="back-btn">Back</a>
</c:if>

</body>
</html>
