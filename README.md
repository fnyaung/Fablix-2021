## CS 122B Project 2 Group 11
This project displays a list of movies that contains information about a specific movie or star.

### Proj2 Demo Video URL: 
https://youtu.be/l1MuWtVrGTc

### Proj3 Demo Video URL: 
https://youtu.be/8mXbeHGKnnU

### Proj3 Inconsistencies and Duplicates:
- Inconsistencies of MainXML, CastXML, and ActorsXML are all stored within `inconsistencies.txt`
- Duplicates within the XMLs are stored within `Duplicates.txt`

### Proj3 Time Optimization strategies vs naive approach:
- Used HashSet to prevent duplicates in Actors/Movie pairs:
    - Searching for actor duplicates would take O(1) since only Actor's ID is needed to search if an actor already existed in the HashSet.
    - The naive way would have taken O(# actors in a movie) since we would have to search for the entire actors in the list to search for duplicates.
- Used HashMap to connect Actors that played in the movie
    - Lookup for movieID is O(1) when connecting Actors that played in the corresponding film.
    - The naive way would have taken O(# films) to look up movieID if ArrayList was used.
- In the end, there's only 1 dataset (stored in `CastOutput.txt`) after parsing all 3 XML files. There are no duplicate information, such as having two movie names again. As a result, when iterating through the entire dataset to insert into the database, we don't need to iterate 3 datasets. 

### Substring matching Design:
User searches for:
- Star: Abc%
- Title: %Abc%
- Director: %Abc%
- Year: %123% 

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