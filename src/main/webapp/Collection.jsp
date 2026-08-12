<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Movie Catalog</title>

    <!-- Correct CSS link -->
    <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<h1 style="text-align: center; margin-top: 20px;">Movie Catalog</h1>

<div class="grid-catalog">
    <c:forEach var="m" items="${movies}">
        <div class="card">

            <img src="${m.posterUrl}" alt="${m.title} Poster" class="poster">

            <h3>
                <a href="${pageContext.request.contextPath}/details?movieId=${m.movieId}"
                   style="color: var(--text); text-decoration:none;">
                        ${m.title}
                </a>
            </h3>

            <p class="sub-info">
                Year: ${m.releaseYear} | Format: ${m.format}</p>

            <div class="actions">
                <a href="${pageContext.request.contextPath}/edit?movieId=${m.movieId}" class="edit-btn">Edit</a>
                <a href="${pageContext.request.contextPath}/delete?movieId=${m.movieId}" class="delete-btn">Delete</a>
            </div>

        </div>
    </c:forEach>

    <div class="card"style="display: flex; justify-content: center; align-items: center;">
        <a href="${pageContext.request.contextPath}/new" class="add-btn">Add Movie</a>
    </div>
</div>

</body>
</html>
