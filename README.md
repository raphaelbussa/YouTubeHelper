# YouTubeHelper [ ![Download](https://api.bintray.com/packages/raphaelbussa/maven/YouTubeHelper/images/download.svg) ](https://bintray.com/raphaelbussa/maven/YouTubeHelper/_latestVersion)
This library is created for get information from YouTube, you can get upload video, user information, video details, comments video, related videos.

### Import
At the moment the library is in my personal maven repo
```Gradle
repositories {
    maven {
        url 'http://dl.bintray.com/raphaelbussa/maven'
    }
}
```
```Gradle
dependencies {
    compile 'rebus:YouTubeHelper:1.0.2'
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

