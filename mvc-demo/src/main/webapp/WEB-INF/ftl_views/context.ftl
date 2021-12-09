<html lang="en">
<head>
  <title>MVC Context</title>
</head>
<body>
  <h1>MVC Context</h1>
  <pre>
  Application's base path     : ${mvc.basePath}
  Local                       : ${mvc.locale}
  URI (GreetController#hello) : ${mvc.uri("GreetController#hello")} 
  URI (GreetController#hi)    : ${mvc.uri("GreetController#hi")}
  </pre> 
</body>
</html>