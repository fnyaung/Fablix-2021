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

//https://stackoverflow.com/questions/2144386/how-to-delete-a-cookie
function setCookie(name,value) {
    document.cookie = name + "=" + value + ";";
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    document.cookie = name+'=; Max-Age=-99999999;';
}

function updateCarts(){
    let movie_array = [];

    let tempids = getCookie("ids");
    let temp_titles = getCookie("titles");

    let ids_arr = tempids.split("&&");
    console.log(ids_arr);
    let titles_arr = temp_titles.split("&&");
    console.log(titles_arr);

    // var unique_tempids = a.filter(onlyUnique);
}

function putItems(movie_id, movie_title){
    // we assume name is ids && titles
    if (document.cookie.indexOf("cart")) {
        //cookies ; name=value
        //if it's first time
        setCookie("cart",movie_id+"&&"+movie_title);

    }
    else { //it's the 2nd time
        // we save the title
        let temp_cart = getCookie("cart");

        eraseCookie("cart");

        let new_cart = temp_cart + "&.&" + movie_id+"&&"+movie_title;
        setCookie("cart",new_cart);

    }
    console.log(document.cookie)
    alert("added!");
}


function handleSingleMovieResult(resultData) {
    console.log("handleMovieResult: populating movie table from resultData");
    console.log("no_of_page: " + resultData[0]["no_of_page"]);
    console.log(window.location.href);

    var curr_URL = window.location.href;
    if (curr_URL.includes("sort=")) {
        if (curr_URL.includes("sort=T")) {
            // TA is the default Title sort
            if (curr_URL.includes("sort=TA")) {
                var curr_sort = "sort=TA";
                // sort = TA (switch to TD)
                curr_URL = curr_URL.replace("sort=TA", "sort=TD");
                var sortTitle_URL = '<a href="' + curr_URL + '">Title</a>';
                curr_URL = curr_URL.replace("sort=TD", "sort=RD");
                var sortRating_URL = '<a href="' + curr_URL + '">Rating</a>';
            } else {
                var curr_sort = "sort=TD";
                // sort = TD (switch to TA)
                curr_URL = curr_URL.replace("sort=TD", "sort=TA");
                var sortTitle_URL = '<a href="' + curr_URL + '">Title</a>';
                curr_URL = curr_URL.replace("sort=TA", "sort=RD");
                var sortRating_URL = '<a href="' + curr_URL + '">Rating</a>';
            }
        } else {
            // RD is default R sort (default sort in general)
            if (curr_URL.includes("sort=RA")) {
                var curr_sort = "sort=RA";
                // sort by RA (switch)
                curr_URL = curr_URL.replace("sort=RA", "sort=RD");
                var sortRating_URL = '<a href="' + curr_URL + '">Rating</a>';
                curr_URL = curr_URL.replace("sort=RD", "sort=TA");
                var sortTitle_URL = '<a href="' + curr_URL + '">Title</a>';
            } else {
                var curr_sort = "sort=RD";
                // sort by RD (switch)
                // has rating
                curr_URL = curr_URL.replace("sort=RD", "sort=RA");
                var sortRating_URL = '<a href="' + curr_URL + '">Rating</a>';
                curr_URL = curr_URL.replace("sort=RA", "sort=TA");
                var sortTitle_URL = '<a href="' + curr_URL + '">Title</a>';
            }
        }
    } else {
        // sort is rating descending by default
        var curr_sort = "sort=RD";
        var sortTitle_URL = '<a href="' + curr_URL + "&sort=TA" + '">Title</a>';
        var sortRating_URL = '<a href="' + curr_URL + "&sort=RD" + '">Rating</a>';
    }

    let title_th_element = jQuery("#title_th");
    title_th_element.append(sortTitle_URL);

    let rating_th_element = jQuery("#rating_th");
    rating_th_element.append(sortRating_URL);

    let pageLimit = resultData[0]["no_of_page"];
    // console.log(pageLimit);

    // populate the movie data table from html with id
    // get the movie_table_body from html file with jQuery
    let movieTableBodyElement = jQuery("#movie_table_body");

    let cur_url = window.location.href;
    let limt_idx = window.location.href.indexOf("limit");
    let sort_idx = window.location.href.indexOf("sort");

    let limit_end = cur_url.slice(limt_idx-1,cur_url.length);
    let curr_page_num = cur_url.slice(limt_idx+6, sort_idx-1);
    // console.log("~~~~~~!");
    // console.log(limt_idx);
    // console.log("Slice");
    // console.log(cur_url.slice(limt_idx+6, sort_idx-1));
    // console.log(resultData.length);


    for (let i = 0; i < Math.max(curr_page_num, resultData.length); i++) {
        // html
        let rowHTML = "";
        rowHTML += "<tr>";

        // movie title is hyperlink get the movie id
        // console.log(resultData[i]);
        rowHTML += "<td>" +
            '<a href="singleMovie.html?id=' + resultData[i]['movie_id'] + '">' +
            resultData[i]['movie_title'] + "</a></td>";

        rowHTML += "<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>";
        // genres
        rowHTML += "<ul> <td>";
        let genres_list = resultData[i]["genres"].split(",");
        for (let i = 0; i < Math.min(3, genres_list.length); i++) {
            rowHTML += "<li> <a href=movieList.html?title=&year=&director=&star=&genre=" + genres_list[i] + limit_end+">" + genres_list[i] + "</a></li>";
        }
        rowHTML += "</ul> </td>";

        //stars
        rowHTML += "<ul> <td>";
        var stars_list = resultData[i]["stars"].split(",");
        var starID_list = resultData[i]["star_id"].split(",");

        // console.log(starID_list[0]);

        // for (let i = 0; i < Math.min(20, 3); i++) {
        for (let i = 0; i <starID_list.length; i++) {
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
        let movieid = resultData[i]['movie_id'];
        let movietitle = resultData[i]['movie_title'];
        // this is button
        rowHTML += '<td>$10<br><input type="button" onClick="putItems(\'' + resultData[i]["movie_id"] + '\',\'' + resultData[i]["movie_title"] + '\')" VALUE="Add"></td>';
        movieTableBodyElement.append(rowHTML);
    }


    let curr_url = window.location.href;
    let page_idx = (window.location.href).indexOf("page");
    let page_url = curr_url.slice(63, page_idx); // movieList.html?title=&year=2009&director=&star=&genre=&sort=RD
    let cur_page_no = curr_url.slice(page_idx + 5);
    let page_end_idx;

    // page limit is the number of page
    if (pageLimit < Number(cur_page_no)){
        page_end_idx = 2;
        console.log("page_end_idx set to 2");
    }
    else if ((Number(cur_page_no) + 1) <= pageLimit) {
        page_end_idx = Number(cur_page_no) + 2;
    }
    else{
        page_end_idx = pageLimit;
    }

    let page_list_element = jQuery("#page_list");
    let pageHTML = "";
    let tem_curr_page_no = Number(cur_page_no);
    if (cur_page_no > 1){
        cur_page_no = Number(cur_page_no)- 1;
    }

    // let limit = cur_url.slice(0,limt_idx+6);
    // console.log("~~~");
    //
    // console.log(limit);


    for (let i = cur_page_no; i < page_end_idx; i++) {
        if (i < tem_curr_page_no){
            if(page_url.includes("sort=RA")) {
                page_url = page_url.replace("sort=RA", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + "<<" + " </a>";
            }else if(page_url.includes("sort=RD")){
                page_url = page_url.replace("sort=RD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + "<<" + " </a>";
            }else if(page_url.includes("sort=TA")){
                page_url = page_url.replace("sort=TA", curr_sort);
                pageHTML += "<a href='" + page_url +"page=" + i + "'>" + "<<" + " </a>";
            }else if(page_url.includes("sort=TD")){
                page_url = page_url.replace("sort=TD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + "<<" + " </a>";
            }else{
                pageHTML += "<a href='" + page_url + curr_sort +"&page=" + i + "'>" + "<<" + " </a>";
            }
        }else if (i == tem_curr_page_no){
            if(page_url.includes("sort=RA")) {
                page_url = page_url.replace("sort=RA", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + i + " </a>";
            }else if(page_url.includes("sort=RD")){
                page_url = page_url.replace("sort=RD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + i + " </a>";
            }else if(page_url.includes("sort=TA")){
                page_url = page_url.replace("sort=TA", curr_sort);
                pageHTML += "<a href='" + page_url +"page=" + i + "'>" + i + " </a>";
            }else if(page_url.includes("sort=TD")){
                page_url = page_url.replace("sort=TD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + i + " </a>";
            }else{
                pageHTML += "<a href='" + page_url + curr_sort +"&page=" + i + "'>" + i + " </a>";
            }
        }else if (i > tem_curr_page_no){
            if(page_url.includes("sort=RA")) {
                page_url = page_url.replace("sort=RA", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + ">>" + " </a>";
            }else if(page_url.includes("sort=RD")){
                page_url = page_url.replace("sort=RD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + ">>" + " </a>";
            }else if(page_url.includes("sort=TA")){
                page_url = page_url.replace("sort=TA", curr_sort);
                pageHTML += "<a href='" + page_url +"page=" + i + "'>" + ">>" + " </a>";
            }else if(page_url.includes("sort=TD")){
                page_url = page_url.replace("sort=TD", curr_sort);
                pageHTML += "<a href='" + page_url + "page=" + i + "'>" + ">>" + " </a>";
            }else{
                pageHTML += "<a href='" + page_url + curr_sort +"&page=" + i + "'>" + ">>" + " </a>";
            }
        }
    }

    page_list_element.append(pageHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser

 */
let title = getParameterByName('title');
let year = getParameterByName('year');
let director = getParameterByName('director');
let star = getParameterByName('star');
let genre = getParameterByName('genre');
let page = getParameterByName('page');
let sort = getParameterByName('sort');
let limit = getParameterByName('limit');


// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list?title=" + title + "&year=" + year + "&director=" + director + "&star=" + star + "&genre=" + genre + "&limit=" + limit + "&sort=" + sort + "&page=" + page,
    success: (resultData) => handleSingleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

