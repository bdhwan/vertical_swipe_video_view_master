package com.altamirasoft.vertical_swipe_video_view_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.altamirasoft.vertical_swipe_video_view.VerticalSwipeVideoListDataProvider;
import com.altamirasoft.vertical_swipe_video_view.VerticalSwipeVideoListListener;
import com.altamirasoft.vertical_swipe_video_view.VerticalSwipeVideoListView;

public class MainActivity extends AppCompatActivity {


    String[] videoUrl = {
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample01_L2_1496998062.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample02_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample03_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample04_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample05_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample06_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample07_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample08_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample09_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample10_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample11_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample12_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample13_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample14_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample15_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample16_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample17_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample18_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample19_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample20_L2.mp4",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample21_L2.mp4",

    };


    String[] videoPreviewUrl = {
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998058956.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998062659.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998066159.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998072388.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998077448.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998082138.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998086805.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998091741.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998096578.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998100789.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998104559.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998108912.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998113549.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998115094.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998119667.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998123294.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998125188.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998129584.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998133459.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998136104.png",
            "https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/preview_1496998140358.png",

    };

    VerticalSwipeVideoListView videoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoListView = (VerticalSwipeVideoListView)findViewById(R.id.videoListView);


        videoListView.setDataProvider(new VerticalSwipeVideoListDataProvider() {
            @Override
            public String getVideoUrl(int position) {
                return videoUrl[position];
            }

            @Override
            public String getPreviewUrl(int position) {
                return videoPreviewUrl[position];
            }

            @Override
            public int getTotalVideoCount() {
                return videoUrl.length;
            }
        });


        videoListView.setListener(new VerticalSwipeVideoListListener() {
            @Override
            public void onSelectVideo(int position) {
                Log.d("log","onSelectVideo = "+position);

            }
        });

    }

    public void clickTest(View v){
        videoListView.setPosition(6);
    }
}
