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
public class HomeTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), R.string.no_network_string, Toast.LENGTH_SHORT).show();
            //show cached tweets
            // Construct adapter plugging in the array source
            // Query ActiveAndroid for list of data
          /*  List<Tweet> queryResults = new Select().from(Tweet.class)
                    .orderBy("CreatedAt ASC").limit(100).execute();*/
             //Load the result into the adapter using `addAll`
           // addAll(queryResults,true);

        } else {
            setClear(true);
            setMaxId(TwitterConstants.DEFAULT_MAX_ID);
            populateTimeline(false);
        }

    }
    //send api req to timeline json and crate tweet obj using deserialize and create tweet
    public void populateTimeline(final boolean swipeRefresh) {
        Toast.makeText(getActivity(), "postttt", Toast.LENGTH_SHORT);

        if(!swipeRefresh){
            showProgressBar();
        }else {
            setMaxId(TwitterConstants.DEFAULT_MAX_ID);  //swipe refresh true so reset
        }
        if(swipeRefresh)
            setClear(true);
        else if (getMaxId() != TwitterConstants.DEFAULT_MAX_ID)
            setClear(false);
        TwitterUtilities.getRestClient().getHomeTimeline(getMaxId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response), isClear());
                if(!swipeRefresh)
                    hideProgressBar();
                else
                    swipeContainer.setRefreshing(false);

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

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset, int total) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        if (offset>5)
            return;
        populateTimeline(false);
    }

    public void fetchTimelineAsync(int page) {
        setClear(true);
        setMaxId(TwitterConstants.DEFAULT_MAX_ID);
        populateTimeline(true);
    }


}
