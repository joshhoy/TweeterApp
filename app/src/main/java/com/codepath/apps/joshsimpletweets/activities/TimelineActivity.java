package com.codepath.apps.joshsimpletweets.activities;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.joshsimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.joshsimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.joshsimpletweets.models.User;
import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;
import com.codepath.apps.joshsimpletweets.utilities.TwitterUtilities;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity {
   // private TweetsListFragment listFragment;
    private ViewPager vpPager;
    private TweetsPagerAdapter vpAdapter;
    private PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setTitle(R.string.home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.blue)));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setLogo(R.drawable.ic_bird1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        if(savedInstanceState == null) {
//            listFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.homeTweetsList);
//        }

        //get the viewpager
         vpPager = (ViewPager) findViewById(R.id.viewpager);


        //set the viewpager adapter fr the pager
        vpAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(vpAdapter);

        //find the sliding tabstrip
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //attavh tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_compose_tweet) {
            //launch a second age form activity
            onComposeTweet();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onComposeTweet() {
        Toast.makeText(this, "Compose tweet", Toast.LENGTH_SHORT).show();
        Intent composeIntent = new Intent(this, ComposeTweetActivity.class);
        startActivityForResult(composeIntent, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == this.RESULT_OK && requestCode==20){
            vpAdapter.getRegisteredFragment(0).setMaxId(TwitterConstants.DEFAULT_MAX_ID);
            vpAdapter.getRegisteredFragment(0).setClear(true);
            vpAdapter.getRegisteredFragment(0).populateTimeline(false);

            //((MentionsTimelineFragment)vpAdapter.getItem(1)).populateTimeline();


        }
    }


    public void onProfileView(MenuItem v) {
        final Intent profileIntent = new Intent(this, ProfileActivity.class);
        TwitterUtilities.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJSON(response);
                profileIntent.putExtra("user_selected", user);
                startActivityForResult(profileIntent, 21);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getBaseContext(), R.string.no_user_string, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

//    public void displayUserProfile(View view) {
//        view.get
//        Intent i = new Intent(this, ProfileActivity.class);
//        i.putExtra("user_selected", vpAdapter.getRegisteredFragment(0).getTweet().getUser());   //either be serializable or parcelable
//        startActivity(i);
//    }


    //return the order of fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabtitles[] = {"Home","Mentions"};
        HomeTimelineFragment hf;
        MentionsTimelineFragment mf;

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                if(hf == null)
                    hf = new HomeTimelineFragment();
                return hf;
            }else if(position == 1) {
                if(mf==null)
                    mf = new MentionsTimelineFragment();
                return mf;
            }else
                return null;
        }

        @Override
        public int getCount() {
            return tabtitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles[position];
        }

        public TweetsListFragment getRegisteredFragment(int i) {
            if(i==0)
                return hf;
            else if(i==1)
                return mf;
            return null;
        }
    }
}
