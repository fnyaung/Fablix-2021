function eraseCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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

//https://stackoverflow.com/questions/1960473/get-all-unique-values-in-a-javascript-array-remove-duplicates
function onlyUnique(value, index, self) {
    return self.indexOf(value) === index;
}

function initialize_arrays(item_array, unique_ids) {

    let m_ids = [];
    let m_titles = [];
    let m_quans = [];
    let count_item = ""
    let unique_item_array = item_array.filter(onlyUnique); // in the format of id&.&title unique

    for (let i=0; i<unique_item_array.length;i++) {
        count_item = countItem(unique_item_array[i],item_array);
        let id_title = unique_item[i].split("&&"); //idx 0 id, idx 1 title
        m_ids[i] = id_title[0];
        m_titles[i] = id_title[1];
        m_quans[i] = count_item;
        count_item = "";
    }
    return [m_ids, m_titles, m_quans];
}

let carts = getCookie("cart");
// let cart;
if (carts.length != 0){
    var item_array = carts.split("&.&"); //id&&title&.&id%=&&title...
}

let unique_item = item_array.filter(onlyUnique); // in the format of id&.&title unique
var total_price = 0;
let title_id_quan = initialize_arrays(item_array, unique_item);
var movie_ids = title_id_quan[0];
var movie_titles = title_id_quan[1];
var movie_quantities = title_id_quan[2];


let confirm_listBodyElement = jQuery("#confirm_list");
let rowHTML = "";


// movieid&&slaeid&.&moveid&&saleid
let m_s = getCookie("saleid");
let movieid_saleid_tgt = m_s.split("&.&"); //movieid&&slaeid&.&movieid&&slaeid

var moviesale_dict = {};
var movieSale;
for (movieSale in movieid_saleid_tgt){
    var curr_movie = movieSale.split("&&")[0];
    var curr_sale = movieSale.split("&&")[1];
    if(curr_movie in moviesale_dict.key()){
        moviesale_dict[curr_movie] += curr_sale + " ";
    }
    else{
        moviesale_dict[curr_movie] = curr_sale;
    }
}
console.log(moviesale_dict);


let total_array = [];

// [[movieid, slaeid , slaeid ],[movieid, slaeid , slaeid ]]
for (let i=0; i< movieid_saleid_tgt.length; i++) {
    let emp_array = movieid_saleid_tgt[i].split("&&"); //movieid&&slaeid
    total_array.push(emp_array);
}

console.log("total array");
console.log(total_array); // [movieid , saleid, saleid], [movieid , saleid, saleid]


for (let i=0; i< total_array.length; i++) {
    if(total_array[i][0].localeCompare(movie_ids[i]) == 0){
        rowHTML += "<p>" + movie_titles[i] + " : ";
        rowHTML += movie_quantities[i] + " ";
        rowHTML += "$"+ movie_quantities[i]*10; //price
        //print saleid
        for (let j=1; j< total_array[i].length; j++) {
            rowHTML += total_array[j] + "  ";
        }
        total_price += movie_quantities[i]*10;
        rowHTML += "</p>";
    }
}

// // make the rows according to the number of unique items
// for (let i=0; i< unique_item.length; i++){
//     // writing confirmation
//     rowHTML += "<p>" + movie_titles[i] + " : ";
//     rowHTML += movie_quantities[i] + " ";
//     rowHTML += "$"+ movie_quantities[i]*10 + "</p>" //price
//     total_price += movie_quantities[i]*10;
// }
rowHTML += '<p>total price: $' + total_price + '</p>'

confirm_listBodyElement.append(rowHTML);
