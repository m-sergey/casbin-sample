#Sample Spring Boot and Basic Auth

## Role model

All user can make the GET request for "/profile"
Only alex can make the GET request for "/accounts"

The password fo all users is "password" :)

See file "resources/authz_policy.csv"

## Examples of requests

### Anonymous user

>curl http://localhost:8080/profile

Response HTTP-code is 403

### Any user

>curl --user "joe:password" http://localhost:8080/profile

Response HTTP-code is 200

>curl --user "joe:password" http://localhost:8080/accounts

Response HTTP-code is 403

### User "alex"

>curl --user "alex:password" http://localhost:8080/profile

Response HTTP-code is 200

>curl --user "alex:password" http://localhost:8080/accounts

Response HTTP-code is 200
