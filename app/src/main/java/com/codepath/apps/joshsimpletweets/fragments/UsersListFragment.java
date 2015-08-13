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
import android.widget.Toast;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.adapters.UserArrayAdapter;
import com.codepath.apps.joshsimpletweets.models.User;
import com.codepath.apps.joshsimpletweets.scrolllistener.EndlessScrollListener;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta on 5/31/2015.
 */
public abstract class UsersListFragment extends Fragment {
    public List<User> users;
    public ListView lvUsers;
    private ProgressBar progressBarFooter;
    public UserArrayAdapter aUsers;
    private long next_cursor = TwitterConstants.DEFAULT_CURSOR;
    public SwipeRefreshLayout swipeContainerUsers;
    private boolean clear = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users_list, parent, false);
        lvUsers = (ListView) v.findViewById(R.id.lvUsers);
        //create the arraylist
        users = new ArrayList<>();
        //construct adapter from data source
        aUsers = new UserArrayAdapter(getActivity(),users);
        aUsers.clear();
        setupListWithFooter(v);


        // Attach the listener to the AdapterView onCreate
        lvUsers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page, totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
//        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getActivity(), TweetDisplayActivity.class);
//                Tweet result = users.get(position);
//                i.putExtra("tweet_selected", result);   //either be serializable or parcelable
//                startActivity(i);
//            }
//
//        });

        swipeContainerUsers = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainerUsers);
        // Setup refresh listener which triggers new data loading
        swipeContainerUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainerUsers.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return v;
    }

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
            setNextCursor(TwitterConstants.DEFAULT_CURSOR);
            populateTimeline(false);
        }

    }

    public void addAll(List<User> list,boolean clear){
        if(aUsers == null)
            return;
        if(clear)
            aUsers.clear();
        //List<Tweet> list = Tweet.fromJSONArray(response);
        aUsers.addAll(list);
        int lastTweet = list.size() - 1;
        //max_id = String.valueOf(list.get(lastTweet).getUid());
        //since_id = String.valueOf(list.get(0).getUid());//list.get(0);
        //save to database
        // saveTweetsOfflineStorage(list, true);
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
        //ListView lvItems = (ListView) v.findViewById(R.id.lvUsers);
        // Inflate the footer
        if(progressBarFooter==null) {
            View footer = getActivity().getLayoutInflater().inflate(
                    R.layout.footer_progress, null);
            // Find the progressbar within footer
            progressBarFooter = (ProgressBar)
                    footer.findViewById(R.id.pbFooterLoading);
            // Add footer to ListView before setting adapter
            lvUsers.addFooterView(footer);
        }
        //connect adapter
        lvUsers.setAdapter(aUsers);
    }

    // Show progress
    public void showProgressBar() {
        if(progressBarFooter!=null) {
            progressBarFooter.setVisibility(View.VISIBLE);
        }

    }

    // Hide progress
    public void hideProgressBar() {
        if(progressBarFooter!=null) {
            progressBarFooter.setVisibility(View.GONE);
        }
    }

    //getter setters
    public long getNextCursor() {
        return next_cursor;
    }

    public void setNextCursor(long nextCursor) {
        this.next_cursor = nextCursor;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public abstract void customLoadMoreDataFromApi(int offset, int total);
    public abstract void populateTimeline(boolean swipeRefresh);
    public abstract void fetchTimelineAsync(int page);

}
