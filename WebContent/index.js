let search_form = $("#movie_search");

/**
 * Submit the form content with GET method
 * @param formSubmitEvent
 */

// title year director star
function submitSearchForm(formSubmitEvent) {

    formSubmitEvent.preventDefault(); // disable defaul behavior


    console.log("submit search form");
    let search_serial = $('form').serialize()
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML movie-List. Instead, it will call this
     * event handler when the event is triggered.
     */
    
    window.location.href = "movieList.html?location=search&"+search_serial;
    //
    // $.ajax(
    //     "/api/movie-list", {
    //         method: "GET",
    //         // Serialize the login form to the data sent by POST request
    //         data: search_form.serialize(),
    //         success: handleLoginResult
    //     }
    // );
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm); // click them this function called