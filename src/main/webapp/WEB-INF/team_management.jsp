<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Equipes d'étudiants</title>
    <style>
        body {
            margin: 0;
            min-height: 100vh;
        }
        section {
            min-height: calc(100vh - 100px);
            margin: 0 80px 0 20px;
        }
        h2 {
            margin: 0;
            padding: 40px;
        }
        button {
            cursor: pointer;
            width: max-content;
            height: max-content;
        }
        .row {
            display: flex;
            flex-direction: row;
        }
        .column {
            display: flex;
            flex-direction: column;
        }
        .flex {
            flex: 2;
        }
        ul {
            gap: 10px;
        }
        li {
            list-style: none;
            padding: 10px;
            border: 1px solid black;
            cursor: pointer;
            width: max-content;
        }
        .students {
            height: 100%;
        }
        .teams {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px;
        }
        .team {
            margin-bottom: 20px;
            padding: 10px;
            border: 1px solid black;
        }
        .team-member {
            margin-bottom: 10px;
        }
        .align {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .change_team_name_form input {
            border: none;
            font-size: 1.17em;
            font-weight: bold;
            padding: 4px;
        }
        .change_team_name_form {
            margin: 10px 0;
            gap: 5px;
        }
        .teams_container {
            flex: 3;
            margin-bottom: 50px;
        }
    </style>
</head>
<body>
    <c:import url="header.jsp" />
    <section class="row">
        <form method="post" class="flex" id="remove_team_form">
            <div class="students" ondragover="event.preventDefault()" ondrop="document.querySelector('.students .studentId').value = event.dataTransfer.getData('text/plain'); document.getElementById('remove_team_form').submit()">
                <input type="hidden" class="teamId" name="teamId" value="null"/>
                <input type="hidden" class="studentId" name="studentId" />
                <div class="row" style="align-items: center">
                    <h2>Etudiants sans equipe</h2>
                    <button type="submit" name="auto_compo">Composition automatique</button>
                </div>
                <ul class="column">
                    <c:forEach items="${students}" var="student">
                        <c:if test="${!student.hasTeam()}">
                            <li draggable="true" ondragstart="event.dataTransfer.setData('text/plain', '${student.id}')">
                                    ${student.lastName} ${student.firstName}
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </form>
        <div class="teams_container">
            <div class="row">
                <h2 class="flex">Equipes</h2>
                <form method="post" id="add_student_form" class="align">
                    <button type="submit" name="export_csv">Exporter</button>
                </form>
            </div>
            <div class="teams">
                <c:forEach begin="0" end="${teams.size()}" var="i">
                    <c:if test="${i == teams.size()}">
                        <form method="post" id="add_team_form" class="align">
                            <button type="submit" name="add_team" value="true">Ajouter une equipe</button>
                        </form>
                    </c:if>
                    <c:if test="${i != teams.size()}">
                        <c:set var="teamId" value="${teams.get(i).id}" />
                        <form id="team${teamId}" method="post">
                            <input type="hidden" class="teamId" name="teamId" value="${teamId}"/>
                            <input type="hidden" class="studentId" name="studentId" />
                            <div class="team" ondragover="event.preventDefault()" ondrop="document.querySelector('#team${teamId} .studentId').value = event.dataTransfer.getData('text/plain'); document.getElementById('team${teamId}').submit()">
                                <div class="change_team_name_form row">
                                    <input type="text" name="teamName" class="title" value="${teams.get(i).name}"/>
                                    <button type="submit" name="change_team_name">Modifier</button>
                                    <button type="submit" name="delete_team">Supprimer</button>
                                </div>

                                <div class="team-members column">
                                    <c:set var="studentNb" value="0" />
                                    <c:forEach begin="0" end="${students.size()}" var="j">
                                        <c:if test="${students[j].hasTeam() && (students[j].getTeamId() == teamId)}">
                                            <div class="team-member" draggable="true" ondragstart="event.dataTransfer.setData('text/plain', '${students[j].id}')">
                                                    ${students[j].lastName} ${students[j].firstName}
                                            </div>
                                            <c:set var="studentNb" value="${studentNb + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${studentNb == 0}">
                                        <p>Aucun etudiant</p>
                                    </c:if>
                                </div>
                            </div>
                        </form>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>
</body>
</html>
