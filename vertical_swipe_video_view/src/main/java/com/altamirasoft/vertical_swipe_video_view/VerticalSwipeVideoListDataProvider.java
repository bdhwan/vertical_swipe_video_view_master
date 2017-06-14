package com.altamirasoft.vertical_swipe_video_view;

/**
 * Created by bdhwan on 2017. 6. 14..
 */

public interface VerticalSwipeVideoListDataProvider {

    String getVideoUrl(int position);
    String getPreviewUrl(int position);
    int getTotalVideoCount();


}
