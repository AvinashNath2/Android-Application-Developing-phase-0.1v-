package com.example.ronak.gadzt_app_listview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movieList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    String jsonStr;

    ArrayList<String> list_name = new ArrayList<String>();
    ArrayList<String> list_gener = new ArrayList<String>();
    ArrayList<String> list_price =new ArrayList<String>();
    ArrayList<String> list_link = new ArrayList<String>();

    public ArrayList<String> intent_list = new ArrayList<String>();
    private static final String TAG = "MyApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        new json_responce_thread().execute();

        //https://stackoverflow.com/questions/24471109/recyclerview-onclick
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        /*  intent_list.add(list_name.get(position));
                        intent_list.add(list_dis.get(position));
                        intent_list.add(list_price.get(position)); */
                        Intent i = new Intent(getApplicationContext(),Product_Activity.class);
                        i.putExtra("message",Integer.toString(position));
                        startActivity(i);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    protected class json_responce_thread extends AsyncTask<Object, Object, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        String name;

        @Override
        protected Void doInBackground(Object... params) {

            String str = "https://avinashnath2.000webhostapp.com/product_server.php";
            HttpURLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = (HttpURLConnection) url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                //temp = line.toString();
                jsonStr = stringBuilder.toString();

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray response = jsonObj.getJSONArray("server_res");

                        // looping through All Contacts
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject c = response.getJSONObject(i);

                            list_name.add(c.getString("name"));
                            list_gener.add(c.getString("gener"));
                            list_price.add(c.getString("price"));
                            list_link.add(c.getString("product_link"));
                        }
                    } catch (final JSONException e) {
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



                bufferedReader.close();
                urlConn.disconnect();
                return null;

            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Void response) {
           // Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
            if (pDialog.isShowing())
                pDialog.dismiss();

            Movie movie = new Movie("Goldfinger", "1965","text");
            movieList.add(movie);

            for (int i = 0; i < list_name.size(); i++) {

                movie = new Movie(list_name.get(i), list_gener.get(i),list_price.get(i));
                movieList.add(movie);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

}
