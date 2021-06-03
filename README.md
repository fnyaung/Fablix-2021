- # General
    - #### Team#: 11
    
    - #### Names: Hyejin Kim and Faustina Nyaung
    
    - #### Project 5 Video Demo Link:

    - #### Instruction of deployment:

    - #### Collaborations and Work Distribution:
        - Hyejin Kim worked on Task 1 and 2
        - Faustina Nyaung worked on Task 3 and 4


- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
        - cs122b-spring21-team-11/website/src/Browse.java
        
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

    - #### How read/write requests were routed to Master/Slave SQL?
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.


- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

## CS 122B Project 2 Group 11
This project displays a list of movies that contains information about a specific movie or star.

### Proj2 Demo Video URL: 
https://youtu.be/l1MuWtVrGTc

### Proj3 Demo Video URL: 
https://youtu.be/8mXbeHGKnnU

### Proj4 Demo Video URL: 
https://www.youtube.com/watch?v=grWb_YYznOU

### Proj3 Inconsistencies and Duplicates:
- Inconsistencies of MainXML, CastXML, and ActorsXML are all stored within `inconsistencies.txt`
- Duplicates within the XMLs are stored within `Duplicates.txt`

### Proj4 design and the implementation of your fuzzy search:
- Faustina Nyaung used the ed function and normalized the edit distance based on the string's length. I divided the string length by 3.5 to get the edit distance. She realized that 3.5 is a good value because that would mean that no typo is allowed until 4 character long word.



### Contributions to this project:
- Project 1:
    - Hyejin Kim and Faustina Nyaung both worked on `MovieListPage.java`, `index.html`, and `index.js`. We both thought of the sql queries together.
    - Hyejin Kim worked on `SingleMoviePage.java`, `singleMovie.html`, and `singleMovie.js`. 
    - Faustina Nyaung worked on `SingleStarPage.java`, `singleStar.html`, and `singleStar.js`.
    
- Project 2: 
    - Hyejin Kim and Faustina Nyaung both worked on MovieListPage, Confirmation, Checkout, Browse, Login.
    - Hyejin Kim worked on Search
    - Faustina Nyaung worked on Payment

- Project 3:
    - Hyejin Kim worked on task 1 to 6.
    - Faustina Nyaung worked on task 7

- Project 4:
    - Hyejin Kim and Faustina Nyaung both worked on Task 1
    - Hyejin Kim worekd on Login and Movie List Page on android
    - Faustina Nyaung worked on Extra Credit and Single Movie Page on android

### To run this project:
1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-11.git`
2. Open IntelliJ -> Import Project -> Choose the project you just cloned (The root path must contain the pom.xml!) -> Choose Import project from external model -> choose Maven -> Click on Finish -> The IntelliJ will load automatically
3. For "Root Directory", right click "cs122b-spring21-team-11" -> Mark Directory as -> sources root
4. make sure you have the `moviedb` database in all 3 java files: `MovieListPage.java`, `SingleMoviePage.java`, `SingleStar.java` in the `src` folder.
5. To run the example, follow the instructions on [canvas](https://canvas.eee.uci.edu/courses/36596/pages/intellij-idea-tomcat-configuration)

### Brief Explanation
-   User must login to access Fablix
- After user successfully logs in, user can search, browse, and add movies.
- User can purchase movie by clicking on "CheckOut"
   
- Note: If using IntelliJ, the url has to be set correctly: In `Edit configuration` -> `Tomcat` -> `Deployment` tab -> `Application context`, change the url to `/cs122b_spring21_team_11_war`