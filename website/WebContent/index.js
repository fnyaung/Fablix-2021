/*
 * CS 122B Project 4. Autocomplete Example.
 *
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 *
 * This example implements the basic features of the autocomplete search, features that are
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 *
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 *
 */


/*
 * This function is called by the library when it needs to lookup a query.
 *
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */

let cachedResult = {};

function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    if (query in cachedResult){
        console.log("Found cached result");
        console.log(cachedResult[query]);
        doneCallback( { suggestions: cachedResult[query] } );
    }
    else{
        //do the rest
        console.log("sending AJAX request to backend Java Servlet")

        // TODO: if you want to check past query results first, you can do it here

        // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
        // with the query data
        jQuery.ajax({
            "method": "GET",
            // generate the request url from the query.
            // escape the query string to avoid errors caused by special characters
            "url": "api/movie-list?title=" + escape(query)+ "&year=&director=&star=&genre=&limit=10&sort=RD&page=1",
            // "url": "movieList.html?title="+ escape(query)+ "&year=&director=&star=&genre=&limit=25&sort=RD&page=1",
            "success": function(data) {
                // pass the data, query, and doneCallback function into the success handler
                handleLookupAjaxSuccess(data, query, doneCallback)
                // sueccsuful case
            },
            "error": function(errorData) {
                console.log("lookup ajax error")
                console.log(errorData)
            }
        })
    }


    //!! This is before i did cache
    // console.log("sending AJAX request to backend Java Servlet")
    //
    // // TODO: if you want to check past query results first, you can do it here
    //
    // // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
    // // with the query data
    // jQuery.ajax({
    //     "method": "GET",
    //     // generate the request url from the query.
    //     // escape the query string to avoid errors caused by special characters
    //     "url": "api/movie-list?title=" + escape(query)+ "&year=&director=&star=&genre=&limit=10&sort=RD&page=1",
    //     // "url": "movieList.html?title="+ escape(query)+ "&year=&director=&star=&genre=&limit=25&sort=RD&page=1",
    //     "success": function(data) {
    //         // pass the data, query, and doneCallback function into the success handler
    //         handleLookupAjaxSuccess(data, query, doneCallback)
    //         // sueccsuful case
    //     },
    //     "error": function(errorData) {
    //         console.log("lookup ajax error")
    //         console.log(errorData)
    //     }
    // })
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */

function handleLookupAjaxSuccess(data, query, doneCallback) {

    // show list of matching result in the drop down box
    console.log("lookup ajax successful")
    console.log(data)

    // * 	{ "value": "Superman", "data": { "heroID": 101 } },

    let resultData = [];
    for (let i = 0; i < Math.min(10, data.length); i++) {
        resultData[i] = {}; //
        resultData[i].value = data[i]["movie_title"];
        resultData[i].data = data[i]["movie_id"];
    }

    cachedResult[query] = resultData; // save the data

    // console.log("Result data")
    // console.log(resultData)

    // parse the string into JSON
    // var jsonData = JSON.parse(resultData);
    // console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: resultData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion

    console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"])
    //url: "api/single-movie?id=" + movieId, // Setting request url, which is mapped by StarsServlet in MovieListPage.java
    window.location.href = "singleMovie.html?id=" + suggestion['data'];
}


/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars: 3, // minimum char for autocomplete
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
    // "url": "movieList.html?title="+ escape(query)+ "&year=&director=&star=&genre=&limit=25&sort=RD&page=1",
    window.location.href = "movieList.html?title="+ query +"&year=&director=&star=&genre=&limit=25&sort=RD&page=1";
f
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


