package com.example.ronak.gadzt_app_listview;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Product_Activity extends AppCompatActivity {

    String jsonStr;
    private TextView title;
    private TextView gener;
    private TextView price;
    private TextView dis;
    private PhotoView photo_view1;

    URL url;
    Bitmap bmp = null;

    String intent_message;

    ArrayList<String> product_details = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        title = (TextView) findViewById(R.id.product_title);
        price = (TextView)findViewById(R.id.product_price);
        dis = (TextView)findViewById(R.id.product_details);
        gener = (TextView)findViewById(R.id.product_gener);
        photo_view1 = (PhotoView)findViewById(R.id.photo_view_1);

        Bundle bundle = getIntent().getExtras();
        intent_message = bundle.getString("message");

        new json_responce_thread().execute();

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view_1);
        photoView.setImageResource(R.drawable.sample1);

        //title.setText(intent_message);
    }

    protected class json_responce_thread extends AsyncTask<Object, Object, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Product_Activity.this);
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
                            JSONObject c = response.getJSONObject(Integer.parseInt(intent_message));

                            product_details.add(c.getString("ranking"));
                            product_details.add(c.getString("name"));
                            product_details.add(c.getString("gener"));
                            product_details.add(c.getString("discr"));
                            product_details.add(c.getString("web_site"));
                            product_details.add(c.getString("img_1").replace("\\",""));
                            product_details.add(c.getString("img_2"));
                            product_details.add(c.getString("price"));

                        try {
                            url = new URL(product_details.get(5));
                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            System.out.println(product_details);
            title.setText(product_details.get(1));
            gener.setText(product_details.get(2));
            dis.setText(product_details.get(3));
            Toast.makeText(Product_Activity.this,product_details.get(5),Toast.LENGTH_SHORT).show();
            photo_view1.setImageBitmap(bmp);
            price.setText(product_details.get(7));

        }
    }




}
