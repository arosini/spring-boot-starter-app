# Spring Boot Starter App
A Spring Boot app intended to be used primarily as a sandbox.  

An example deployment can be found here: https://spring-boot-starter-app.herokuapp.com/.
I added a limit of 100 requests per month, so my example deployment doesn't get charged by Heroku.

# Technologies
 - Java 8
 - Gradle
 - Mongo
 - Lombok
 - Git
 - Spring Boot (MVC, Data REST, Actuator, Test)
 - JUnit
 - Mockito
 - RestAssured
 - Docker (not used yet)
 
# Example Requests

- Check the API's health:  
    ```
    GET https://spring-boot-starter-app.herokuapp.com/admin/health
    ```

- View the API:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/
    ```

- View the API's profile:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/profile
    ```

- View the users API:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/users
    ```

- View the users API profile:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/profile/users
    ```

- Retrieve a single user:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/users/1
    ```

- Search users:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/users/search
    ```

- Find users by last name:
    ```
    GET https://spring-boot-starter-app.herokuapp.com/users/search/findByLastName?lastName=Summers
    ```

- Create a user with a generated ID:
    ```
    POST https://spring-boot-starter-app.herokuapp.com/users
    Content-Type: Application/json
    Accept: application/json
    Body: { "username": "Nightcrawler", "firstName": "Kurt", "lastName": "Wagner" }
    ```  
    
- Create a user with a specific ID:
    ```
    PUT https://spring-boot-starter-app.herokuapp.com/users/4
    Content-Type: Application/json
    Accept: application/json
    Body: { "username": "Nightcrawler", "firstName": "Kurt", "lastName": "Wagner" }
    ```  
    
- Update a user:
    ```
    PATCH https://spring-boot-starter-app.herokuapp.com/users/4
    Content-Type: Application/json
    Accept: application/json
    Body: { "username": "Nightcrawler", "firstName": "Kurt", "lastName": "Wagner" }
    ```

- Delete a user: ```DELETE https://spring-boot-starter-app.herokuapp.com/users/4```
 
# Setting Up (Debian/Ubuntu)
1. Install Git (http://git-scm.com/download/linux)
    ```
    $ sudo apt-get install git
    ```

2. Install Java 8 (http://ubuntuhandbook.org/index.php/2015/01/install-openjdk-8-ubuntu-14-04-12-04-lts/)
    ```
    $ sudo add-apt-repository ppa:openjdk-r/ppa
    $ sudo apt-get update
    $ sudo apt-get install openjdk-8-jdk
    ```

    If you have multiple versions of Java installed, you can can set your default version using the following command:
    ```
    $ sudo update-alternatives --config java
    ```

3. Install Mongo DB (https://docs.mongodb.org/v2.4/tutorial/install-mongodb-on-ubuntu/)
    ```
    $ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
    $ echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
    $ sudo apt-get update
    $ sudo apt-get install mongodb-10gen
    $ sudo service mongodb start
    ```

4. Install Eclipse / Spring Tools Suite

    You must use Eclipse in order to have Lombok support.

    The recommended version of Eclipse is the [Spring Tools Suite](https://spring.io/tools/sts/all), however any version is OK.

    If you are not using the Spring Tools Suite version of Eclipse, you should install the Spring Tools into Eclipse by performing the following:
     - In Eclipse, click on `Help -> Eclipse Marketplace`
     - Search for `Spring Tools Suite`
     - Click `Install` under `Spring IDE`
  
5. Install Lombok
   - Download and run the Lombok JAR from https://projectlombok.org/downloads/lombok.jar
   - Select your Eclipse installation you plan on using and click `Install / Update`

6. Clone the project
    ```
    $ git clone git@github.com:arosini/spring-boot-starter-app.git
    ```

7. Import into Eclipse
    - Open up Eclipse and select `File -> Import -> Gradle -> Gradle Project` and then click `Next`
    - Click on `Browse` and select the cloned repository location
    - Click on `Build Model` and then `Finish`
    
8. Install and setup Heroku
    ```
    $ wget -O- https://toolbelt.heroku.com/install-ubuntu.sh | sh
    $ heroku login
    $ heroku git:remote -a spring-boot-starter-app
    ```

# Building
```
$ ./gradlew clean build
 ```

This will clear the old build, create a new build, run the unit tests and generate code analysis reports. The build will fail if there are any compilation errors, failing unit tests or warnings reported by the code analysis tools.

# Running Integration Tests
```
$ ./gradlew integrationTest
```

# Running the Application
To run from inside Eclipse, right click on the project and select `Run As -> Spring Boot App`  

To run the JAR generated from the build:
```
$ java -jar build/libs/spring-boot-starter-0.0.1-SNAPSHOT.jar
```

# Deploying to Heroku
```
$ git push heroku master
```

Reference: https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku#preparing-a-spring-boot-app-for-heroku

# Available Reports
To view the reports generated during the build, see the `build/reports` directory. The following reports are available:
- Checkstyle
- FindBugs
- JaCoCo
- PMD
- Test


