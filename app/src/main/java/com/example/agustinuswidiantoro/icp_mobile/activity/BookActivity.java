package com.example.agustinuswidiantoro.icp_mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.agustinuswidiantoro.icp_mobile.R;
import com.example.agustinuswidiantoro.icp_mobile.adapter.BookAdapter;
import com.example.agustinuswidiantoro.icp_mobile.model.DataBook;
import com.example.agustinuswidiantoro.icp_mobile.util.HttpHandler;
import com.example.agustinuswidiantoro.icp_mobile.util.SessionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookActivity extends AppCompatActivity {

    ArrayList<DataBook> dataBooks;
    ListView listView;

    private String TAG = BookActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    // URL to get books JSON
    String url = "http://test.incenplus.com:5000/books?token=";
    SessionUtils session;

    Button input_book;

    RecyclerView mBook;
    BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        input_book = (Button) findViewById(R.id.btninput_book);
        input_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InsertBookActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        // Session class instance
        session = new SessionUtils(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        String token = user.get(SessionUtils.KEY_TOKEN);

        Log.e(TAG, "Session token: " + token);

        dataBooks = new ArrayList<>();

        new GetBooks().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetBooks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BookActivity.this);
            pDialog.setMessage("Please wait...");
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

            Log.e(TAG, "Response from url book : " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject data = jsonObj.getJSONObject("data");

                    // Getting JSON Array node
                    JSONArray books = data.getJSONArray("books");

                    // looping through All Contacts
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject c = books.getJSONObject(i);

                        String createdAt = c.getString("createdAt");
                        String description = c.getString("description");
                        String name_book = c.getString("name");
                        String id_book = c.getString("id");

                        JSONObject by = c.getJSONObject("createdBy");
                        String fullname = by.getString("fullname");

                        Log.e(TAG, "Book list : " + createdAt + " , " + description
                                + " , " + name_book + " , " + fullname + " , " + id_book);

                        // tmp hash map for single contact
//                        HashMap<String, String> book = new HashMap<>();
                        DataBook book = new DataBook(id_book, createdAt, fullname, name_book, description);

                        // adding each child node to HashMap key => value
////                        book.put("id", id_book);
//                        book.put("fullname", fullname);
//                        book.put("id", id_book);
//                        book.put("description", description);
//                        book.put("name", name_book);

                        // adding book to book list
                        dataBooks.add(book);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error 1: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error 2: " + e.getMessage(),
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

            // Setup and Handover data to recyclerview
            mBook = (RecyclerView) findViewById(R.id.recyclerBook);
            mAdapter = new BookAdapter(BookActivity.this, dataBooks);
            mBook.setAdapter(mAdapter);
            mBook.setLayoutManager(new LinearLayoutManager(BookActivity.this));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Membaca file menu dan menambahkan isinya ke action bar jika ada.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miProfile:
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
