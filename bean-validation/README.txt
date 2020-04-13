Given the web application is deployed in a server like Wildfly, 
the validations can be tested by for example:

$ curl -H"Content-Type: application/json" -H"Accept: application/json" localhost:8080/bean-validation/range -d '{ "min": 1, "max": 2}' -v -o -| jq


