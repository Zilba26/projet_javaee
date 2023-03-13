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
            font-size: 20px;
        }
        input[type="submit"], select, button {
            cursor: pointer;
        }
    </style>
</head>
<body>
    <c:import url="header.jsp" />
    <section>
        <form method="post" style="display: flex; flex-direction: column">
            <label for="lastName">Nom</label>
            <input type="text" name="lastName" id="lastName" />
            <label for="firstName">Prenom</label>
            <input type="text" name="firstName" id="firstName" />
            <label for="gender">Genre</label>
            <select name="gender" id="gender">
                <option value="M">Masculin (M)</option>
                <option value="F">Feminin (F)</option>
                <option value="A">Autre</option>
            </select>
            <label for="lastPlace">Site precedent</label>
            <input type="text" name="lastPlace" id="lastPlace" />
            <label for="lastFormation">Formation precedente</label>
            <input type="text" name="lastFormation" id="lastFormation" />
            <input type="submit" value="Créer" name="classic_submit" id="classic_submit">
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
