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

function countItem(value,array) {
    // counts the value in the array
    var count = 0;
    for(var i = 0; i < array.length; ++i){
        if(array[i] == value)
            count++;
    }
    return count;
}

//https://stackoverflow.com/questions/1960473/get-all-unique-values-in-a-javascript-array-remove-duplicates
function onlyUnique(value, index, self) {
    return self.indexOf(value) === index;
}
//
// function update(button){
//     // if 1 incrase 2 decrese
//
//     location.reload();
//     return false;
// }
//

console.log("At the checkout.js: printing out the cart");

let carts = getCookie("cart");
console.log("carts: "+carts);

let item_array = carts.split("&.&"); //id&&title&.&id%=&&title...
// [id&&title, id&&title...]
console.log("item_array: "+item_array);

let unique_item = item_array.filter(onlyUnique); // in the format of id&.&title unique
console.log("unique_item: "+unique_item);
console.log("item_array: "+item_array.length);

let totalItemElement = jQuery("#total_item");
let rowHTML = "";
rowHTML += item_array.length;
totalItemElement.append(rowHTML);

let itemTableBodyElement = jQuery("#item_table_body");
rowHTML = "";

let total_price = 0;



for (let i=0; i<unique_item.length;i++){
    rowHTML += "<tr>";
    console.log(" unique_item[i]"+ unique_item[i]);

    // unique items in the cookie array
    let id_title = unique_item[i].split("&&"); //idx 1 id idx 2 title

    console.log("id_title"+id_title);
    rowHTML += "<th>"+ id_title[1]+"</th>"; //title\

    // counting quantity
    let count_item = countItem(unique_item[i],item_array);
    // rowHTML += "<th>"+ count_item+"</th>"; //quantity
    rowHTML += "<th>"; //quantity
    rowHTML += '<input type="button" onClick="update(0)" VALUE="-">';
    rowHTML += count_item; //quantity

    rowHTML += '<input type="button" onClick="update(1)" VALUE="+">';
    // rowHTML += "<th>"+ count_item+"</th>"; //quantity

    rowHTML += "<th>"+"$"+ 10+"</th>"; //price
    rowHTML += "<th> $"+ count_item*10+"</th>"; //price
    total_price += count_item*10;
    rowHTML += "</tr>";
}
rowHTML += "<td> Cart Total : " +total_price+"</td></tr>"

itemTableBodyElement.append(rowHTML);

