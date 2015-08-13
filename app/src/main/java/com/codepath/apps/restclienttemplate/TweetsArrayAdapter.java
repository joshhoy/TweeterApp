package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.helpers.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by joshho on 8/12/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // ViewHolder pattern
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);

        tvUserName.setText("@"+tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvTime.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        //tvBody.setText(tweet.getUser().getProfileImageUrl());

        return convertView;
    }

    /*
    private void getMyInfo() {
        client.getVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                // Log.i("debug", json.toString());
                User user = User.fromJson(json);

                ivMyProfileImage.setImageResource(android.R.color.transparent);
                Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivMyProfileImage);
                tvMyScreenName.setText("@" + user.getScreenName());
                tvMyName.setText(user.getName());
            }

            @Override
            public void onFailure(int status, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }
    */
}
