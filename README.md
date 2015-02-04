# YouTubeHelper [ ![Download](https://api.bintray.com/packages/raphaelbussa/maven/YouTubeHelper/images/download.svg) ](https://bintray.com/raphaelbussa/maven/YouTubeHelper/_latestVersion)[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-YouTubeHelper-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1479)
This library is created for get information from YouTube, you can get upload video, user information, video details, comments video, related videos.

### Import
Now library is in jcenter, so just add this:
```Gradle
dependencies {
    compile 'rebus:YouTubeHelper:1.1.0'
}
```

### How to use
Upload Video List
```java
import rebus.youtube.helper.UploadItem;
import rebus.youtube.helper.UploadParser;

private UploadParser uploadParser;

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
    }

    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
User Info
```java
import rebus.youtube.helper.UserItem;
import rebus.youtube.helper.UserParser;

private UserParser userParser;

userParser = new UserParser("PewDiePie"); //new parser for user PewDiePie
userParser.showLog(true); //set logcat (default false)
userParser.showLoading(true, getString(R.string.loading), this); //set loading dialog, need boolean value, string to show and context
userParser.execute(); //start parsing!
userParser.onFinish(new UserParser.OnComplete() {
    @Override
    public void onFinish(UserItem user) {
        //return UserItem with all user info
        //do here something when parsing is finished
    }
    
    @Override
    public void onError() {
        //this method is call on parsing error
    }
});
```
Remember if you use loading dialog
```java
@Override
protected void onPause() {
    super.onPause();
    userParser.onPauseLoading();
}
```
Other methods are used in the same way

### Methods available
- Upload video list
- Comments video list
- Related video list
- Details video items
- Info user items

###Sample
Some sample code is present in the repo

[ ![Browse code](http://dabuttonfactory.com/b.png?t=Browse code&f=Calibri-Bold&ts=24&tc=ffffff&c=5&bgt=unicolored&bgc=47c&hp=20&vp=11) ](https://github.com/rebus007/YouTubeHelper/tree/master/app/src/main)

You can also download sample apk, it show last video from PewDiePie end also PewDiePie user information

[ ![Download](http://dabuttonfactory.com/b.png?t=Sample.apk&f=Calibri-Bold&ts=24&tc=ffffff&c=5&bgt=unicolored&bgc=47c&hp=20&vp=11) ](https://github.com/rebus007/YouTubeHelper/releases/tag/1.0.0)

### License
```
The MIT License (MIT)

Copyright (c) 2015 Raphael Bussa <raphaelbussa@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
