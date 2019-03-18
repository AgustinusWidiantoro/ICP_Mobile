package com.example.agustinuswidiantoro.icp_mobile.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.util.HttpHandler;
import com.example.agustinuswidiantoro.icp_mobile.util.SessionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    TextView text_fullname, text_username;
    Button btn_logout;
    SessionUtils session;
    private String TAG = UserProfileActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    // URL to get contacts JSON
    String url = "http://test.incenplus.com:5000/users/me?token=";

    HashMap<String, String> user_token;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        btn_logout = (Button) findViewById(R.id.btn_logout);

        // Session class instance
        session = new SessionUtils(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        session.checkLogin();

        // get user data from session
        user_token = session.getUserDetails();

        token = user_token.get(SessionUtils.KEY_TOKEN);

        Log.e(TAG, "Session token: " + token);

        new GetUserProfile().execute();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Logout().execute();

            }
        });


    }

    private class Logout extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // get user data from session
            // Defined URL  where to send data

            HttpHandler sh = new HttpHandler();
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();
            String token = user.get(SessionUtils.KEY_TOKEN);

            // Making a request to url and getting response
            sh.makeServiceCall("http://test.incenplus.com:5000/users/logout?token=" + token);

            session.logoutUser();
            // get user data from session
            user_token = session.getUserDetails();
            String name = user_token.get(SessionUtils.KEY_TOKEN);

            Log.e(TAG, "Session clear: " + name);

            startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();

            return null;
        }
    }

    private class GetUserProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // get user data from session
            HashMap<String, String> user = session.getUserDetails();
            String token = user.get(SessionUtils.KEY_TOKEN);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + token);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject data = jsonObj.getJSONObject("data");
                    String username = data.getString("username");
                    String fullname = data.getString("fullname");

                    Log.e(TAG, "User detail: " + username + " dan " + fullname);

                    text_fullname = (TextView) findViewById(R.id.fullname);
                    text_username = (TextView) findViewById(R.id.username);

                    text_fullname.setText(fullname);
                    text_username.setText(username);

                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        startActivity(new Intent(getApplicationContext(), BookActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

        super.onBackPressed();  // optional depending on your needs
    }
}
