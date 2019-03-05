package com.example.agustinuswidiantoro.icp_mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.util.HttpHandler;
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

public class UpdateBookActivity extends AppCompatActivity {

    EditText edit_name, edit_description;
    String idBook, nameBook, descriptionBook;
    Button btn_update;

    private String TAG = UpdateBookActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    SessionUtils session;

    // URL to get contacts JSON
    String url = "http://test.incenplus.com:5000/books/edit?token=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatebook);

        // Session Manager
        session = new SessionUtils(getApplicationContext());

        edit_name = (EditText) findViewById(R.id.edit_namebook);
        edit_description = (EditText) findViewById(R.id.edit_description);


        // Set edittext
        Intent intent = getIntent();
        //get the attached extras from the intent
        //we should use the same key as we used to attach the data.
        final String id = intent.getStringExtra("Id_Book");
        final String name = intent.getStringExtra("Name_Book");
        final String description = intent.getStringExtra("Description_Book");

        edit_name.setText(name);
        edit_description.setText(description);

        // Get user defined values
        idBook = id;

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetUpdateBook().execute();

//                Toast.makeText(UpdateBookActivity.this, idBook, Toast.LENGTH_LONG).show();

            }
        });

    }

    private class GetUpdateBook extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UpdateBookActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {


            nameBook = edit_name.getText().toString();
            descriptionBook = edit_description.getText().toString();

            // Create data variable for sent values to server
            String data = null;
            try {
                data = URLEncoder.encode("id", "UTF-8")
                        + "=" + URLEncoder.encode(idBook, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                data += "&" + URLEncoder.encode("name", "UTF-8")
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

            Log.e("Data ", String.valueOf(data));

            String text = "";
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {

                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String token = user.get(SessionUtils.KEY_TOKEN);

                // Defined URL  where to send data
                URL url = new URL("http://test.incenplus.com:5000/books/edit?token=" + token);

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

            Log.e("Json update ", String.valueOf(sb));

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
