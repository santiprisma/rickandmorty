# Rick and Morty API
Spring API Rest which uses [Rick And Morty API](https://rickandmortyapi.com/documentation/).
Basically it finds a character by name and displays relevant info such as character episodes and their first appearance in the show 

### Endpoints
`GET http://localhost:8080/search?name={character name}`

### Response Schema
```
{
    "name": "string",
    "episodes": [
        "string"
    ],
    "first_appearance": "string"
}
```

### Error Schema
```
{
    "status": int
    "description": "string"
}
```

### Errors
- 400: There are more than 1 character with this name
- 404: Character not found
- 500: Internal Server Error

### Run project
`mvn spring-boot:run`

### Run tests
`mvn test`