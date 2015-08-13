package com.codepath.apps.joshsimpletweets.clients;

import android.content.Context;

import com.codepath.apps.joshsimpletweets.utilities.TwitterConstants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.List;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1";//https://api.twitter.com"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "h8hTzVMfhivQQ4UTNBZd581Ij";       // Change this
	public static final String REST_CONSUMER_SECRET = "5i66lhJXPSDIyBPupRLJR8jMu6vdIsLAOShSlmSq86CNh9QQBZ"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

   // each method is timeline
    //getHometimeline is to get hometimeline
    //https://api.twitter.com/1.1/statuses/home_timeline.json?count=25&since_id=1
    public void getHomeTimeline(String max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //specify the params
        RequestParams params = new RequestParams();
        params.put("count",TwitterConstants.MAX_TWEETS);
        params.put("since_id", 1);  //since the first tweet see all the tweets
        if(max_id != TwitterConstants.DEFAULT_MAX_ID)
            params.put("max_id", max_id);
        getClient().get(apiUrl, params, handler);
    }

//    public void getHomeTimelineScroll(String max_id, AsyncHttpResponseHandler handler) {
//        String apiUrl = getApiUrl("statuses/home_timeline.json");
//        //specify the params
//        RequestParams params = new RequestParams();
//        params.put("count", TwitterConstants.MAX_TWEETS);
//        params.put("max_id",max_id);  //since the first tweet see all the tweets
//        params.put("since_id",1);
//        getClient().get(apiUrl, params, handler);
//
//    }

    public void getUserCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("skip_status", 1);
        params.put("include_entities", "false");
        getClient().get(apiUrl, params, handler);
    }

    //composing a tweet
    public void postTweet(String tweetText, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetText);
        getClient().post(apiUrl, params, handler);
    }

    //statuses/show
    public void showTweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/show.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(String max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        //specify the params
        RequestParams params = new RequestParams();
        params.put("count",TwitterConstants.MAX_TWEETS);
       params.put("since_id", 1);  //since the first tweet see all the tweets
        if(max_id != TwitterConstants.DEFAULT_MAX_ID)
            params.put("max_id",max_id);
        getClient().get(apiUrl, params, handler);
    }

//    public void getMentionsTimelineScroll(String max_id, AsyncHttpResponseHandler handler) {
//        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
//        //specify the params
//        RequestParams params = new RequestParams();
//        params.put("count", TwitterConstants.MAX_TWEETS);
//        params.put("max_id",max_id);  //since the first tweet see all the tweets
//        params.put("since_id",1);
//        getClient().get(apiUrl,params,handler);
//
//    }

    public void getUserTimeline(String screenName, String max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        //specify the params
        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        params.put("count",TwitterConstants.MAX_TWEETS);
        params.put("since_id",1);  //since the first tweet see all the tweets
        if(max_id != TwitterConstants.DEFAULT_MAX_ID)
            params.put("max_id",max_id);
        getClient().get(apiUrl, params, handler);
    }

//    public void getUserTimelineScroll(String screenName, String max_id, AsyncHttpResponseHandler handler){
//        String apiUrl = getApiUrl("statuses/user_timeline.json");
//        //specify the params
//        RequestParams params = new RequestParams();
//        params.put("screen_name",screenName);
//        params.put("max_id",max_id);  //since the first tweet see all the tweets
//        params.put("count",TwitterConstants.MAX_TWEETS);
//        params.put("since_id",1);  //since the first tweet see all the tweets
//        getClient().get(apiUrl, params, handler);
//    }

    public void getFriendsIds(String screenName,  String cursor, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("friends/ids.json");
        //specify the params
        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        params.put("count", TwitterConstants.MAX_USERS);
        if(cursor!=String.valueOf(TwitterConstants.DEFAULT_CURSOR))
            params.put("cursor",cursor);

        //params.put("cursor",-1);
        //params.put("since_id",1);  //since the first tweet see all the tweets
        getClient().get(apiUrl, params, handler);
    }

//    public void getFriendsIdsScroll(String screenName,AsyncHttpResponseHandler handler){
//        String apiUrl = getApiUrl("friends/ids.json");
//        //specify the params
//        RequestParams params = new RequestParams();
//        params.put("screen_name",screenName);
//        params.put("cursor",cursor);
//        params.put("count",TwitterConstants.MAX_USERS);
//
//        //params.put("since_id",1);  //since the first tweet see all the tweets
//        getClient().get(apiUrl, params, handler);
//    }

    public void getFollowersIds(String screenName, String cursor, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("followers/ids.json");
        //specify the params
        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        params.put("count",TwitterConstants.MAX_USERS);
        if(cursor!=String.valueOf(TwitterConstants.DEFAULT_CURSOR))
            params.put("cursor",cursor);
        //params.put("since_id",1);  //since the first tweet see all the tweets
        getClient().get(apiUrl, params, handler);
    }

//    public void getFollowersIdsScroll(String screenName, String cursor, AsyncHttpResponseHandler handler){
//        String apiUrl = getApiUrl("followers/ids.json");
//        //specify the params
//        RequestParams params = new RequestParams();
//        params.put("screen_name",screenName);
//        params.put("cursor",cursor);
//        params.put("count",TwitterConstants.MAX_USERS);
//        //params.put("since_id",1);  //since the first tweet see all the tweets
//        getClient().get(apiUrl, params, handler);
//    }

    public void getUserDetails(List<Integer> uids, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("users/lookup.json");
        //specify the params
        RequestParams params = new RequestParams();
        String uidStr="";
        for(Integer uid:uids) {
            uidStr += uid + ",";
        }
        uidStr = uidStr.replace(",$", "");
        params.put("user_id",uidStr);
        getClient().get(apiUrl, params, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

}