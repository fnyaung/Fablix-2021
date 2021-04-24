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
    console.log("no_of_page: " + resultData[0]["no_of_page"]);
    console.log(window.location.href);

    let pageLimit = resultData[0]["no_of_page"];

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
<<<<<<< HEAD
            resultData[i]['movie_title'] + "</a></td>";

=======
            resultData[i]['movie_title'] + "'</a>'</td>";
>>>>>>> 07bb3aabc245429200833de0a30032387e03a47b
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
<<<<<<< HEAD

        // this is button
        rowHTML += "<td> <button onClik =/itmes?newItem=" + resultData[i]['movie_title'] + ">Add</button> </td>";
        // rowHTML += '<td> </td><input type="button" onClick="setCart(\'' + resultData[i]["movie_id"] + '\',\'' + resultData[i]["movie_title"] + '\')" VALUE="Add"> </td>';

        // rowHTML += "<td><button type=\"button\">add</button> </td>";
=======
        rowHTML += "<td><button type=\"button\">add</button> </td>";
>>>>>>> 07bb3aabc245429200833de0a30032387e03a47b


        movieTableBodyElement.append(rowHTML);
    }


    var curr_url = window.location.href;
    var page_idx = (window.location.href).indexOf("page");
    var page_url = curr_url.slice(63, page_idx); // movieList.html?title=&year=2009&director=&star=&genre=&
    var cur_page_no = curr_url.slice(page_idx+5);
    // console.log("curr page: "+ cur_page_no);
    var page_end_idx = +cur_page_no + 20;
    // console.log("page_end_idx: "+ page_end_idx);

    if (page_end_idx > pageLimit){
        // console.log("the page end idx is : "+ page_end_idx);
        page_end_idx = +pageLimit+1;
    }

    let page_list_element = jQuery("#page_list");
    let pageHTML = "";

    for (let i = cur_page_no; i < page_end_idx; i++) {
        pageHTML += "<a href='" + page_url + "page=" + i + "'>" + i + " </a>";
    }
    page_list_element.append(pageHTML);

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
<<<<<<< HEAD
let page = getParameterByName('page');
=======
>>>>>>> 07bb3aabc245429200833de0a30032387e03a47b


// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
<<<<<<< HEAD
    url: "api/movie-list?title=" + title + "&year=" + year + "&director=" + director + "&star=" + star + "&genre=" + genre + "&page=" + page,
=======
    url: "api/movie-list?title=" + title + "&year=" + year + "&director=" + director + "&star=" + star+ "&genre=" + genre,
>>>>>>> 07bb3aabc245429200833de0a30032387e03a47b
    success: (resultData) => handleSingleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

