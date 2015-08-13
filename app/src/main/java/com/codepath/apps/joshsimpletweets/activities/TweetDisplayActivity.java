package com.codepath.apps.joshsimpletweets.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.models.Tweet;
import com.codepath.apps.joshsimpletweets.utilities.TwitterUtilities;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class TweetDisplayActivity extends ActionBarActivity {
    private ImageView ivDetailProfileImage;
    private TextView tvDetailName;
    private TextView tvDetailUserName;
    private TextView tvDetailBody;
  //  private TextView tvDetailDesc;
    private TextView tvDetailDate;
    private Tweet tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_display);

        tweet = (Tweet) getIntent().getParcelableExtra("tweet_selected");

        getSupportActionBar().setTitle(R.string.home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.blue)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ivDetailProfileImage = (ImageView) findViewById(R.id.ivDetailProfileImage);
        tvDetailName = (TextView) findViewById(R.id.tvDetailName);
        tvDetailBody = (TextView) findViewById(R.id.tvDetailBody);
        tvDetailUserName = (TextView) findViewById(R.id.tvDetailUserName);
        tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
       // tvDetailDesc = (TextView) findViewById(R.id.tvDetailDesc);

        getDetails();


    }

    private void getDetails(){
        TwitterUtilities.getRestClient().showTweet(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // ...the data has come back, add new items to your adapter...
                tvDetailUserName.setText("@" + tweet.getUser().getScreenName());
                tvDetailDate.setText(tweet.getCreatedAt());
                tvDetailName.setText(tweet.getUser().getName());
                try {
                   // tvDetailDesc.setText(response.getString("description"));
                    tvDetailBody.setText(response.getString("text"));

                } catch (JSONException e) {
                    e.printStackTrace();
                };
                Picasso.with(getBaseContext()).load(tweet.getUser().getProfileImageUrl()).into(ivDetailProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getBaseContext(), R.string.no_network_string, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
