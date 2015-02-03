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

public class VideoParser extends AsyncTask {

    private static String TAG_LOG = "YouTubeHelper";
    private String ID_VIDEO;
    private ProgressDialog loading;
    private String loadingMessage;
    private Context context;
    private OnComplete onComplete;
    private VideoItem values;
    private Boolean showLoading = false;
    private Boolean error = false;
    private Boolean showLog = false;

    public VideoParser(String ID_VIDEO) {
        this.ID_VIDEO = ID_VIDEO;
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
        public void onFinish(VideoItem values);
        public void onError();
    }

    public void onFinish (OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Document feed = Jsoup.connect("https://gdata.youtube.com/feeds/api/videos/" + ID_VIDEO + "?v=2")
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(60000).ignoreContentType(true).get();
            values.setTitleVideo(feed.getElementsByTag("title").text());
            values.setDescriptionVideo(feed.getElementsByTag("media:description").text());
            values.setLikeVideo(feed.select("yt|rating").attr("numLikes"));
            values.setDislikeVideo(feed.select("yt|rating").attr("numDislikes"));
            values.setAverageVideo(feed.select("gd|rating").attr("average"));
            values.setViewsVideo(feed.select("yt|statistics").attr("viewCount"));
            values.setDurationVideo(feed.select("yt|duration").attr("seconds"));
            values.setThumbnailVideo(feed.select("media|thumbnail").first().attr("url"));
            values.setAuthorVideo(feed.getElementsByTag("name").text());
            values.setAuthorVideoId(feed.getElementsByTag("yt:uploaderId").text());
            values.setNumberCommentsVideo(feed.select("gd|feedLink").attr("countHint"));
            values.setFeedCommentsVideo(feed.select("gd|feedLink").attr("href"));
            values.setCategoryVideo(feed.select("media|category").attr("label"));
            values.setDateVideo(feed.getElementsByTag("yt:uploaded").text());
            Document image  = Jsoup.connect(feed.getElementsByTag("uri").text())
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(60000).ignoreContentType(true).get();
            values.setThumbnailAuthor(image.select("media|thumbnail").attr("url"));
            if(showLog) {
                Log.d(TAG_LOG, feed.getElementsByTag("title").text());
                Log.d(TAG_LOG, feed.getElementsByTag("media:description").text());
                Log.d(TAG_LOG, feed.select("yt|rating").attr("numLikes"));
                Log.d(TAG_LOG, feed.select("yt|rating").attr("numDislikes"));
                Log.d(TAG_LOG, feed.select("gd|rating").attr("average"));
                Log.d(TAG_LOG, feed.select("yt|statistics").attr("viewCount"));
                Log.d(TAG_LOG, feed.select("yt|duration").attr("seconds"));
                Log.d(TAG_LOG, feed.select("media|thumbnail").first().attr("url"));
                Log.d(TAG_LOG, feed.getElementsByTag("name").text());
                Log.d(TAG_LOG, feed.getElementsByTag("yt:uploaderId").text());
                Log.d(TAG_LOG, feed.select("gd|feedLink").attr("countHint"));
                Log.d(TAG_LOG, feed.select("gd|feedLink").attr("href"));
                Log.d(TAG_LOG, feed.select("media|category").attr("label"));
                Log.d(TAG_LOG, feed.getElementsByTag("yt:uploaded").text());
                Log.d(TAG_LOG, image.select("media|thumbnail").attr("url"));
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
        values = new VideoItem();
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
            onComplete.onFinish(values);
        }
        super.onPostExecute(o);
    }
}
