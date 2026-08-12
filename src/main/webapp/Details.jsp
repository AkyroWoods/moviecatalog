<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <title>${movie.title}</title>
  <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<div class="container">

  <!-- Poster -->
  <img src="${movie.posterUrl}" alt="${movie.title} Poster" class="poster">

  <!-- Title -->
  <h2 style="text-align:center; margin-top:20px;">${movie.title}</h2>

  <!-- Sub Info -->
  <div class="sub-info" style="text-align:center;">
    ${movie.releaseYear} • ${movie.runtime} min • ${movie.format} • ${movie.rating}
  </div>

  <div class="grid">

    <!-- Left Column -->
    <div>
      <div class="section-title">Description</div>
      <div class="description">${movie.description}</div>

      <div class="section-title" style="margin-top:25px;">Genres</div>
      <c:forEach var="g" items="${movie.genreList}">
        <span class="badge">${g}</span>
      </c:forEach>
    </div>

    <!-- Right Column -->
    <div>
      <div class="section-title">Director</div>
      <p class="description">${movie.director}</p>

      <div class="section-title" style="margin-top:25px;">Details</div>
      <p><strong>Format:</strong> ${movie.format}</p>
      <p><strong>Release Year:</strong> ${movie.releaseYear}</p>
      <p><strong>Runtime:</strong> ${movie.runtime} minutes</p>
      <p><strong>Rating:</strong> ${movie.rating}</p>
    </div>

  </div>

  <!-- Actions -->
  <div class="actions">
    <a href="edit?movieId=${movie.movieId}" class="edit-btn">Edit</a>
    <a href="delete?movieId=${movie.movieId}" class="delete-btn">Delete</a>
    <a href="list" class="inline-red">Back to Catalog</a>
  </div>

</div>

</body>
</html>
