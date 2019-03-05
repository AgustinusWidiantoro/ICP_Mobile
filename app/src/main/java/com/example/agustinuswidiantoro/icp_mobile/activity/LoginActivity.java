package com.example.agustinuswidiantoro.icp_mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.util.SessionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText input_username, input_password;
    String Username, Password;
    Button btn_login;

    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    // URL to get contacts JSON
    String url = "http://test.incenplus.com:5000/users/login";

    SessionUtils session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);

        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetToken().execute();

            }
        });

        // Session Manager
        session = new SessionUtils(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        if (session.isLoggedIn() == true) {
            startActivity(new Intent(LoginActivity.this, BookActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    private class GetToken extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait login...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {

            // Get user defined values
            Username = input_username.getText().toString();
            Password = input_password.getText().toString();

//            if (Username.trim().length() > 0 && Password.trim().length() > 0) {
//                if (Username.equals("icp") && Password.equals("rahasia")) {

            // Create data variable for sent values to server
            String data = null;
            try {
                data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(Username, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(Password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String text = "";
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                // Defined URL  where to send data
                URL url = new URL("http://test.incenplus.com:5000/users/login");

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                return String.valueOf(sb);
            } catch (Exception ex) {
                Log.e(TAG, "Json parsing error: " + ex);
            }

            Log.e("Json Login", String.valueOf(sb));

//                } else {
//                    Toast.makeText(LoginActivity.this,
//                            "Login failed..\", \"Username/Password is incorrect", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(LoginActivity.this,
//                        "Login failed..\", \"Please enter username and password", Toast.LENGTH_LONG).show();
//            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.e("Result", String.valueOf(result));

            if (result != "") {
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject data = jsonObj.getJSONObject("data");
                    String token = data.getString("token");

                    JSONObject user_data = data.getJSONObject("user");
                    String username = user_data.getString("username");
                    String fullname = user_data.getString("fullname");

                    Log.e(TAG, "Token: " + token);

                    session.createLoginSession(token);

                    // Staring LoginActivity
                    Intent i = new Intent(getApplicationContext(), BookActivity.class);
                    startActivity(i);
                    finish();

                } catch (final JSONException e) {
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
        }

    }
}
