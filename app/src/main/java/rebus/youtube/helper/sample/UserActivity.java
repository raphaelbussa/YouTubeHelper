/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Raphael Bussa <raphaelbussa@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package rebus.youtube.helper.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import rebus.youtube.helper.UserItem;
import rebus.youtube.helper.UserParser;

public class UserActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private UserParser userParser;

    private TextView username;
    private TextView description;
    private TextView location;
    private TextView googlePlusUserId;
    private TextView subscriberCount;
    private TextView totalUploadViews;
    private TextView numberUpload;
    private TextView feedUpload;
    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        username = (TextView) findViewById(R.id.user);
        description = (TextView) findViewById(R.id.description);
        location = (TextView) findViewById(R.id.location);
        googlePlusUserId = (TextView) findViewById(R.id.googlePlusUserId);
        subscriberCount = (TextView) findViewById(R.id.subscriberCount);
        totalUploadViews = (TextView) findViewById(R.id.totalUploadViews);
        numberUpload = (TextView) findViewById(R.id.numberUpload);
        feedUpload = (TextView) findViewById(R.id.feedUpload);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        toolbar.setTitle("PewDiePie");
        userParser = new UserParser("PewDiePie"); //new parser for user PewDiePie
        userParser.showLog(true); //set logcat (default false)
        userParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
        userParser.execute(); //start parsing!
        userParser.onFinish(new UserParser.OnComplete() {
            @Override
            public void onFinish(UserItem user) {
                //return UserItem with all user info
                //do here something when parsing is finished
                username.setText("PewDiePie");
                description.setText(user.getDescription());
                location.setText(user.getLocation());
                googlePlusUserId.setText(user.getGooglePlusUserId());
                subscriberCount.setText(user.getSubscriberCount());
                totalUploadViews.setText(user.getTotalUploadViews());
                numberUpload.setText(user.getNumberUpload());
                feedUpload.setText(user.getFeedUpload());
                Picasso.with(UserActivity.this).load(user.getThumbnail())
                        .error(R.drawable.ic_launcher)
                        .placeholder(R.drawable.ic_launcher)
                        .into(thumbnail);
            }

            @Override
            public void onError() {
                //this method is call on parsing error
                Toast.makeText(UserActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        userParser.onPauseLoading();
    }
}
