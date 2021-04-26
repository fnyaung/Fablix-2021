/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User {

    private final String username;
    private int userID;

    public User(String username) {

        this.username = username;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return userID;
    }


}
