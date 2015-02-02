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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import rebus.youtube.helper.UploadItem;
import rebus.youtube.helper.UploadParser;
import rebus.youtube.helper.sample.adapter.ListAdapter;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ListAdapter listAdapter;
    private UploadParser uploadParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listView);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_user:
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        MainActivity.this.startActivity(intent);
                        return true;
                    case R.id.action_info:
                        infoDialog(MainActivity.this);
                        return true;
                }
                return true;
            }
        });
        uploadParser = new UploadParser("PewDiePie"); //set user id channel youtube
        uploadParser.setNumber(25); //set number of result (default 50)
        uploadParser.showLog(true); //set logcat (default false)
        uploadParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
        uploadParser.execute(); //start parsing!
        uploadParser.onFinish(new UploadParser.OnComplete() {
            @Override
            public void onFinish(final ArrayList<UploadItem> uploadVideo) {
                //return arraylist with all video info for each item
                //do here something when parsing is finished
                listAdapter = new ListAdapter(uploadVideo, MainActivity.this);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        detailsDialog(uploadVideo.get(position), MainActivity.this);
                        return false;
                    }
                });
            }

            @Override
            public void onError() {
                //this method is call on parsing error
                Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void detailsDialog(UploadItem uploadItem, Context context) {
        final CharSequence[] charSequences = {
                uploadItem.getIdVideo(),
                uploadItem.getThumbnail(),
                uploadItem.getDuration(),
                uploadItem.getViewCount(),
                uploadItem.getRatingAverage(),
                uploadItem.getDateVideo()
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(uploadItem.getTitle());
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void infoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.info));
        builder.setPositiveButton(getString(R.string.github), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent o = new Intent(Intent.ACTION_VIEW);
                o.setData(Uri.parse("https://github.com/rebus007/YouTubeHelper"));
                MainActivity.this.startActivity(o);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadParser.onPauseLoading(); //if you use loading dialog, remember to call onPauseLoading() method in onPause()
    }
}
