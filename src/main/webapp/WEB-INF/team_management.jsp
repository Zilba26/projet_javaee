<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Equipes d'étudiants</title>
    <style>
        body {
            min-height: 100vh;
            margin: 0 80px 0 20px;
        }
        h2 {
            margin: 0;
            padding: 40px;
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
            flex: 1;
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
            min-height: 100vh;
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
        .teams_nb {
            align-items: center;
            gap: 10px;
            margin-left: 50px;
        }
    </style>
</head>
<body>
<div class="row">
    <form method="post" class="flex" id="remove_team_form">
        <div class="students" ondragover="event.preventDefault()" ondrop="document.querySelector('.students .studentId').value = event.dataTransfer.getData('text/plain'); document.getElementById('remove_team_form').submit()">
            <input type="hidden" class="teamId" name="teamId" value="null"/>
            <input type="hidden" class="studentId" name="studentId" />
            <h2>Etudiants sans equipe</h2>
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
    <div class="teams_container flex">
        <div class="team_title row">
            <h2 class="flex">Equipes</h2>
            <div class="teams_nb row flex">
                <form id="teamForm" method="post">
                    <label for="teams_nb_select">Nombres d'equipes</label>
                    <select name="teams_nb" id="teams_nb_select" onchange="this.form.submit()">
                        <c:forEach var="i" begin="2" end="12">
                            <c:choose>
                                <c:when test="${i == teamsNb}">
                                    <option value="${i}" selected>${i}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${i}">${i}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </form>
            </div>
        </div>
        <div class="teams">
            <c:forEach begin="1" end="${teamsNb}" var="teamNum">
                <form id="team${teamNum}" method="post">
                    <input type="hidden" class="teamId" name="teamId" value="${teamNum}"/>
                    <input type="hidden" class="studentId" name="studentId" />
                    <div class="team" ondragover="event.preventDefault()" ondrop="document.querySelector('#team${teamNum} .studentId').value = event.dataTransfer.getData('text/plain'); document.getElementById('team${teamNum}').submit()">
                        <h3>Equipe ${teamNum}</h3>
                        <div class="team-members column">
                            <c:set var="studentNb" value="0" />
                            <c:forEach begin="0" end="${students.size()}" var="i">
                                <c:if test="${students[i].hasTeam() && (students[i].getTeamId() == teamNum)}">
                                    <div class="team-member" draggable="true" ondragstart="event.dataTransfer.setData('text/plain', '${students[i].id}')">
                                            ${students[i].lastName} ${students[i].firstName}
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
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
