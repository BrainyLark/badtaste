package com.archer.badtaste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private int userid;
    private String username;
    ListAdapter listAdapter;
    ListView listView;

    public void init() {
        pd = new ProgressDialog(MainActivity.this, R.style.myTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(R.style.Widget_AppCompat_ProgressBar);
        listView = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getInt("userid");
        username = bundle.getString("username");
        Toast.makeText(this, userid + " : " + username, Toast.LENGTH_LONG).show();
    }

    public void recommend() {
        RecommendTask recommendTask = new RecommendTask();
        recommendTask.execute(userid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        recommend();
    }

    class RecommendTask extends AsyncTask<Integer, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected List<Movie> doInBackground(Integer... userid) {
            String urlString = "http://192.168.1.7:8080/recommend/" + userid[0];
            JSONArray response;
            try {
                response = (JSONArray) new JSONTokener(IOUtils.toString(new URL(urlString).openStream())).nextValue();
                List<Movie> movies = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(object.getLong("id"));
                    movie.setTitle(object.getString("title"));
                    movie.setPredicted(object.getDouble("predicted"));
                    movie.setPoster(object.getString("url"));
                    movie.setGenres(object.getString("genres"));
                    movies.add(movie);
                }
                return movies;
            } catch (JSONException e) {

            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            listAdapter = new ListAdapter(MainActivity.this, movies);
            listView.setAdapter(listAdapter);
            pd.dismiss();
        }
    }
}
