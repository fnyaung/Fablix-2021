/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
function getParameterByName(target){
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

    console.log("handleMovieSingle: populating single movie table from resultData");

    // populate the movie data table from html with id
    // get the movie_table_body from html file with jQuery
    let movieInfo = jQuery("#movie-info");
    // console.log(resultData);

    movieInfo.append("<p>Title : "+resultData[0]["movie_title"]+"</p>");

    //
    // for (let i = 0; i < Math.min(20, resultData.length); i++){
    // html
    let movieTableBodyElement = jQuery("#movie_table_body");

    let rowHTML = "";
    rowHTML += "<tr>";

    // movile title is hyperlink get the movie id
    console.log(resultData[0]);
    // rowHTML += "<th>" +
    //     '<a href="singleMovie.html?id=' + resultData[0]['movie_id'] + '">'+
    //     resultData[i]['movie_title'] + "</th>";
    // console.log("movieID: ",resultData[0]['movie_id'])
    rowHTML += "<td>" + '<a href="singleMovie.html?id=' + resultData[0]['movie_id'] + '">'+ resultData[0]["movie_title"] +'</a>'+ "</td>";
    rowHTML += "<td>" + resultData[0]["movie_year"] + "</td>";
    rowHTML += "<td>" + resultData[0]["movie_director"] + "</td>";
    // generes
    rowHTML += "<ul> <td>";
    var genres_list = resultData[0]["genres"].split(",");
    for (let i = 0; i < Math.min(20, genres_list.length); i++){
        rowHTML += "<li>" + genres_list[i] + "</li>";
    }
    rowHTML += "</ul> </td>";

    //stars
    rowHTML += "<ul> <td>";
    var stars_list = resultData[0]["stars"].split(",");
    var starID_list = resultData[0]["star_id"].split(",");

    // rowHTML += "<th>" +
    //     '<a href="singleMovie.html?id=' + resultData[i]['movie_id'] + '">'+
    //     resultData[i]['movie_title'] + "</th>";

    for (let i = 0; i < Math.min(20, stars_list.length); i++){
        rowHTML += "<li>" + '<a href="singleStar.html?id=' + starID_list[i] + '">' + stars_list[i] + "</li>";
    }
    rowHTML += "</ul> </td>";
    rowHTML += "<td>" + resultData[0]["movie_rating"] + "</td>";

    // rowHTML += "<tr/>"; // close up tr
    movieTableBodyElement.append(rowHTML);
// }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser

 */
let movieId = getParameterByName('id');
// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/single-movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in MovieListPage.java
    success: (resultData) => handleSingleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

