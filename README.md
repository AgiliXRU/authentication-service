# Authentication Service

Kata for training

## Requires
 - Java 8+
 - Docker Compose

## Branches
### SUT - docker component
- **v1-manual-e2e** - Manual E2E
- **v2-manual-n-adapter**  - Manual E2E + Wiremock
- **v3-bad-cucumber**  - Automated E2E (component), IT language + Adapter  + Wiremock
- **v4-good-cucumber**  - Automated E2E (component), user language  + Wiremock

### SUT - java classes
- **v5-integration**  - Integration
- **master** - +Unit tests

## Run application
```
gradlew clean build composeUp
```

## 3. Access 
 - [Swagger UI - http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
 - [Wiremock - http://localhost:8081/__admin/webapp/mappings](http://localhost:8081/__admin/webapp/mappings)