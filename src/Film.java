import java.util.HashSet; // Import the HashSet class
import java.util.Iterator;
import java.util.List;

/**
 * Information extracted from mains243.xml
 */
public class Film{
    private String fId; // Movie ID <fid>
    private String fTitle; // Movie Title <t>
    private Integer fYear; // Movie Year <year>
    private String genreName; // genre name <cat>
    private Integer genreID; // genreID
    private HashSet<Actor> actors; // actor names (prevent duplicates)

    public Film(){
        actors = new HashSet<>();
    }

    public Film(String fId, String fTitle, Integer fYear, String genreName){
        this.fId = fId;
        this.fTitle = fTitle;
        this.fYear = fYear;
        this.genreName = genreName;
        this.genreID = -1; // uninitialized genreID. Must set genreID in the future
        actors = new HashSet<>();
    }

    // Helper functions: Get Movie's information
    public String getfId(){
        return fId;
    }
    public String getfTitle(){
        return fTitle;
    }
    public Integer getfYear(){
        return fYear;
    }
    public String getGenreName(){
        return genreName;
    }
    public Integer getGenreID(){
        return genreID;
    }
    public HashSet<Actor> getActors(){
        return actors;
    }
    // Helper functions: set movie data
    public void setfId(String fId){
        this.fId = fId;
    }
    public void setfTitle(String fTitle){
        this.fTitle = fTitle;
    }
    public void setfYear(Integer fYear){
        this.fYear = fYear;
    }
    public void setGenreName(String genreName){
        this.genreName = genreName;
    }
    public void setGenreID(Integer genreID){
        this.genreID = genreID;
    }

    public void addActor(Actor newActor){
        actors.add(newActor);
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Film Details - ");
        sb.append("FilmID: " + getfId());
        sb.append(", ");
        sb.append("fTitle: " + getfTitle());
        sb.append(", ");
        sb.append("fYear: " + getfYear());
        sb.append(", ");
        sb.append("genreName: " + getGenreName());
        sb.append(", ");
        sb.append("genreID: " + getGenreID());
        sb.append(". \n>>> Actors in Films: \n");

        Iterator<Actor> it  = getActors().iterator();
        while(it.hasNext()){
            sb.append(it.next().toString());
        }

        return sb.toString();
    }
}

