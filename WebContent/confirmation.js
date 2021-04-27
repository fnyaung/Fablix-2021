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
console.log("movieid_saleid_tgt");
movieid_saleid_tgt.pop();

var moviesale_dict = {};

for (let i = 0; i < movieid_saleid_tgt.length; i++) {
    let temp_movieSale = movieid_saleid_tgt[i];



    var curr_movie = temp_movieSale.split("&&")[0];
    var curr_sale = temp_movieSale.split("&&")[1];

    if(curr_movie in moviesale_dict){
        curr_sale = " " + curr_sale;
        moviesale_dict[curr_movie] += curr_sale;
    }
    else{
        moviesale_dict[curr_movie] = curr_sale;
    }
}


// moviesale_dict
let mid = "";
for(mid in moviesale_dict) {
    for (let i = 0; i < movie_ids.length; i++) {
        if (mid.localeCompare(movie_ids[i]) == 0){

            // matching id then we get titles
            rowHTML += "<p> Title : " + movie_titles[i] + "</p>"; //titles
            rowHTML += "<p> Price : $ " + movie_quantities[i] * 10 + "</p> "; //price
            total_price += Number( movie_quantities[i] * 10);
            rowHTML += "<p> Sale Id : "+ moviesale_dict[mid] + " </p>";//sales id
            rowHTML += "<br>";
        }
    }
}
rowHTML += '<p>total price: $' + total_price + '</p>'
confirm_listBodyElement.append(rowHTML);
