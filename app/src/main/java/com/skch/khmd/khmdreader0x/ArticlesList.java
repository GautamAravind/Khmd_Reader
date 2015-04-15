package com.skch.khmd.khmdreader0x;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.skch.khmd.khmdreader0x.model.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ArticlesList extends ActionBarActivity {



    /*
    *
    *           Redundant Activity. To be used if using fragments and if everything is a child of Main Activity
    *
    * */

    private Toolbar toolbar;
    private ArrayList<FeedItem> feedList = null;
    private ProgressBar progressbar = null;
    private ListView feedListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        getJson();
        //moved parse function to getJson

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    public void getJson() {

        String url = "http://pipes.yahoo.com/pipes/pipe.run?_id=b7836ddf37201097635727c10845d841&_render=JSON";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //if valid json, parse it
                parseJson(response);
                updateList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("onResponse", "error getting JSON");
            }
        });
        //RequestQueue queue = Volley.newRequestQueue(this); //Currently using an on demand queue, but can be integrated with Singleton
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        queue.add(req);
    }


    public void parseJson(JSONObject json) {
        JSONObject items = null;
        if (json == null) {

            return;
        }
        try {

            if (json.getInt("count") > 0) {
                items = json.getJSONObject("value");
                JSONArray posts = items.getJSONArray("items");


                feedList = new ArrayList<FeedItem>();

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    FeedItem item = new FeedItem();
                    JSONObject author = post.getJSONObject("author");
                    JSONObject content = post.getJSONObject("content");
                    item.setTitle(post.getString("title"));
                    item.setAuthor(author.getString("name"));
                    item.setId(post.getString("id"));
                    item.setUrl(post.getString("link"));
                    item.setContent(content.getString("content"));


                    if (post.has("media:thumbnail") && !post.isNull("media:thumbnail")) {
                        JSONObject thumb = post.getJSONObject("media:thumbnail");
                        item.setAttachmentUrl(thumb.getString("url"));
                    } else {
                        item.setAttachmentUrl(null);

                    }


                    feedList.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateList() {
        feedListView = (ListView) findViewById(R.id.custom_list);
        feedListView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);

        feedListView.setAdapter(new CustomListAdapter(this, feedList));
        feedListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = feedListView.getItemAtPosition(position);
                FeedItem newsData = (FeedItem) o;

                Intent intent = new Intent(ArticlesList.this, FeedDetails.class);

                intent.putExtra("feed", newsData);
                startActivity(intent);
            }
        });
    }


}



