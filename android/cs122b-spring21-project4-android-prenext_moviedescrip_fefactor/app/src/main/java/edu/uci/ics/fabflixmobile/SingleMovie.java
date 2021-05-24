package edu.uci.ics.fabflixmobile;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class SingleMovie extends Activity{

    private String movieTitle;
    private short movieYear;
    private String movieDirector;
    private String movieGenre;
    private String movieStars;

    TextView title;
    TextView year;
    TextView director;
    TextView stars;
    TextView genres;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.movie); // we load the longin view

        title = (TextView)findViewById(R.id.title);
        year = (TextView)findViewById(R.id.year);
        director = (TextView)findViewById(R.id.director);
        stars = (TextView)findViewById(R.id.stars);
        genres = (TextView)findViewById(R.id.genres);

        Bundle movieDetail = this.getIntent().getExtras();
        movieTitle = movieDetail.getString("movieTitle");
        movieYear = movieDetail.getShort("movieYear");
        movieDirector = movieDetail.getString("movieDirector");
        movieGenre = movieDetail.getString("movieGenre");
        movieStars = movieDetail.getString("movieStars");

        title.setText(movieTitle);
        year.setText(String.valueOf(movieYear));
        director.setText(movieDirector);
        genres.setText(movieGenre);
        stars.setText(movieStars);
    }
}