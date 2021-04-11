/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
 function handleMovieResult(resultData) {

    console.log("handleMovieResult: populating movie table from resultData");

    // populate the movie data table from html with id
    // get the movie_table_body from html file with jQuery
    let movieTableBodyElement = jQuery("#movie_table_body");

    for (let i = 0; i < Math.min(20, resultData.length); i++){
        // html
        let rowHTML = "";
        rowHTML += "<tr>"; // opens up tr
        // rowHTML += "<th>"; // opens the th to add each row
        // movie_title, movie_year, movie_director
        // rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + '<a href="singleMovie.html?id=' + resultData[i]['movie_id'] + '">'+ resultData[i]["movie_title"] +'</a>'+ "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";

        rowHTML += "<th>";
        rowHTML += "<ul>";
        if (resultData[i]["genre1"] != null){
            rowHTML += "<li>" + resultData[i]["genre1"] + "</li>";
        }
        if (resultData[i]["genre2"] != null){
            rowHTML += "<li>" + resultData[i]["genre2"] + "</li>";
        }
        if (resultData[i]["genre3"] != null){
            rowHTML += "<li>" + resultData[i]["genre3"] + "</li>";
        }

        rowHTML += "</ul>";
        rowHTML += "</th>";

        rowHTML += "<th>";
        rowHTML += "<ul>";
        if (resultData[i]["star1"] != null){
            rowHTML += "<li>" + '<a href="singleStar.html?id=' + resultData[i]["starid1"] + '">' + resultData[i]["star1"] + "</li>";

            // rowHTML += "<li>" + resultData[i]["star1"] + "</li>";
        }
        if (resultData[i]["star2"] != null){
            rowHTML += "<li>" + '<a href="singleStar.html?id=' + resultData[i]["starid2"] + '">' + resultData[i]["star2"] + "</li>";

            // rowHTML += "<li>" + resultData[i]["star2"] + "</li>";
        }
        if (resultData[i]["star3"] != null){
            rowHTML += "<li>" + '<a href="singleStar.html?id=' + resultData[i]["starid3"] + '">' + resultData[i]["star3"] + "</li>";

            // rowHTML += "<li>" + resultData[i]["star3"] + "</li>";
        }

        rowHTML += "</ul>";
        rowHTML += "</th>";

        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";

        rowHTML += "<tr/>"; // close up tr
        // console.log(resultData[i]["movie_title"]);
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser

 */
// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list", // Setting request url, which is mapped by StarsServlet in MovieListPage.java
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});