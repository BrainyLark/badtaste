package com.archer.badtaste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private int userid;
    private String username;
    public ListAdapter listAdapter;
    private ListView listView;
    private int currentItem;

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

    public void setCurrentItem(int item) {
        currentItem = item;
    }

    public void contribute(float rating, long itemId) {
        Bundle bundle = new Bundle();
        bundle.putLong("userId", (long)userid);
        bundle.putDouble("rating", (double)rating);
        bundle.putLong("itemId", itemId);
        ContributeTask contributeTask = new ContributeTask();
        contributeTask.execute(bundle);
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

    class ContributeTask extends AsyncTask<Bundle, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Bundle... bundles) {
            JSONObject data = new JSONObject();
            HttpURLConnection urlConn;
            try {
                data.put("userId", bundles[0].getLong("userId"));
                data.put("rating", bundles[0].getDouble("rating"));
                data.put("itemId", bundles[0].getLong("itemId"));
                String json = data.toString();

                URL mUrl = new URL("http://192.168.1.7:8080/contribute");
                urlConn = (HttpURLConnection) mUrl.openConnection();
                urlConn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestMethod("POST");
                OutputStream os = urlConn.getOutputStream();
                os.write(json.getBytes());
                os.close();

                InputStream in = new BufferedInputStream(urlConn.getInputStream());

                String result = IOUtils.toString(in, "UTF-8");
                JSONObject jsonObject = new JSONObject(result);
                return jsonObject.getBoolean("success");

            } catch (JSONException e) {

            } catch (ProtocolException e) {
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(success) {
                listAdapter.getMovies().remove(currentItem);
                listAdapter.notifyDataSetChanged();
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Таны үнэлгээ амжилттай хадгалагдлаа! Хувь нэмрээ оруулсан танд баярлалаа!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Уучлаарай, таны үнэлгээ ороход алдаа гарлаа!", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }
    }

}
