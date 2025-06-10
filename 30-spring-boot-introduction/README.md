Spring Boot - An Introduction
=

Nothing here to see as you are going to generate it all yourselves. 

1. In your browser of choice goto [Spring Initialzr](https://start.spring.io)
   * Here you can quickly bootstrap an application, which is what we are going to do.
2. Project, select what you want to use for the build system
3. Language (we are sticking with Java here)
4. Spring Boot version select the most recent non SNAPSHOT version
2. For the group enter `biz.deinum` (or feel free to think of something else)
3. For the artifact use `library`
4. For the name use **Library**
5. The description you can make up yourself :)
6. The package name is auto-generated so you shouldnt need to touch it, but you could if you like
7. Packaging `jar`
8. Select the Java version you want to use (either will do)
9. Click `Add Dependencies`
   * Observe the list as there is plenty to choose from
   * Select `Spring Boot Devtools`, `Spring Boot Actuator`
   * After selecting the dependencies you can view what it will generate by pressing the **Explore** button
10. Press **Generate**
    * This will download a zip file which you can unpack and import into your IDE
    * Examine the imported project and notice it already generated some classes and even a test.
    * In the `pom.xml` (or `build.gradle`) there will be the dependencies including `spring-boot-starter-test` (not having this is not an option :) 
    * The `HELP.md` contains links to documentation on how to use the various added dependencies and get you started. 
11. Run the generated application (you can also try to run the test if you like)
12. Open the generated application and add the following:
```java
@Bean
public ApplicationRunner hello() {
    return (args) -> System.out.println("Hello ING from Spring Boot"); 
}
```
13. Re-run the application and it should print something to the console.
14. Run `./mvnw package` or `./gradlew build` and wait for the build to complete. 
    * there is now an executable jar in the `target` or `build` directory
    * run it with `java -jar <name-of-jar>` and it should give the same output as in the IDE
15. If you have docker installed you can try running it as container as well (building will take a while!)
    * Run `./mvnw spring-boot:build-image` or `./gradlew bootBuildImage`
    * Wait for the build to have completed
    * Run the container with `docker run docker.io/<your-container-name>`
    * It should now run (and exit) in Docker