<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Spring Boot JSP</title>
</head>
<body>
    <h1>Spring Boot with JSP View</h1>
    <p>${message}</p>
    <c:forEach var="i" begin="1" end="5">
            <p>Item number: ${i}</p>
        </c:forEach>
</body>
</html>
