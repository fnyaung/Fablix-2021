/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Use regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


function handleSingleMovieResult(resultData) {

    console.log("handleMovieResult: populating movie table from resultData");

    // populate the movie data table from html with id
    // get the movie_table_body from html file with jQuery
    let movieTableBodyElement = jQuery("#movie_table_body");

    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        // html
        let rowHTML = "";
        rowHTML += "<tr>";

        // movile title is hyperlink get the movie id
        // console.log(resultData[i]);
        rowHTML += "<td>" +
            '<a href="singleMovie.html?id=' + resultData[i]['movie_id'] + '">' +
            resultData[i]['movie_title'] + "'</a>'</td>";
        rowHTML += "<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>";
        // generes
        rowHTML += "<ul> <td>";
        var genres_list = resultData[i]["genres"].split(",");
        for (let i = 0; i < Math.min(20, genres_list.length); i++) {
            rowHTML += "<li>" + genres_list[i] + "</li>";
        }
        rowHTML += "</ul> </td>";

        //stars
        rowHTML += "<ul> <td>";
        var stars_list = resultData[i]["stars"].split(",");
        var starID_list = resultData[i]["star_id"].split(",");

        // console.log(starID_list[0]);

        for (let i = 0; i < Math.min(20, 3); i++) {
            rowHTML +=
                "<li>" +
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="singleStar.html?id=' + starID_list[i] + '">'
                + stars_list[i] +     // display star_name for the link text
                '</a>' +
                "</li>";
        }
        rowHTML += "</ul> </td>";
        rowHTML += "<td>" + resultData[i]["movie_rating"] + " </td>";
        rowHTML += "<td><button type=\"button\">add</button> </td>";


        // rowHTML += "<tr/>"; // close up tr
        movieTableBodyElement.append(rowHTML);
    }
}




/**
 * Once this .js is loaded, following scripts will be executed by the browser

 */
// let location = getParameterByName('location');
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let star = getParameterByName('star');
let genre = getParameterByName('genre');


// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list?title=" + title + "&year=" + year + "&director=" + director + "&star=" + star+ "&genre=" + genre,
    success: (resultData) => handleSingleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});
