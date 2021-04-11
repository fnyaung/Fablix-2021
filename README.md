## CS 122B Project 1 Group 11
This project displays a list of movies that contains information about a specific movie or star.

### Demo Video URL: 

### Contributions to this project:
- Hyejin Kim and Faustina Nyaung both worked on `MovieListPage.java`, `index.html`, and `index.js`. We both thought of the sql queries together.
- Hyejin Kim worked on `SingleMoviePage.java`, `singleMovie.html`, and `singleMovie.js`. 
- Faustina Nyaung worked on `SingleStarPage.java`, `singleStar.html`, and `singleStar.js`.

### To run this project:
1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-11.git`
2. Open IntelliJ -> Import Project -> Choose the project you just cloned (The root path must contain the pom.xml!) -> Choose Import project from external model -> choose Maven -> Click on Finish -> The IntelliJ will load automatically
3. For "Root Directory", right click "cs122b-spring21-team-11" -> Mark Directory as -> sources root
4. make sure you have the `moviedb` database in all 3 java files: `MovieListPage.java`, `SingleMoviePage.java`, `SingleStar.java` in the `src` folder.
5. To run the example, follow the instructions on [canvas](https://canvas.eee.uci.edu/courses/36596/pages/intellij-idea-tomcat-configuration)

### Brief Explanation
- `index.html` displays the list of movies. Open the url `http://localhost:8080/cs122b_spring21_team_11_war/`, if the list of movies shows up, then that means that you have successfully deployed the project.

- `MovieListPage.java`, `SingleMoviePage.java`, `SingleStar.java` are Java servlets that talks to the database and gets the list of movies, single movie, and single star page in respect to the java files listed. It generates the HTML strings and write it to response.
- By clicking on the hyperlinks on the MovieListPage it should redirect you to
   - A single star page, which contains star's info and movies played. (or)
   - A single movie page, which contains movie's info and all genres and stars associated with the movie.
   
- Note: If using IntelliJ, the url has to be set correctly: In `Edit configuration` -> `Tomcat` -> `Deployment` tab -> `Application context`, change the url to `/cs122b_spring21_team_11_war`
