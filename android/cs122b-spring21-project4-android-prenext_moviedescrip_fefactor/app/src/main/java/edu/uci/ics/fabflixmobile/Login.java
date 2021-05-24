package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private TextView message;
    private Button loginButton;

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
        setContentView(R.layout.login); // we loead the longin view
        // below are the ids that i've created
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        message = findViewById(R.id.message);
        loginButton = findViewById(R.id.login);

        //assign a listener to call a function to handle the user request when clicking a button
        // when the user clicks a loginbutton this function is called
        loginButton.setOnClickListener(view -> login());
    }

    public void login() {
        // messages is going to show
        message.setText("Trying to login");
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest loginRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/login",
                response -> {
                    String status = null;
                    String message = null;

                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    Log.d("login.success", response);
//                    System.out.println("This response " + response);
                    try {
                        JSONObject res = new JSONObject(response);
                        status = res.getString("status");
                        message = res.getString("message");
                        System.out.println("status : " + status);
                        System.out.println("message : " + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(status.equals("success")){
                        System.out.println("Verification passed");
                        // verification passed
                        // initialize the activity(page)/destination
                        // if it's successful we create a activity
                        Intent listPage = new Intent(Login.this, ListViewActivity.class);
                        // activate the list page.
                        startActivity(listPage);
                    }
                    else{
                        //verification failed
                        setLoginfail(message);
                        Log.d("login.error", message);
                        System.out.println("Verification failed");
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };


        // important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
        // add this request to the queue

    }

    public void setLoginfail(String msg){
        System.out.println("loginfailed function");
        message.setText(msg);
        loginButton.setOnClickListener(view -> login());
    }
}