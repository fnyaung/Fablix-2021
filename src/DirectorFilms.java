import java.util.*;

public class DirectorFilms {
    // key: filmID (fid) value: Film obj
    private HashMap<String, Film> films; // hashmap of films the director directed
//    private List<Film> films; // list of films the director directed
    private String directorName; // director name <dirname>

    public DirectorFilms(){
        films = new HashMap<String, Film>();
//        films = new ArrayList<Film>();
    }

    public String getDirectorName(){
        return this.directorName;
    }
    public void setDirectorName(String directorName){
        this.directorName = directorName;
    }
    public HashMap<String, Film> getfilmHash(){
        return this.films;
    }
    public void addFilm(String fid, Film addMe){
        films.put(fid, addMe);
//        if(!films.containsKey(fid)) {
//            films.put(fid, new ArrayList<>());
//        }
//        films.get(fid).add(addMe);
//        films.add(addMe);
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("DirectorFilms Details - ");
        sb.append("DirectorName: " + getDirectorName());
        sb.append(", ");
        sb.append("Director's Films: \n");

        Iterator fi = films.entrySet().iterator();
        while(fi.hasNext()){
            Map.Entry mapElement = (Map.Entry) fi.next();
            sb.append("KEY: " + mapElement.getKey() + "--");
            Film curr_film = (Film) mapElement.getValue();
            sb.append(curr_film.toString());
            sb.append("\n");
        }
//        for(Film currFilm : getfilmList()){
//            sb.append(currFilm.toString());
//            sb.append("\n");
//        }
        return sb.toString();
    }
}
