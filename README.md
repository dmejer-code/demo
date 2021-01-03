## Setup
To run this project:

```
mvnw clean -DskipTests package

docker-compose -f docker-compose.yml up
```

## Use cases

### 1. Auth

#### a. Login

GET http://localhost:8081/api/auth/login
```
{
    "name": "admin",
    "password": "password"
}
```

#### b. Reset password

PUT http://localhost:8081/api/auth/reset
```
{
    "oldPassword": "oldPassword",
    "newPassword": "newPassword"
}
```

#### c. Forgot password

PUT http://localhost:8081/api/auth/forgot
```
{
"userName": "admin"
}
```
```
{
"userName": "admin",
"token": "999666",
"newPassword": "newPassword"
}
```
### 2. User

#### a. Create user

POST  http://localhost:8081/api/users
```
{
"name": "tester",
"password": "password",
"roleNames": []
}
```

#### b. Update user

PUT  http://localhost:8081/api/users/{uuid}
```
{
"name": "tester",
"password": "password",
"roleNames": ["reader"]
}
```

#### c. Delete user

DELETE  http://localhost:8081/api/users/{uuid}

#### d. List users

GET  http://localhost:8081/api/users


### 3. Role

#### a. Create role

POST  http://localhost:8081/api/roles
```
{
"name": "test",
"permissions": []
}
```

#### b. Update role

PUT http://localhost:8081/api/roles
```
{
"name": "test",
"permissions": ["LIST_USER"]
}
```

#### c. Delete role

DELETE  http://localhost:8081/api/roles/{uuid}

#### d. List roles

GET  http://localhost:8081/api/roles
