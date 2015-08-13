package com.codepath.apps.joshsimpletweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.adapters.TweetArrayAdapter;
import com.codepath.apps.joshsimpletweets.models.Tweet;
import com.codepath.apps.joshsimpletweets.scrolllistener.EndlessScrollListener;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta on 5/29/2015.
 */
public abstract class TweetsListFragment extends Fragment {
    public List<Tweet> tweets;
    public ListView lvTweets;
    private ProgressBar progressBarFooter;
    public TweetArrayAdapter aTweets;
    private String max_id = TwitterConstants.DEFAULT_MAX_ID;
    //private String since_id;
    public SwipeRefreshLayout swipeContainer;
    private boolean clear = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        //create the arraylist
        tweets = new ArrayList<>();
        //construct adapter from data source

        setupListWithFooter(v);
        //connect adapter
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
        aTweets.clear();
        lvTweets.setAdapter(aTweets);
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page, totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void addAll(List<Tweet> list, boolean clear) {
        if (aTweets == null )
            return;
        if (clear) {
            aTweets.clear();
            aTweets.notifyDataSetChanged();
        } else {
            if(list.size() == 0)
                return;
            list.remove(0);
        }
        if(list.size() == 0)
            return;

        //List<Tweet> list = Tweet.fromJSONArray(response);
        //if clear is false then scrolling so remove first tweet since repeated
        aTweets.addAll(list);
        int lastTweet = list.size() - 1;
        max_id = String.valueOf(list.get(lastTweet).getUid());
        aTweets.notifyDataSetChanged();
        //since_id = String.valueOf(list.get(0).getUid());//list.get(0);
        //save to database
        saveTweetsOfflineStorage(list, clear);
    }

    private void saveTweetsOfflineStorage(List<Tweet> tweetsList, boolean clear) {

        if (clear) {
            // Deleting all earlier items
//            Tweet item = Tweet.load(Tweet.class, 1);
//            item.delete();
//            User user = User.load(User.class,2);
//            user.delete();
// or with
            /*
            List<Tweet> queryResults = new Select().from(Tweet.class)
                    .execute();
            for (Tweet t : queryResults) {
                new Delete().from(Tweet.class).where("uid = ?", t.getUid()).execute();
            }
            List<User> userResults = new Select().from(User.class).execute();
            for (User u : userResults) {
                new Delete().from(User.class).where("uid = ?", u.getUid()).execute();
            }
            */
        }/*
        for (Tweet tweet : tweetsList) {
            // Create a category
            User user = tweet.getUser();
            user.save();

            // Create an item
            tweet.save();
        }*/
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Adds footer to the list default hidden progress
    public void setupListWithFooter(View v) {
        // Find the ListView
        // Inflate the footer
        if (progressBarFooter == null) {
            View footer = getActivity().getLayoutInflater().inflate(
                    R.layout.footer_progress, null);
            // Find the progressbar within footer
            progressBarFooter = (ProgressBar)
                    footer.findViewById(R.id.pbFooterLoading);
            // Add footer to ListView before setting adapter
            lvTweets.addFooterView(footer);
        }

    }

    // Show progress
    public void showProgressBar() {
        if (progressBarFooter != null) {
            progressBarFooter.setVisibility(View.VISIBLE);
        }

    }

    // Hide progress
    public void hideProgressBar() {
        if (progressBarFooter != null) {
            progressBarFooter.setVisibility(View.GONE);
        }
    }

    //to be overridden in fragments
    public abstract void fetchTimelineAsync(int page);

    public abstract void populateTimeline(boolean swipeRefresh);

    public abstract void customLoadMoreDataFromApi(int offset, int total);

    //getter setters
    public String getMaxId() {
        return max_id;
    }

    public void setMaxId(String max_id) {
        this.max_id = max_id;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }
}
