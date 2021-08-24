#Sample Spring Boot and JWT 


## Role model

Only user with the permission "READ_PROFILE" can make the GET request for "/profile"
Only user with the permission "READ_ACCOUNTS" can make the GET request for "/accounts"

See file "resources/authz_policy.csv"

## Custom function in model definition 

Implemented the custom function "contains" which returns true if this list contains the specified element.

`contains(String elem, List<String> coll)`

See class ContainsFunc

## JWT samples

### User has only the permission "READ_PROFILE" 
{
"sub": "joe",
"perms": ["READ_PROFILE"]
}

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJqb2UiLCJwZXJtcyI6WyJSRUFEX1BST0ZJTEUiXX0.
XMT67MAdrwASTfE2_NOgsBx3N46i1USr00SqZRK3uCA

### User has the permissions: "READ_PROFILE" and "READ_ACCOUNTS" 
{
"sub": "alex",
"perms": ["READ_PROFILE", "READ_ACCOUNTS"]
}

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhbGV4IiwicGVybXMiOlsiUkVBRF9QUk9GSUxFIiwiUkVBRF9BQ0NPVU5UUyJdfQ.
HJF4zlkLsP3VGEepIjlQ96SQ-LmtopOCHaraxPF0T0I


## Examples of requests

### User with "READ_PROFILE"

>curl --request GET \
--url http://localhost:8080/profile \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2UiLCJwZXJtcyI6WyJSRUFEX1BST0ZJTEUiXX0.XMT67MAdrwASTfE2_NOgsBx3N46i1USr00SqZRK3uCA'

Response HTTP-code is 200

>curl --request GET \
--url http://localhost:8080/accounts \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2UiLCJwZXJtcyI6WyJSRUFEX1BST0ZJTEUiXX0.XMT67MAdrwASTfE2_NOgsBx3N46i1USr00SqZRK3uCA'

Response HTTP-code is 403

### User with "READ_PROFILE" and "READ_ACCOUNTS"

>curl --request GET \
--url http://localhost:8080/profile \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGV4IiwicGVybXMiOlsiUkVBRF9QUk9GSUxFIiwiUkVBRF9BQ0NPVU5UUyJdfQ.HJF4zlkLsP3VGEepIjlQ96SQ-LmtopOCHaraxPF0T0I'

Response HTTP-code is 200

>curl --request GET \
--url http://localhost:8080/accounts \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGV4IiwicGVybXMiOlsiUkVBRF9QUk9GSUxFIiwiUkVBRF9BQ0NPVU5UUyJdfQ.HJF4zlkLsP3VGEepIjlQ96SQ-LmtopOCHaraxPF0T0I'

Response HTTP-code is 200
