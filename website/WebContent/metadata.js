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

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: adding star info from resultData");
    console.log(resultData)

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let addmetadataElement = jQuery("#meta_data_result");

    let rowHTML = "";

    for (let i = 0; i < resultData.length; i++) {
        console.log(resultData[i]);

        console.log(resultData[i]["tableName"]);
        rowHTML += "<div>";
        rowHTML += "<b style='font-size:25px'>" + resultData[i]["tableName"] + "</b>";

        console.log(resultData[i]["element"]);

        let elem_list = resultData[i]["element"].split("&");
        elem_list.pop();

        for (let i = 0; i < elem_list.length; i++) {
            if ( i % 2 == 0)
            {
                rowHTML += "<p>" + elem_list[i] + " - ";
            }
            else {
                rowHTML += elem_list[i] + "</p>";
            }
        }
        rowHTML += "<br>";
        rowHTML += "</div>";

    }





    addmetadataElement.append(rowHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// for single star: star_star, birthyear

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/metadata" ,
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

