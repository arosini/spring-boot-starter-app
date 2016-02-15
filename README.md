# spring-boot-starter-application
A Spring Boot application to be used primarily as a sandbox.

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

# Building
```
./gradlew clean build
 ```

This will clear the old build, create a new build, run the unit tests and generate code analysis reports. The build will fail if there are any compilation errors, failing unit tests or warnings reported by the code analysis tools.

# Running Integration Tests
```
./gradlew integrationTest
```

# Running the Application
To run from inside Eclipse, right click on the project and select `Run As -> Spring Boot App`  

To run the JAR generated from the build:
```
java -jar build/libs/spring-boot-starter-0.0.1-SNAPSHOT.jar
```

# Available Reports
To view the reports generated during the build, see the `build/reports` directory. The following reports are available:
- Checkstyle
- FindBugs
- JaCoCo
- PMD
- Test


