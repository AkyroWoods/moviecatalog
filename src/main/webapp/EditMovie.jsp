<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Movie</title>
    <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<div class="container" style="max-width: 700px; margin: 40px auto;">

    <h1 class="section-title" style="text-align:center; margin-bottom:30px;">
        Edit Movie
    </h1>

    <form action="${pageContext.request.contextPath}/update" method="post" class="movie-form">

        <input type="hidden" name="movieId" value="${movie.movieId}">

        <label class="field-label">Title</label>
        <input type="text" name="title" value="${movie.title}" required>

        <label class="field-label">Release Year</label>
        <input type="number" name="releaseYear" value="${movie.releaseYear}" min="1888" max="2100" required>

        <label class="field-label">Poster Image URL</label>
        <input type="text" name="posterUrl" value="${movie.posterUrl}" placeholder="https://example.com/poster.jpg">

        <label class="field-label">Format</label>
        <select name="format" required>
            <option value="DVD" ${movie.format.name() == 'DVD' ? 'selected' : ''}>DVD</option>
            <option value="BLU_RAY" ${movie.format.name() == 'BLU_RAY' ? 'selected' : ''}>Blu-Ray</option>
            <option value="UHD_4K" ${movie.format.name() == 'UHD_4K' ? 'selected' : ''}>4K UHD</option>
            <option value="VHS" ${movie.format.name() == 'VHS' ? 'selected' : ''}>VHS</option>
        </select>

        <label class="field-label">Description</label>
        <textarea name="description" rows="5" class="description-box">${movie.description}</textarea>


        <label class="field-label">Genres</label>
        <select name="genres" multiple class="genre-select">
            <c:forEach var="g" items="${allGenres}">
                <option value="${g}" ${movie.genreList.contains(g) ? 'selected' : ''}>
                        ${g}
                </option>
            </c:forEach>
        </select>

        <button type="submit" class="add-btn" style="margin-top:25px;">
            Save Changes
        </button>

        <a href="${pageContext.request.contextPath}/list"
           class="back-btn"
           style="display:block; text-align:center; margin-top:20px;">
            Back to Catalog
        </a>

    </form>

</div>

</body>
</html>
