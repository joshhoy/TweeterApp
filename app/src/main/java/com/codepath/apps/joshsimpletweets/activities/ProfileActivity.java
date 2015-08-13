package com.codepath.apps.joshsimpletweets.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.fragments.FollowersListFragment;
import com.codepath.apps.joshsimpletweets.fragments.FriendsListFragment;
import com.codepath.apps.joshsimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.joshsimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.joshsimpletweets.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ProfileActivity extends ActionBarActivity {
    private User user;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvTagline;
//    private TextView tvFollowersCount;
//    private TextView tvFriendsCount;
    private RelativeLayout rlUserHeader;

    private ViewPager vpPager;
    private ProfilePagerAdapter vpAdapter;
    private PagerSlidingTabStrip tabStrip;
    private Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("DEBUG", "*******************DONE");
            //rlUserHeader.setBackgroundColor(getResources().getColor(R.color.blue));
            rlUserHeader.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            Log.d("DEBUG", "*******************FAILED");
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {
            Log.d("DEBUG", "*********************Prepare Load");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.blue)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
//        tvFollowersCount = (TextView) findViewById(R.id.tvFollowers);
//        tvFriendsCount = (TextView) findViewById(R.id.tvFriends);
        rlUserHeader = (RelativeLayout) findViewById(R.id.rlUserHeader);

        user = getIntent().getParcelableExtra("user_selected");
        if(user == null){
            Toast.makeText(getBaseContext(), R.string.no_user_string, Toast.LENGTH_SHORT).show();
        } else {
            getSupportActionBar().setTitle("@" + user.getScreenName());
            populateProfileHeader(user);

            //get the viewpager
            vpPager = (ViewPager) findViewById(R.id.vpProfile);


            //set the viewpager adapter fr the pager
            vpAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
            vpPager.setAdapter(vpAdapter);
            vpAdapter.setUser(user);

            //find the sliding tabstrip
            tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabsProfile);

            //attavh tabstrip to the viewpager
            tabStrip.setViewPager(vpPager);

        }

        //String screenName = getIntent().getStringExtra("screen_name");
        //create the user timeline fragment
        //if(savedInstanceState == null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            UserTimelineFragment frag = UserTimelineFragment.newInstance(screenName);
//            ft.replace(R.id.flContainer, frag);
//            ft.commit();

    }

    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagLine());
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

//        tvFriendsCount.setText(Integer.toString(user.getFriendsCount()) + " Followers");
//        tvFollowersCount.setText(Integer.toString(user.getFollowersCount()) + " Friends");

        Picasso.with(this).load(user.getProfileBkgdImageUrl()).into(target);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    //return the order of fragments in the view pager
    public class ProfilePagerAdapter extends FragmentPagerAdapter {
        private String tabtitles[] = {"Tweets","Friends","Followers"};
        private UserTimelineFragment hf;
        private User user;

        public ProfilePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                hf =  UserTimelineFragment.newInstance(user.getScreenName());
                return hf;
            } else if(position == 1){
                return FriendsListFragment.newInstance(user.getScreenName()) ;
            } else if(position == 2) {
                return FollowersListFragment.newInstance(user.getScreenName());
            } else
                return null;
        }

        @Override
        public int getCount() {
            return tabtitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String number="";
            if (position == 1)
                number = ""+user.getFriendsCount();
            else if(position == 2)
                number = ""+user.getFollowersCount();
            return number + " " + tabtitles[position];
        }

        public TweetsListFragment getRegisteredFragment(int i) {
            if(i==0)
                return hf;
            return null;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
