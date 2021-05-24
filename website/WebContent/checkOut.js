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
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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
    // alert(document.cookie);
}
function countItem(value,array) {
    // counts the value in the array
    let count = 0;
    for(let i = 0; i < array.length; ++i){
        if(array[i] == value)
            count++;
    }
    return count;
}

//https://stackoverflow.com/questions/1960473/get-all-unique-values-in-a-javascript-array-remove-duplicates
function onlyUnique(value, index, self) {
    return self.indexOf(value) === index;
}

// function setCookie(name,value) {
//     document.cookie = name + "=" + value + ";";
// }

function initialize_arrays(item_array, unique_ids) {
    // item_array : id&&title&.&id%=&&title...
    // get the quantity of the items
    // let unique_ids = item_array.filter(onlyUnique); // in the format of id&.&title unique
    // console.log("initialize_arrays function ..");
    let m_ids = [];
    let m_titles = [];
    let m_quans = [];
    let count_item = ""
    let unique_item_array = item_array.filter(onlyUnique); // in the format of id&.&title unique
    // console.log("unique_item_array");
    // console.log(unique_item_array);
    // console.log("item_array");
    // console.log(item_array);
    for (let i=0; i<unique_item_array.length;i++) {
        count_item = countItem(unique_item_array[i],item_array);
        // console.log(count_item);
        let id_title = unique_item[i].split("&&"); //idx 0 id, idx 1 title
        m_ids[i] = id_title[0];
        // console.log("m_ids : "+id_title[0]);
        m_titles[i] = id_title[1];
        // console.log("id_title : "+id_title[1]);
        m_quans[i] = count_item;
        // console.log("m_quans[i] : "+count_item);
        count_item = "";
    }
    return [m_ids, m_titles, m_quans];
}

console.log("At the checkout.js: printing out the cart");

let carts = getCookie("cart");
// console.log("carts: "+carts);

console.log(carts.length);

// let cart;
if (carts.length != 0){
    var item_array = carts.split("&.&"); //id&&title&.&id%=&&title...
}
// var item_array = carts.split("&.&"); //id&&title&.&id%=&&title...
// // [id&&title, id&&title...]
// console.log("item_array: "+item_array);

let unique_item = item_array.filter(onlyUnique); // in the format of id&.&title unique
// console.log("unique_item: "+unique_item);
// console.log("item_array: "+item_array.length);

let totalItemElement = jQuery("#total_item");
let rowHTML = "";
rowHTML += item_array.length;
totalItemElement.append(rowHTML);


var total_price = 0;
let title_id_quan = initialize_arrays(item_array, unique_item);
// console.log("Three: " + title_id_quan);

var movie_ids = title_id_quan[0];
// console.log("id" + movie_ids);

var movie_titles = title_id_quan[1];
// console.log("titles" + movie_titles);

var movie_quantities = title_id_quan[2];
// console.log("1quantites " + movie_quantities);


function plus(i){
    // console.log("In Plus")
    // console.log("before cookie: "+document.cookie);
    movie_quantities[i] = (parseInt(movie_quantities[i]) + 1).toString();
    let one_item = movie_ids[i] + "&&" + movie_titles[i];

    putItems(movie_ids[i],movie_titles[i]);
    // console.log("final cookie: "+document.cookie);
    // console.log("After array "+movie_quantities[i]);
}



function deleteRow(i) {
    console.log("Deleting rows... ");
    console.log("first cookie: "+document.cookie);
    console.log("starting:" + i);


    console.log(movie_quantities);
    console.log(movie_titles);
    console.log(movie_ids);

    let temp_cookies = "";

    for (let j=0; j< movie_ids.length; j++){
        if(j != i){
            console.log("if statement");
            for (let k=0; k< Number(movie_quantities[j]); k++){
                // console.log( "Inside for loops");
                //
                // console.log( "movie_ids[j]");
                // console.log( movie_ids[j]);
                // console.log( "movie_titles[j]");
                // console.log( movie_titles[j]);
                temp_cookies += movie_ids[j] + "&&" +movie_titles[j]  + "&.&";
                // console.log(temp_cookies);
            }
        }
    }

    console.log("~~final temp_cookies");
    console.log(temp_cookies);

    let temp_cookies_arry = temp_cookies.split("&.&");
    temp_cookies_arry.pop();
    temp_cookies = temp_cookies_arry.join("&.&");

    movie_quantities.splice(i,1);
    movie_titles.splice(i,1);
    movie_ids.splice(i,1);

    eraseCookie("cart");
    setCookie("cart",temp_cookies);

    document.getElementById("row"+i).style.display = "none";
    console.log("final: "+document.cookie);
}


