<html>
<head>
  <title>Tasks</title>
</head>
<body>
  <h3>New Task</h3>
  <form action="/app/tasks" method="post">
    <label for="description">Description:</label>
    <input type="text" id="description" name="description"><br><br>
    <label for="priority">Priority:</label>
    <select name="priority" id="priority">
      <option value="HIGH">HIGH</option>
      <option value="MEDIUM">MEDIUM</option>
      <option value="LOW">LOW</option>
    </select>
     
    <input type="submit" value="Add">
  </form>

  <hr>

  <h3>Tasks List</h3>

  <table style="width:100%">
  <tr>
    <th>Description</th>
    <th>Priority</th>
  </tr>
  <#list tasks as task>
  <tr>
    <td>${task.description}</td>
    <td>${task.priority}</td>
  </tr>
  </#list>
  </table>
</body>
</html>
