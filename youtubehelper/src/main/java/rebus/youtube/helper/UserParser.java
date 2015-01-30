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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package rebus.youtube.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class UserParser extends AsyncTask {

    private static String TAG_LOG = "YouTubeHelper";
    private String USER_ID;
    private ProgressDialog loading;
    private String loadingMessage;
    private Context context;
    private OnComplete onComplete;
    private UserItem user;
    private Boolean showLoading = false;
    private Boolean showLog = false;
    private Boolean error = false;

    public UserParser(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void showLoading(Boolean showLoading, String loadingMessage, Context context) {
        this.showLoading = showLoading;
        this.loadingMessage = loadingMessage;
        this.context = context;
    }

    public void onPauseLoading() {
        if ((loading != null) && loading.isShowing())
            loading.dismiss();
        loading = null;
    }

    public void showLog(Boolean showLog) {
        this.showLog = showLog;
    }

    public interface OnComplete {
        public void onFinish(UserItem user);
        public void onError();
    }

    public void onFinish (OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Document feed = Jsoup.connect("https://gdata.youtube.com/feeds/api/users/" + USER_ID)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(60000).ignoreContentType(true).get();
            user.setDescription(feed.getElementsByTag("content").text());
            user.setThumbnail(feed.select("media|thumbnail[url]").attr("url"));
            user.setLocation(feed.getElementsByTag("yt:location").text());
            user.setGooglePlusUserId(feed.getElementsByTag("yt:googlePlusUserId").text());
            user.setSubscriberCount(feed.select("yt|statistics[subscriberCount]").attr("subscriberCount"));
            user.setTotalUploadViews(feed.select("yt|statistics[totalUploadViews]").attr("totalUploadViews"));
            user.setNumberUpload(feed.select("gd|feedLink[countHint]").get(4).attr("countHint"));
            user.setFeedUpload(feed.select("gd|feedLink[rel]").get(4).attr("rel"));
            if (showLog) {
                Log.d(TAG_LOG, feed.getElementsByTag("content").text());
                Log.d(TAG_LOG, feed.select("media|thumbnail[url]").attr("url"));
                Log.d(TAG_LOG, feed.getElementsByTag("yt:location").text());
                Log.d(TAG_LOG, feed.getElementsByTag("yt:googlePlusUserId").text());
                Log.d(TAG_LOG, feed.select("yt|statistics[subscriberCount]").attr("subscriberCount"));
                Log.d(TAG_LOG, feed.select("yt|statistics[totalUploadViews]").attr("totalUploadViews"));
                Log.d(TAG_LOG, feed.select("gd|feedLink[countHint]").get(4).attr("countHint"));
                Log.d(TAG_LOG, feed.select("gd|feedLink[rel]").get(4).attr("rel"));
            }
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (showLoading) {
            loading = new ProgressDialog(context);
            loading.setMessage(loadingMessage);
            loading.setCancelable(false);
            loading.show();
        }
        user = new UserItem();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        if (showLoading) {
            if ((loading != null) && loading.isShowing()) {
                loading.dismiss();
            }
        }
        if (error) {
            onComplete.onError();
        } else {
            onComplete.onFinish(user);
        }
        super.onPostExecute(o);
    }
}
