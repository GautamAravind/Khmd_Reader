package com.skch.khmd.khmdreader0x;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skch.khmd.khmdreader0x.model.FeedItem;


public class FeedDetails extends ActionBarActivity {


    private Toolbar toolbar;
    private FeedItem feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);

        feed = (FeedItem) this.getIntent().getSerializableExtra("feed");
        // Gets intent from ArticleList.java -> updateList();

        NetworkImageView thumb = (NetworkImageView) findViewById(R.id.featuredImg);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(feed.getTitle());

        TextView htmlTextView = (TextView) findViewById(R.id.content);
        htmlTextView.setText(Html.fromHtml(feed.getContent(), null, null));


        if (feed != null) {
            if (feed.getAttachmentUrl() != null) {
                thumb.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                ImageLoader loader = VolleySingleton.getInstance().getImageLoader();
                thumb.setImageUrl(feed.getAttachmentUrl(), loader);
            }
        }


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("The KHMD Blog");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feed_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                shareContent();
                return true;
            case R.id.menu_view:
                Intent intent = new Intent(FeedDetails.this, Webview.class);
                intent.putExtra("url", feed.getUrl());
                intent.putExtra("title", feed.getTitle());
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareContent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, feed.getTitle() + "\n" + feed.getUrl());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share using"));

    }

}
