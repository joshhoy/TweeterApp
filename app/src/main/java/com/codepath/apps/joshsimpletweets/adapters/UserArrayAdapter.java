package com.codepath.apps.joshsimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.joshsimpletweets.R;
import com.codepath.apps.joshsimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Prajakta on 5/31/2015.
 */
public class UserArrayAdapter extends ArrayAdapter<User> {
    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvFullName;
    }

    public UserArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       User user = getItem(position);
        //find or inflate the template
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
//        }
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_users, parent, false);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //find the subviews to fill data in the template

        //populate data into subview
        viewHolder.tvFullName.setText(user.getName());
        viewHolder.tvBody.setText(user.getTagLine());
        viewHolder.tvScreenName.setText("@" + user.getScreenName());
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(viewHolder.ivProfileImage);
        //return view to be inserted in the list
        return convertView;
    }
}
