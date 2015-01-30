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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CommentsParser extends AsyncTask {

    private static String TAG_LOG = "YouTubeHelper";
    private String VIDEO_ID;
    private ProgressDialog loading;
    private String loadingMessage;
    private Context context;
    private OnComplete onComplete;
    private ArrayList<CommentsItem> commentsItems;
    private Boolean showLoading = false;
    private Boolean showLog = false;
    private int number = 50;

    public CommentsParser(String VIDEO_ID) {
        this.VIDEO_ID = VIDEO_ID;
    }

    public void setNumber(int number) {
        if (number >= 10 && number <= 50) {
            this.number = number;
        } else {
            Log.e(TAG_LOG, "This value must be between 10 and 50");
        }
    }

    public void showLoading(Boolean showLoading, String loadingMessage, Context context) {
        this.showLoading = showLoading;
        this.loadingMessage = loadingMessage;
        this.context = context;
    }

    public void showLog(Boolean showLog) {
        this.showLog = showLog;
    }

    public void onPauseLoading() {
        if ((loading != null) && loading.isShowing())
            loading.dismiss();
        loading = null;
    }

    public interface OnComplete {
        public void onFinish(ArrayList<CommentsItem> commentsItems);
        public void onError();
    }

    public void onFinish (OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Document feed = Jsoup.connect("https://gdata.youtube.com/feeds/api/videos/" + VIDEO_ID + "/comments?max-results=" + number + "&v=2")
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(60000).ignoreContentType(true).get();
            Elements entrys = feed.getElementsByTag("entry");
            for (Element entry : entrys) {
                CommentsItem commentsItem = new CommentsItem();
                commentsItem.setPublished(entry.getElementsByTag("published").text());
                commentsItem.setContent(entry.getElementsByTag("content").text());
                commentsItem.setName(entry.getElementsByTag("name").text());
                commentsItem.setReplyCount(entry.getElementsByTag("yt:replyCount").text());
                commentsItem.setGooglePlusUserId(entry.getElementsByTag("yt:googlePlusUserId").text());
                commentsItem.setChannelId(entry.getElementsByTag("yt:channelId").text());
                Document image  = Jsoup.connect(entry.getElementsByTag("uri").text())
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                        .timeout(60000).ignoreContentType(true).get();
                commentsItem.setThumbnail(image.select("media|thumbnail[url]").attr("url"));
                if (showLog) {
                    Log.d(TAG_LOG, entry.getElementsByTag("published").text());
                    Log.d(TAG_LOG, entry.getElementsByTag("content").text());
                    Log.d(TAG_LOG, entry.getElementsByTag("name").text());
                    Log.d(TAG_LOG, entry.getElementsByTag("yt:replyCount").text());
                    Log.d(TAG_LOG, image.select("media|thumbnail[url]").attr("url"));
                }
                commentsItems.add(commentsItem);
            }
        } catch (IOException e) {
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
        commentsItems = new ArrayList<>();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        if (showLoading) {
            if ((loading != null) && loading.isShowing()) {
                loading.dismiss();
            }
        }
        if (commentsItems.size() == 0) {
            onComplete.onError();
        } else {
            onComplete.onFinish(commentsItems);
        }
        super.onPostExecute(o);
    }
}
