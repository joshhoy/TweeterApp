package com.codepath.apps.joshsimpletweets.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.models.User;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;
import com.codepath.apps.joshsimpletweets.utilities.TwitterUtilities;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends ActionBarActivity {

    private ImageView ivUserProfilePic;
    private TextView tvScreenName;
    private EditText etComposeTweet;
    private TextView tvFirstName;
    private TextView tvTextChar;
    private TextWatcher mTextEditorWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.blue)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ivUserProfilePic = (ImageView) findViewById(R.id.ivUserProfilePic);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        etComposeTweet = (EditText) findViewById(R.id.etComposeText);
        tvTextChar = (TextView) findViewById(R.id.tvTextChar);
        updateUserName();
        tvTextChar.setText(TwitterConstants.MAX_FIELD_LENGTH);

        mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvTextChar.setText("140");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvTextChar.setText(String.valueOf(Integer.valueOf(TwitterConstants.MAX_FIELD_LENGTH)-s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        etComposeTweet.addTextChangedListener(mTextEditorWatcher);
        etComposeTweet.setFocusable(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tweet) {
            postToTwitter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUserName() {
        TwitterUtilities.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User auth_user = User.fromJSON(response);
                tvScreenName.setText("@" + auth_user.getScreenName());
                tvFirstName.setText(auth_user.getName().toString());
                Picasso.with(getBaseContext()).load(auth_user.getProfileImageUrl()).into(ivUserProfilePic);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getBaseContext(), R.string.no_user_string, Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void postToTwitter() {
        String tweet = etComposeTweet.getText().toString();
        TwitterUtilities.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getBaseContext(), R.string.posted_tweet, Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


}
