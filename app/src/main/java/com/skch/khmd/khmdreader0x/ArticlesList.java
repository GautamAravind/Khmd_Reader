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
import com.android.volley.toolbox.Volley;
import com.skch.khmd.khmdreader0x.model.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Synopsis
            Volley Singleton class set up to provide 2 objects - requestqueue and image loader
            Request Queue in getJson() downloads Json directly, thus no need to convert string to json
            parseJson() unchanged
            ImageDownloderTask shifted to CustomListAdapter
            updateList() unchanged
*/

public class ArticlesList extends ActionBarActivity {

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
    public void getJson(){

        String url = "http://pipes.yahoo.com/pipes/pipe.run?_id=b7836ddf37201097635727c10845d841&_render=JSON";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //if valid json, parse it
                parseJson(response);
                //update the listview
                updateList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("onResponse", "error getting JSON");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this); //Currently using an on demand queue, but can be integrated with Singleton

        queue.add(req);
    }


    public void parseJson(JSONObject json) {
        JSONObject items = null;
        if(json == null) {
           // Log.i("ParseJson","Json Null");
            return;
        }
        try {
            //Log.i("parse Json", "inside try");

            // parsing json object
            if (json.getInt("count") > 0) {
                items = json.getJSONObject("value");
                JSONArray posts = items.getJSONArray("items");
                //Log.i("parse Json", "parsing items");

                feedList = new ArrayList<FeedItem>();
                //Log.d("Article Count", Integer.toString(posts.length()));
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

                   // Log.i("parse Json", "parsing each item");

                    if (post.has("media:thumbnail") && !post.isNull("media:thumbnail")) {
                        JSONObject thumb = post.getJSONObject("media:thumbnail");
                        item.setAttachmentUrl(thumb.getString("url"));
                    } else {
                        item.setAttachmentUrl("http://lh3.googleusercontent.com/-L-SGKiNt3ZQ/VJBN4QRpmYI/AAAAAAAAMm8/1CgVgSu9vL4/s72-c/blogger-image--823560845.jpg");
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

/** / To Do:                                                                            /**/
/** / in getJSONFromUrl() : Setup request queue for JSON Object Directly /**/
/** /                       If json found in cache, skip to updateList() and do not execute parseJson() /**/
/** / in parseJson() : Parse the new JSON Object Created /**/
/** / in UpdateList() : Fill up feedlist /**/