function minus(i){
    console.log("In Minus.")
    if (Number(movie_quantities[i]) != 1){
        movie_quantities[i] =  movie_quantities[i]-1;
        // console.log("before: "+document.cookie);
        let one_item = movie_ids[i] + "&&" + movie_titles[i];
        // console.log("item: "+ one_item);
        // console.log("item_array: ", item_array);
        let index = item_array.indexOf(one_item);
        // console.log("index: "+ index);
        item_array.splice(index,1);
        // console.log("after: "+item_array);

        let new_cookie = item_array.join('&.&');
        // console.log("new_cookie: "+ new_cookie);
        eraseCookie("cart");
        setCookie("cart",new_cookie);
        console.log("final: "+document.cookie);
        console.log("After array "+movie_quantities[i]);
    }
    else {
        deleteRow(i);
    }

}

function payment(){

    let temp_id_cookies = ""
    for (let j=0; j< movie_ids.length; j++){
        for (let k=0; k< Number(movie_quantities[j]); k++){
            temp_id_cookies += movie_ids[j] + "&.&";
        }
    }

    let temp_id_arry = temp_id_cookies.split("&.&");
    temp_id_arry.pop();
    temp_id_cookies = temp_id_arry.join("&.&");
    setCookie("movie_ids",temp_id_cookies);
    // alert(document.cookie);
    window.location.replace("payment.html");
}

//update cart
function updateCart(){
    window.location.reload();
}

let itemTableBodyElement = jQuery("#item_table_body");
rowHTML = "";

// make the rows according to the number of unique items
for (let i=0; i< unique_item.length; i++){
    // wirting for each rows
    // rowHTML += '<tr><p>hello</p></tr>';
    rowHTML += "<tr id=\"row" + i.toString() + "\">";
    rowHTML += "<td>" + movie_titles[i] + "</td>";
    rowHTML += "<td>";
    rowHTML += '<input type="button" onClick="minus(\'' + i.toString() + '\')" VALUE="-">';
    rowHTML += movie_quantities[i];
    rowHTML += '<input type="button" onClick="plus(\'' + i.toString() + '\')" VALUE="+">';
    rowHTML += "<td>"+"$"+ 10+"</td>"; //price
    rowHTML += "<td> $"+ movie_quantities[i]*10+"</td>"; //price
    rowHTML += '<th><input type="button" onClick="deleteRow(\'' + i.toString() + '\')" VALUE="delete"></th>';
    rowHTML += "</tr>";
    total_price += movie_quantities[i]*10;
}

itemTableBodyElement.append(rowHTML);

let totalpriceElement = jQuery("#totalprice");
rowHTML ="";
rowHTML += "<p> Cart Total : $" +total_price+"</p>"
totalpriceElement.append(rowHTML);

let updatecartElement = jQuery("#updatecart");
rowHTML ="";
rowHTML += '<input type="button" onClick="updateCart()" VALUE="Update Cart">';
// rowHTML += '<button onClick="window.location.reload();">Update Cart</button>';
updatecartElement.append(rowHTML);


// <button onClick="document.location='payment.html'">Place Order</button>
let paymentElement = jQuery("#payment");
rowHTML ="";
rowHTML += '<input type="button" onClick="payment()" VALUE="Place Order">';
// rowHTML += '<button onClick="window.location.reload();">Update Cart</button>';


paymentElement.append(rowHTML);



