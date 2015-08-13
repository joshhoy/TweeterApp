package com.codepath.apps.joshsimpletweets.fragments;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.models.User;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;
import com.codepath.apps.joshsimpletweets.utilities.TwitterUtilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta on 5/31/2015.
 */
public  class FriendsListFragment extends UsersListFragment {

    public static FriendsListFragment newInstance(String screen_name) {
        FriendsListFragment fragmentDemo = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    //to be overridden in fragments
    public void fetchTimelineAsync(int page) {
        setClear(true);
        setNextCursor(TwitterConstants.DEFAULT_CURSOR);
        populateTimeline(true);
    }


    public void populateTimeline(final boolean swipeRefresh) {
        if(!swipeRefresh){
            showProgressBar();
        }else {
            setNextCursor(TwitterConstants.DEFAULT_CURSOR);  //swipe refresh true so reset cursor
        }
        if(swipeRefresh)
            setClear(true);
        else if (getNextCursor() != TwitterConstants.DEFAULT_CURSOR)
            setClear(false);
        String screenName = getArguments().getString("screen_name", "");
        TwitterUtilities.getRestClient().getFriendsIds(screenName, String.valueOf(getNextCursor()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                try {
                    List<Integer> listFriends = new ArrayList<Integer>();
                    JSONArray friends = response.getJSONArray("ids");
                    setNextCursor(response.getInt("next_cursor"));
                    int len = friends.length();
                    for (int i=0;i<len;i++){
                        listFriends.add(friends.getInt(i));
                    }
                    if(len == 0){
                       // Toast.makeText(getActivity(), R.string.no_friends_string, Toast.LENGTH_SHORT).show();
                        if(!swipeRefresh)
                            hideProgressBar();
                        else
                            swipeContainerUsers.setRefreshing(false);

                        return;
                    }
                    TwitterUtilities.getRestClient().getUserDetails(listFriends,new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            //super.onSuccess(statusCode, headers, response);
                            Log.d("DEBUG", response.toString());
                            if(!swipeRefresh)
                                hideProgressBar();
                            else
                                swipeContainerUsers.setRefreshing(false);
                            addAll(User.fromJSONArray(response), isClear());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            if(!swipeRefresh)
                                hideProgressBar();
                            else
                                swipeContainerUsers.setRefreshing(false);
                            Log.d("DEBUG", errorResponse.toString());
                            Toast.makeText(getActivity(), R.string.no_user_string, Toast.LENGTH_SHORT).show();
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(!swipeRefresh)
                    hideProgressBar();
                else
                    swipeContainerUsers.setRefreshing(false);
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getActivity(), R.string.no_tweets, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset, int total){
        Log.d("DEBUG", "**friends:next cursor " + getNextCursor() + "offset " + offset);
        if(getNextCursor() == 0 ) //
                return;
        populateTimeline(false);
    }
}
