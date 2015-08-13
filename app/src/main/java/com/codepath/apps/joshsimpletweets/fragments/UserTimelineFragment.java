package com.codepath.apps.joshsimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.models.Tweet;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;
import com.codepath.apps.joshsimpletweets.utilities.TwitterUtilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by josh on 8/14/2015.
 */
public class UserTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), R.string.no_network_string, Toast.LENGTH_SHORT).show();
            //show cached tweets
            // Construct adapter plugging in the array source
            // Query ActiveAndroid for list of data
//            List<Tweet> queryResults = new Select().from(Tweet.class)
//                    .orderBy("CreatedAt ASC").limit(100).execute();
//            // Load the result into the adapter using `addAll`
//            addAll(queryResults,true);

        } else {
            setClear(true);
            setMaxId(TwitterConstants.DEFAULT_MAX_ID);
            populateTimeline(false);
        }
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void fetchTimelineAsync(int page) {
        setClear(true);
        setMaxId(TwitterConstants.DEFAULT_MAX_ID);
        populateTimeline(true);

    }

    @Override
    public void populateTimeline(final boolean swipeRefresh) {
        String screenname = getArguments().getString("screen_name", "");
        if(!swipeRefresh){
            showProgressBar();
        }else {
            setMaxId(TwitterConstants.DEFAULT_MAX_ID);  //swipe refresh true so reset
        }
        if(swipeRefresh)
            setClear(true);
        else if (getMaxId() != TwitterConstants.DEFAULT_MAX_ID)
            setClear(false);
        TwitterUtilities.getRestClient().getUserTimeline(screenname, getMaxId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(!swipeRefresh)
                    hideProgressBar();
                else
                    swipeContainer.setRefreshing(false);

                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response), isClear());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(!swipeRefresh)
                    hideProgressBar();
                else
                    swipeContainer.setRefreshing(false);

                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getActivity(), R.string.no_tweets, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void customLoadMoreDataFromApi(int offset, int total) {
        if (offset>2)
            return;
        populateTimeline(false);
//        String screenname = getArguments().getString("screen_name", "");
//        showProgressBar();
//        TwitterUtilities.getRestClient().getUserTimelineScroll(screenname, getMaxId(), new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                hideProgressBar();
//                Log.d("DEBUG", response.toString());
//                addAll(Tweet.fromJSONArray(response), false);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                hideProgressBar();
//                Log.d("DEBUG", errorResponse.toString());
//                Toast.makeText(getActivity(), R.string.no_tweets, Toast.LENGTH_SHORT).show();
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
    }
}

