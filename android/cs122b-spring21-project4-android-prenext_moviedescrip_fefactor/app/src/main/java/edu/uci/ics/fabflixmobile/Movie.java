package edu.uci.ics.fabflixmobile;

public class Movie {
    private final String name;
    private final short year;
    private final String director;
    private final String id;
    private final String genres;
    private final String rating;
    private final String stars;

    public Movie(String name, short year, String id, String director, String genres, String rating, String stars) {
        this.name = name;
        this.year = year;
        this.id = id;
        this.director = director;
        this.genres = genres;
        this.rating = rating;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getGenres() {
        return genres;
    }

    public String getRating() {
        return rating;
    }

    public String getStars() {
        return stars;
    }


}