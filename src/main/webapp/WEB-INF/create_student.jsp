<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Creation d'etudiants</title>
    <meta charset="utf-8">
    <style>
        body {
            height: 100vh;
            margin: 0;
        }
        section {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            height: calc(100vh - 100px);
        }
        form {
            width: 50%;
        }
        input, select {
            height: 40px;
            margin-bottom: 10px;
            font-size: 20px;
        }
        label {
            cursor: text;
            font-size: 20px;
        }
        input[type="submit"], select, button {
            cursor: pointer;
        }
        .error {
            color: red;
        }
    </style>
</head>
<body>
<c:import url="header.jsp" />
<section>
    <form method="post" style="display: flex; flex-direction: column">
        <label for="lastName">Nom</label>
        <input type="text" name="lastName" id="lastName" <c:if test="${!empty student}">value="${student.lastName}" </c:if>/>
        <label for="firstName">Prénom</label>
        <input type="text" name="firstName" id="firstName" <c:if test="${!empty student}">value="${student.firstName}" </c:if>/>
        <label for="gender">Genre</label>
        <select name="gender" id="gender">
            <option value="M" <c:if test="${!empty student && student.gender eq 'M'}">selected</c:if>>Masculin (M)</option>
            <option value="F" <c:if test="${!empty student && student.gender eq 'F'}">selected</c:if>>Féminin (F)</option>
            <option value="A" <c:if test="${!empty student && student.gender ne 'A'}">selected</c:if>>Autre</option>
        </select>
        <label for="lastPlace">Site précédent</label>
        <input type="text" name="lastPlace" id="lastPlace" <c:if test="${!empty student}">value="${student.lastPlace}" </c:if>/>
        <label for="lastFormation">Formation précédente</label>
        <input type="text" name="lastFormation" id="lastFormation" <c:if test="${!empty student}">value="${student.lastFormation}" </c:if>/>
        <input type="submit" value="Créer" name="classic_submit" id="classic_submit">
        <c:if test="${!empty error}">
            <p class="error">${error}</p>
        </c:if>
    </form>
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="fichier" id="fichier" />
        <input type="submit" value="Importez" name="csv_submit" id="csv_submit">
    </form>
    <c:if test="${not empty studentImported}">
        <label for="fichier">${studentImported} étudiants importés</label>
    </c:if>
</section>
</body>
</html>
