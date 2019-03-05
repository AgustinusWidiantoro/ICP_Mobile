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
import java.util.HashMap;

public class InsertBookActivity extends AppCompatActivity {

    EditText input_name, input_description;
    String nameBook, descriptionBook;
    Button btn_insert;

    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    SessionUtils session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertbook);

        input_name = (EditText) findViewById(R.id.input_namebook);
        input_description = (EditText) findViewById(R.id.input_description);

        // Session Manager
        session = new SessionUtils(getApplicationContext());

        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new insertBook().execute();

            }
        });


    }

    private class insertBook extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InsertBookActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {

            // Get user defined values
            nameBook = input_name.getText().toString();
            descriptionBook = input_description.getText().toString();

//            if (Username.trim().length() > 0 && Password.trim().length() > 0) {
//                if (Username.equals("icp") && Password.equals("rahasia")) {

            // Create data variable for sent values to server
            String data = null;
            try {
                data = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(nameBook, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                data += "&" + URLEncoder.encode("description", "UTF-8") + "="
                        + URLEncoder.encode(descriptionBook, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String text = "";
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {

                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String token = user.get(SessionUtils.KEY_TOKEN);

                // Defined URL  where to send data
                URL url = new URL("http://test.incenplus.com:5000/books/insert?token=" + token);

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

            Log.e("Json insert ", String.valueOf(sb));

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

            startActivity(new Intent(getApplicationContext(), BookActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();

        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        startActivity(new Intent(getApplicationContext(), BookActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

        super.onBackPressed();  // optional depending on your needs
    }
}
