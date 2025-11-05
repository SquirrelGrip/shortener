# Shortener - Shortening URLs since 2025
A service that will take your URL and give you a shortened unique URL that can be passed around.

## Instructions

### To build the code

In a command line, enter
```shell
./gradlew build
```

### To run the application

In a command line, enter
```shell
./gradlew bootRun
```

Open http://localhost:8080 in a browser.

### To access the OpenDoc API

Open http://localhost:8080/swagger-ui/index.html

## Scenarios
There are 2 scenarios for generating a shortened URL for any given URL:
1. Generate a new shortened URL for every URL request
2. Generate a new unique shortened URL for every URL request

The first scenario is easiest, but would result in database growth as each URL would result in a new shortened URL, not ideal in the long term.
The second comes with an issue around concurrency, in that a there exists a chance that 2 URLs could return different shortened URLs, if the calls were made at the same time. This is because the checking and creating of the shortened URLs takes some time.

### Concurrency Issue
To resolve the issue, the use of a lock on the input URL would be required when a request for a URL is made. However, this would result in a synchronized block which creates a performance bottle neck on the syste,.
In order to get around this, the system should check for an existing shortened URL first. If the shortened URL exists, return the shortened URL. However, if it doesn't exist, lock the URL while we create the shortened URL and save it to the database. 
While it is locked, any subsequent requests for the same URL will block until the first request has completed. It will then request the shortened URL by looking it up in the database.
By doing this we can prevent unnecessary synchronized locking and get better performance to those requests that need it.

### Solution
1. NonUniqueShorteningService - Provides a solution to the first scenario, in that it will generate a shortened URL for every input URL.
2. SimpleShorteningService - Provides a naive solution to the second scenario, but does not solve the concurrency issue, which can give slightly better performance, but can has a chance of producing non unique shortened URLs.
3. ConcurrentShorteningService - Provides a robust solution to the second scenario, in that it guarantees uniqueness of shortened URL.
4. ConcurrentShorteningService - Provides a robust solution to the second scenario, in that it guarantees uniqueness of shortened URL with better performance for existing shortened URLs.

## Considerations
- Using JPA was consider the easiest choice. While JPA comes with an opinionated outer join implementation, it is easy to implement and therefore was chosen. Very little configuration and no SQL coding involved.
- Thymeleaf was selected for UI rendering. While I am not a fan of server side rendering, and have in the past used Velocity, Freemarker and Mustache templates, I chose Thymeleaf mainly to give it a go. I like that it is quite easy to add in and aligns with the HTML standards (i.e. It keeps the HTML looking like HTML)
- I chose H2 database for the In-Memory store. It is easy to implement, but would never use it in production.
- I chose 6 character code as it provides around 62^6 (56,800,235,584) possible codes. This can be adjusted in the application properties if more is needed (I doubt it)

## TODO
- The solution does not include a fancy front page.
- The solution does not use kotlin coroutines, as it could increase code complexity, with little performance gains.
- The entity, Shortening, was bare minimum to suit the requirements. In real world implementation, it would be wise to have more attributes in order to specify owner of shortening (would require authentication, etc.) and auditing requirements.