package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {

    private EditText title;
    private Button search_button;
    private Button home_button;
    private Button prev_button;
    private Button next_button;
    private String query;
    private int totalPage = 0;
    public int currPage = 1;


    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "100.25.153.155";
//    private final String host = "10.0.2.2";
    private final String port = "8080";
//    private final String domain = "cs122b_spring21_project1_api_example_war";
    private final String domain = "cs122b-spring21-project1-api-example";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;


    // call back function whenever this application is launched
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.search); // we load the longin view
        // below are the ids that i've created
        title = findViewById(R.id.title);
        search_button = findViewById(R.id.search_button);
        //assign a listener to call a function to handle the user request when clicking a button
        // when the user clicks a login button this function is called
        search_button.setOnClickListener(view -> search(currPage));
    }

    public void search(int currPage) {
        System.out.println("\ttitle is : "+title.getText());
        query = title.getText().toString();
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/movie-list?title="+ title.getText()+"&year=&director=&star=&genre=&limit=20&sort=RD&page="+ currPage,
                response -> {
                    // handle response
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    ArrayList<Movie> movies = null;
                    try {
                        movies = setUpMovieList(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // set the content view
                    setContentView(R.layout.listview);
                    MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

                    // set up the buttons
                    home_button = findViewById(R.id.home_btn);
                    prev_button = findViewById(R.id.prev_btn);
                    next_button = findViewById(R.id.next_btn);

                    if(currPage == 1){
                        System.out.println("curr page == 1");
                        prev_button.setEnabled(false);
                    }
                    else{
                        System.out.println("curr page != 1");
                        prev_button.setEnabled(true);
                    }
                    if(currPage == totalPage){
                        next_button.setEnabled(false);
                    }

                    home_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            homebtn();
                        }
                    });

                    prev_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prevbtn(currPage);
                        }
                    });

                    next_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextbtn(currPage);
                        }
                    });

                    ListView listView = findViewById(R.id.list);
                    listView.setAdapter(adapter);

                    ArrayList<Movie> finalMovies = movies;
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Movie movie = finalMovies.get(position);
                        String message = String.format("Clicked on position: name: %s, %d", movie.getName(), movie.getYear());
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        // this is where we should activate this
                        Intent listPage = new Intent(ListViewActivity.this, SingleMovie.class);
                        listPage.putExtra("movieTitle", movie.getName());
                        listPage.putExtra("movieYear", movie.getYear());
                        listPage.putExtra("movieDirector", movie.getDirector());
                        listPage.putExtra("movieGenre", movie.getGenres());
                        listPage.putExtra("movieStars", movie.getStars());
                        // activate the list page.
                        startActivity(listPage);

                    });
                },
                error -> {
                    // error
                    Log.d("search.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // GET request form data
                final Map<String, String> params = new HashMap<>();
                params.put("search_txt", title.getText().toString());
                return params;
            }
        };

        // important: queue.add is where the  request is actually sent
        queue.add(searchRequest);
    }

    public ArrayList<Movie> setUpMovieList(String response) throws JSONException {
        final ArrayList<Movie> movies = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        Log.d("Array length", String.valueOf(jsonArray.length()));
        currPage = 1;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject res = new JSONObject(jsonArray.get(i).toString());
            String movie_id = res.getString("movie_id");
            String movie_title = res.getString("movie_title");
            String movie_year = res.getString("movie_year");
            String movie_rating = res.getString("movie_rating");
            String director = res.getString("movie_director");
            String stars = res.getString("stars");
            String genres = res.getString("genres");
            String no_of_page = res.getString("no_of_page");
            totalPage = Integer.parseInt(no_of_page);

            int year = Integer.parseInt(movie_year);

            movies.add(new Movie(movie_title, (short) year, movie_id, director, genres, movie_rating, stars));
        }
        return movies;
    }

    public void homebtn(){
        Intent listPage = new Intent(ListViewActivity.this, ListViewActivity.class);
        startActivity(listPage);
    }

    public void prevbtn(int currPage){
        currPage--;
        Log.d("prevbtn", String.valueOf(currPage));
        search(currPage);
    }

    public void nextbtn(int currPage){
        currPage++;
        Log.d("nextbtn", String.valueOf(currPage));
        search(currPage);
    }
}

