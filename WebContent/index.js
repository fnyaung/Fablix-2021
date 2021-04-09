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
        rowHTML += "<tr>";

        // movile title is hyperlink get the movie id
        // console.log(resultData[i]);
        rowHTML += "<th>" +
            '<a href="singleMovie.html?id=' + resultData[i]['movie_id'] + '">'+
            resultData[i]['movie_title'] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        // generes
        rowHTML += "<ul> <th>";
        var genres_list = resultData[i]["genres"].split(",");
        for (let i = 0; i < Math.min(20, genres_list.length); i++){
            rowHTML += "<li>" + genres_list[i] + "</li>";
        }
        rowHTML += "</ul> </th>";

        //stars
        rowHTML += "<ul> <th>";
        var stars_list = resultData[i]["stars"].split(",");
        var starID_list = resultData[i]["starids"].split(",");

        // console.log(starID_list[0]);

        for (let i = 0; i < Math.min(20, stars_list.length); i++){
            rowHTML +=
                "<li>" +
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + starID_list[i] + '">'
                + stars_list[i] +     // display star_name for the link text
                '</a>' +
                "</li>";
        }
        rowHTML += "</ul> </th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";

        rowHTML += "<tr/>"; // close up tr
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