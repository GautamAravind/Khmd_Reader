package com.skch.khmd.khmdreader0x;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.skch.khmd.khmdreader0x.model.FeedItem;


public class FeedDetails extends ActionBarActivity {
                
    /*
    Started from ArticleList.java 
                        -> (AsyncTask) DownLoadFilesTask().execute(url)
                        -> onPostExecute();
                        -> parseJson(json);
                        -> updateList(); 
    */

    private Toolbar toolbar;
    private FeedItem feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        feed = (FeedItem) this.getIntent().getSerializableExtra("feed");
        // Gets intent from ArticleList.java -> updateList();

        if (feed != null) {
            ImageView thumb = (ImageView) findViewById(R.id.featuredImg);
            //new ImageDownloaderTask(thumb).execute(feed.getAttachmentUrl());
            // AsyncTask to download image AFTER it opens FeedDetails and set in top bar

            TextView title = (TextView) findViewById(R.id.title);
            title.setText(feed.getTitle());

            TextView htmlTextView = (TextView) findViewById(R.id.content);
            htmlTextView.setText(Html.fromHtml(feed.getContent(), null, null));
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(feed.getTitle());


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

/** / To Do                                                                            /**/
/** / Change size of image view to display properly /**/
/** / Remove title text view from below image /**/
/** / Edit Thumbnail Logic : If thumbnail found, display /**/
/** /                        Else set visibility of image view to GONE    /**/