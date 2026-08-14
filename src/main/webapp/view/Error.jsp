<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <title>Error</title>
  <link rel="stylesheet" href="<c:url value='/css/theme.css' />">
</head>

<body>

<div class="container" style="text-align:center; padding:40px;">

  <h2 style="color: var(--accent-red); margin-bottom:20px;">
    Something went wrong
  </h2>

  <p class="description" style="margin-bottom:25px;">
    <c:out value="${errormessage}" />
  </p>

  <div class="actions" style="justify-content:center;">
    <a href="${pageContext.request.contextPath}/list" class="back-btn">Back to Catalog</a>
  </div>

</div>

</body>
</html>
