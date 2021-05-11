
public class Actor {
    private String aID; // actor's ID
    private Integer birthYear; // <dob>
    private String aName; // actor's stagename <a> or <stagename>

    public Actor(){
    }

    public Actor(String aID, Integer birthYear, String aName){
        this.aID = aID;
        this.birthYear = birthYear;
        this.aName = aName;
    }

    public String getaID() {
        return aID;
    }

    public void setaID(String aID) {
        this.aID = aID;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("*** Actor Details - ");
        sb.append("aName: " + getaName() + ", ");
        sb.append("aID: " + getaID() + ", ");
        sb.append("aDOB: " + getBirthYear() + ".\n");
        return sb.toString();
    }

    @Override
    // Actors are equal if they have the same name
    public boolean equals(Object obj){
        boolean bool = false;
        if(obj instanceof Actor){
            Actor otrActor = (Actor) obj;
            if(this.getaName().equalsIgnoreCase(otrActor.getaName()) &&
                    this.hashCode() == otrActor.hashCode()){
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public int hashCode(){
        int hash = 31;
        hash = hash + (this.aName == null? 0 : this.aName.hashCode());
        return hash;
    }
}
