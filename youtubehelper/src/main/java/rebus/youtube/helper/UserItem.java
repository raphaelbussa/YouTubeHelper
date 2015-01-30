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

public class UserItem {

    private String description;
    private String thumbnail;
    private String location;
    private String googlePlusUserId;
    private String subscriberCount;
    private String totalUploadViews;
    private String numberUpload;
    private String feedUpload;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGooglePlusUserId() {
        return googlePlusUserId;
    }

    public void setGooglePlusUserId(String googlePlusUserId) {
        this.googlePlusUserId = googlePlusUserId;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public String getTotalUploadViews() {
        return totalUploadViews;
    }

    public void setTotalUploadViews(String totalUploadViews) {
        this.totalUploadViews = totalUploadViews;
    }

    public String getNumberUpload() {
        return numberUpload;
    }

    public void setNumberUpload(String numberUpload) {
        this.numberUpload = numberUpload;
    }

    public String getFeedUpload() {
        return feedUpload;
    }

    public void setFeedUpload(String feedUpload) {
        this.feedUpload = feedUpload;
    }
}
