// let search_form = $("#movie_search");

// /**
//  * Submit the form content with GET method
//  * @param formSubmitEvent
//  */

// // title year director star
// function submitSearchForm(formSubmitEvent) {
//     /**
//      * When users click the submit button, the browser will not direct
//      * users to the url defined in HTML movie-List. Instead, it will call this
//      * event handler when the event is triggered.
//      */
//     console.log("submit search form");

//     formSubmitEvent.preventDefault(); // disable default behavior

//     let search_serial = $('form').serialize()
//     console.log(search_serial);


//     console.log("location=search&"+search_form.serialize());

//     $.ajax(
//         "api/movie-list", {
//             method: "GET",
//             // Serialize the search form to the data sent by GET request
//             data: "location=search&"+search_form.serialize(), // sending data work
//             //prob have to make success
//             // success:
//         }
//     );

// }

// search_form.submit(submitSearchForm);
