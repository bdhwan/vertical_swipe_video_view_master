package com.altamirasoft.vertical_swipe_video_view;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.altamirasoft.easing_helper.EasingHelper;
import com.altamirasoft.easing_helper.EasingUpdateListener;

/**
 * Created by bdhwan on 2017. 6. 14..
 */

public class VerticalSwipeVideoListView extends RelativeLayout implements View.OnTouchListener{



    Context context;

    VelocityTracker tracker;

    VerticalSwipeVideoListDataProvider dataProvider;

    VerticalSwipeVideoListListener listener;

    int width, height;

    ImageView[] imageViews = new ImageView[3];

    VideoView videoView;

    EasingHelper helper;


    int position;

    int totalCount = 10;

    boolean isAnimating = false;

    float[] initPosition = new float[3];

    public VerticalSwipeVideoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        String service = Context.LAYOUT_INFLATER_SERVICE;
        final LayoutInflater li = (LayoutInflater) context.getSystemService(service);
        li.inflate(R.layout.view_vertical_swipe_video_list, this, true);
        imageViews[0] = (ImageView)findViewById(R.id.imageTop);
        imageViews[1] = (ImageView)findViewById(R.id.imageCenter);
        imageViews[2] = (ImageView)findViewById(R.id.imageBottom);

        videoView = (VideoView)findViewById(R.id.videoView);

        imageViews[0].setBackgroundColor(Color.BLUE);
        imageViews[1].setBackgroundColor(Color.RED);
        imageViews[2].setBackgroundColor(Color.YELLOW);

        tracker = new VelocityTracker();
        setOnTouchListener(this);

        helper = new EasingHelper().setEasing(0.6f);
        helper.addUpdateListener(new EasingUpdateListener() {
            @Override
            public void onUpdateCurrentValue(float value) {
                currentTranslationY = value;
                invalidateView();
            }

            @Override
            public void onFinishUpdateValue(float value) {
                currentTranslationY = value;
                isAnimating = false;
                position += endPlusPosition;
                Log.d("log","after position = "+position+", ty = "+currentTranslationY);
            }
        });


        videoView.setVideoPath("https://d3egch4vj9g9yc.cloudfront.net/donut/2017060908/sample21_L2.mp4");
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.start();

        helper.start();

        position = 0;
    }

    public void setDataProvider(VerticalSwipeVideoListDataProvider dataProvider){
        this.dataProvider = dataProvider;
    }


    public void setListener(VerticalSwipeVideoListListener listener){
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("log","onSizeChanged ="+w+", h = "+h);
        this.width = w;
        this.height = h;
        initView();
    }

    private void initView() {

        videoView.pause();
        for(int i =0;i<3;i++){
            imageViews[i].setImageResource(R.drawable.transparent);
            RelativeLayout.LayoutParams aParam =  (RelativeLayout.LayoutParams)imageViews[i].getLayoutParams();
            aParam.width = width;
            aParam.height = height;
            imageViews[i].setLayoutParams(aParam);
        }

        RelativeLayout.LayoutParams videoParam =  (RelativeLayout.LayoutParams)videoView.getLayoutParams();
        videoParam.width = width;
        videoParam.height = height;
        videoView.setLayoutParams(videoParam);


        initPosition[0] = 0;
        initPosition[1] = height;
        initPosition[2] = height*2;

        invalidateView();

    }


    float beforeX, beforeY;

    float currentTranslationY;

    int endPlusPosition = 0;



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float currentX = event.getRawX();
        float currentY = event.getRawY();

        if(isAnimating)return false;
        if (action == MotionEvent.ACTION_DOWN) {
            Log.d("log","down");
            tracker.resetValue(currentX, currentY);
            beforeX = currentX;
            beforeY = currentY;
            endPlusPosition = 0;
            helper.pause();
//            Log.d("log","before position = "+position);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            tracker.addHistory(currentX, currentY);
            float dx = currentX - beforeX;
            float dy = currentY - beforeY;

            if(tracker.getCheckCount()<5){
                return true;
            }
            currentTranslationY = currentTranslationY+dy;
            invalidateView();

            beforeX = currentX;
            beforeY = currentY;

            return true;
        } else if (action == MotionEvent.ACTION_UP) {

            float lastVY = tracker.getLastVelocityY(5);
            isAnimating = true;
            helper.setCurrentValue(currentTranslationY);
//            Log.d("log","track cound = "+tracker.getCheckCount());
            if (Math.abs(lastVY) < 2) {
                helper.setTargetValue(position*height);
            } else {
                if (lastVY < 0) {
                    //bottom
                    if(position>=totalCount-1){
                        helper.setTargetValue(position*-height);
                    }
                    else{
                        helper.setTargetValue((position+1)*-height);
                        position++;
                    }

                } else {
                   //top
                    //bottom
                    if(position<=0){
                        helper.setTargetValue(position*-height);
                    }
                    else{
                        helper.setTargetValue((position-1)*-height);
                        position--;
                    }
                }
            }
            return true;
        }
        return false;
    }



    private void invalidateView() {
        if(height==0)return;
        for(int i =0;i<3;i++){
            float target = (currentTranslationY+initPosition[i])%(3*height);
            if(target<-height){
                target = 3f*height + target;
            }
            imageViews[i].setTranslationY(target);
        }


        videoView.setTranslationY(currentTranslationY%height);
    }
}
