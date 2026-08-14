<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add New Movie</title>
    <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<h1 class="section-title">Add New Movie</h1>

<form action="${pageContext.request.contextPath}/insert" method="post" class="movie-form">

    <input type="text" id="title" name="title" placeholder="Movie title..." required autofocus>

    <input type="number" id="releaseYear" name="releaseYear" placeholder="Release year..." min="1888" max="2100">

    <select name="format" required>
        <option value="DVD">DVD</option>
        <option value="BLU_RAY">Blu-Ray</option>
        <option value="UHD_4K">4K UHD</option>
        <option value="VHS">VHS</option>
    </select>

    <div class="form-group">
        <label for="edition">Edition (optional):</label>
        <select name="edition" id="edition" class="form-control">
            <option value="">-- None --</option>
            <option value="Widescreen">Widescreen</option>
            <option value="Full Screen">Full Screen</option>
            <option value="Director's Cut">Director's Cut</option>
            <option value="Collector's Edition">Collector's Edition</option>
           <option value="Box Set">Box Set</option>
            <option value="Anniversary">Anniversary</option>
            <option value="Steelbook">Steelbook</option>
            <option value="Extended">Extended</option>
            <option value="Unrated">Unrated</option>
            <option value="Criterion">Criterion</option>
        </select>
    </div>

    <div class="form-group">
        <label for="discCount">Disc Count (optional):</label>
        <input type="number" name="discCount" id="discCount"
               class="form-control" placeholder="e.g., 2" />
    </div>
    <button type="submit">Add Movie</button>
</form>

</body>
</html>