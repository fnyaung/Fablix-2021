- # General
    - #### Team#: 11
    
    - #### Names: Hyejin Kim and Faustina Nyaung
    
    - #### Project 5 Video Demo Link:
   https://www.youtube.com/watch?v=c0XcQkXqnVw

    - #### Instruction of deployment:
        1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-11.git`
        2. Open IntelliJ -> Import Project -> Choose the project you just cloned (The root path must contain the pom.xml!) -> Choose Import project from external model -> choose Maven -> Click on Finish -> The IntelliJ will load automatically
        3. For "Root Directory", right click "cs122b-spring21-team-11" -> Mark Directory as -> sources root
        4. make sure you have the `moviedb` database in all 3 java files: `MovieListPage.java`, `SingleMoviePage.java`, `SingleStar.java` in the `src` folder.
        5. To run the example, follow the instructions on [canvas](https://canvas.eee.uci.edu/courses/36596/pages/intellij-idea-tomcat-configuration)

    - #### Collaborations and Work Distribution:
        - Hyejin Kim worked on Task 1 and 4
        - Faustina Nyaung worked on Task 2 and 3 and 4

- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
        - cs122b-spring21-team-11/website/src/Browse.java
        - cs122b-spring21-team-11/website/src/AddMovie.java
        - cs122b-spring21-team-11/website/src/AddStar.java
        - cs122b-spring21-team-11/website/src/LoginFilter.java
        - cs122b-spring21-team-11/website/src/LoginPage.java
        - cs122b-spring21-team-11/website/src/Metadata.java
        - cs122b-spring21-team-11/website/src/MovieListPage.java
        - cs122b-spring21-team-11/website/src/Payment.java
        - cs122b-spring21-team-11/website/src/SingleMoviePage.java
        - cs122b-spring21-team-11/website/src/SingleStarPage.java

    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
        - The servlets gets a cached connection from the JDBC connection pool. If all connections are being used, then new connections are created. Otherwise, pre-exisiting connections would be used. 
        - When the servlets are done using the connection, the connection would go back into the connection pool to be used again.
        - As a result of reusing pre-existing connections, the time it takes to create and free connections are reduced.
        - we defined our connection pooling in `context.xml` and used prepared statement for user queries. 

    - #### Explain how Connection Pooling works with two backend SQL.
        - Used two connection pools, masterdb and moviedb.
        - If the servlet is writing/reading to the db, it would get a cached connection from masterdb, which connects to the master instance sql database. 
        - If the servlet is only reading to the db, it would get a cached connection from the localhost, which connects to the slave instance sql database. 
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
        - cs122b-spring21-team-11/website/src/AddMovie.java
        - cs122b-spring21-team-11/website/src/AddStar.java
        - cs122b-spring21-team-11/website/src/Payment.java
        - cs122b-spring21-team-11/website/WebContent/META-INF/context.xml


    - #### How read/write requests were routed to Master/Slave SQL?
       - Fablix uses masterdb for write/read requests. However, if the servlet is only reading, then the servlet would get connection from the localhost connection pool (either using master or slave). 
    
- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
        - The log_processing is located at the base directory. To use it, you have to change the first line of the file to your file path. And type python3 log_processing.py or just simple execute it. 
        - Note : The log files for thisthe projec5 were saved in the porj5/logfiles.
        ```
        path_file = "/home/ubuntu/filename.txt"
        ```

- # JMeter TS/TJ Time Measurement Report
| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | porj5/proj5_image/1_single_case1.png  | 379                        | 293.143073                          | 293.031416             | This case 1 was done with the connection pooling. Our group found that there is not much difference between the TS and TJ, the difference is 0.111657.            |
| Case 2: HTTP/10 threads                        | porj5/proj5_image/1_single_case2.png   | 799                        | 712.518939                         |  712.386364            | This case 2 was done with the connection pooling. Our group noticed that our avery servelt time significantly increased from the 1 thread, (about 500ns), we found that the average has also incrased.           |
| Case 3: HTTPS/10 threads                       | porj5/proj5_image/1_single_case3.png   | 824                         | 732.813636                        | 732.654167             | This case 3 was done with the connection pooling. Our group noticed that the result(TS and TJ) have incrase a little bit from the http. This is a bit interesting as we thoguht https will be faster.           |
| Case 4: HTTP/10 threads/No connection pooling  | porj5/proj5_image/1_single_case4.png   | 842                         | 734.342045                      | 734.147727          | This case 4 was done without the connection pooling. Our group noticed that the result(TS and TJ) are similar to with connection pooling. The throguh output is bit higher than the rest of the cases.
           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | porj5/proj5_image/2_single_case1.png   | 397                         | 317.257759                     | 316.950038                | This case 1 was done with the load balancer. Our group found that there is little bit of a difference compare to the single instance. Also there is bit more difference between TS and TJ compare to the connection pooling.           |
| Case 2: HTTP/10 threads                        | porj5/proj5_image/2_single_case2.png   | 493                         | 406.833523                     | 406.528696                | This case 2 was done with the load balancer. Our group found that there much difference in the TS and TJ time compare to the single instance. We could see that the load balancing as well as the connection pooling is working well as it fastened the query time. 
           |
| Case 3: HTTP/10 threads/No connection pooling  | porj5/proj5_image/2_single_case3.png   | 508                         | 395.379167                     | 395.114394                        | This case 3 was done with the load balancer and without the connection pooling. Our group noticed that the result(TS and TJ) are similar to with connection pooling. The average query time is faster than single instance. 
           |

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