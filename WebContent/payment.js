let payment_form = $("#payment_form");

/**
 *
 */
function handlePaymentResult(resultDataString){
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle payment response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // fix up this part vvvv

    // If payment succeeds, it should be recorded in the system
    // (in the "sales" table) and a confirmation page should be
    // displayed. You might need to alter the "sales" table
    // in order to support multiple copies of movie.
    if(resultDataJson["status"] === "success"){
        // record payment info in "sales" table
        console.log("Show success message");
        console.log(resultDataJson["message"]);
        var movieSaleID = resultDataJson["movieSaleID"];
        console.log("printing movieSaleID:");
        console.log(movieSaleID)
        window.location.replace("confirmation.html");
    }else{
        // If payment failed, it should return back to Payment Page with an
        // error message for customers to re-enter payment information.
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#payment_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Handle the data returned by PaymentServlet
 * @param resultDataString jsonObject
 */
function submitPaymentForm(formSubmitEvent){
    console.log("submit payment form")

    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/payment",{
            method: "POST",
            // Serialize the payment form to the data sent by POST request
            data: payment_form.serialize(),
            success: handlePaymentResult
        }
    );
}

// Bind the submit action of the form to a handler function
payment_form.submit(submitPaymentForm);