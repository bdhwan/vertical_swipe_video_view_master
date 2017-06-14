package com.altamirasoft.vertical_swipe_video_view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.altamirasoft.easing_helper.EasingHelper;
import com.altamirasoft.easing_helper.EasingUpdateListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.annotation.GlideModule;


/**
 * Created by bdhwan on 2017. 6. 14..
 */
public class VerticalSwipeVideoListView extends RelativeLayout implements View.OnTouchListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {



    Context context;

    VelocityTracker tracker;

    VerticalSwipeVideoListDataProvider dataProvider;

    VerticalSwipeVideoListListener listener;

    int width, height;

    ImageView[] imageViews = new ImageView[3];

    VideoView videoView;

    EasingHelper helper;


    int beforePostion;
    int position;
    int tPosition;


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

        imageViews[0].setBackgroundColor(Color.BLACK);
        imageViews[1].setBackgroundColor(Color.BLACK);
        imageViews[2].setBackgroundColor(Color.BLACK);

        tracker = new VelocityTracker();
        setOnTouchListener(this);

        helper = new EasingHelper().setEasing(0.5f);
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
                position = getCurrentPosition();
                Log.d("log","after position = "+position+", ty = "+currentTranslationY);
                invalidateView();
                invalidateData();

                if(listener!=null){
                    listener.onSelectVideo(position);
                }

            }
        });

        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.start();

        helper.start();

        position = 0;
        tPosition = 0;
        beforePostion =-1;
    }

    public void setDataProvider(VerticalSwipeVideoListDataProvider dataProvider){
        this.dataProvider = dataProvider;
    }


    public void setListener(VerticalSwipeVideoListListener listener){
        this.listener = listener;
    }


    public void setPosition(int position){

        this.position = position;
        invalidateData();

        tPosition = position;
        currentTranslationY = tPosition*-height;
        helper.setTargetValue(currentTranslationY,false);
        helper.setCurrentValue(currentTranslationY,false);
        invalidateView();

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
        invalidateData();
    }


    float beforeX, beforeY;

    float currentTranslationY;

    int endPlusPosition = 0;



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float currentX = event.getRawX();
        float currentY = event.getRawY();


        if (action == MotionEvent.ACTION_DOWN) {
            Log.d("log","down");
            tracker.resetValue(currentX, currentY);
            beforeX = currentX;
            beforeY = currentY;
            endPlusPosition = 0;
            helper.pause();

            position = getCurrentPosition();
            invalidateView();
            invalidateData();
//            Log.d("log","before position = "+position);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            tracker.addHistory(currentX, currentY);
            float dx = currentX - beforeX;
            float dy = currentY - beforeY;


            currentTranslationY = currentTranslationY+dy;
            invalidateView();

            beforeX = currentX;
            beforeY = currentY;

            return true;
        } else if (action == MotionEvent.ACTION_UP) {

            float lastVY = tracker.getLastVelocityY(5);
            final float tempCurrent = currentTranslationY;
            helper.setCurrentValue(tempCurrent,false);
            helper.setTargetValue(tempCurrent,false);

            if (Math.abs(lastVY) < 2) {

            } else {
                if (lastVY < 0) {
                    //bottom
                    if(tPosition>=totalCount-1){

                    }
                    else{
                        ++tPosition;
                    }

                } else {
                   //top
                    //bottom
                    if(tPosition<=0){
                    }
                    else{
                        --tPosition;
                    }
                }
            }
            helper.setTargetValue(tPosition*-height,false);
            helper.start();
            return true;
        }
        return false;
    }


    public int getCurrentPosition(){
        int result = (int) ((-currentTranslationY+height/2)/height);
        if(result>totalCount-1){
            result = totalCount -1;
        }
        return result;
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
        videoView.setTranslationY(imageViews[position%3].getTranslationY());
    }


    public void invalidateData(){

        if(dataProvider!=null){
            if(beforePostion!=position){
                videoView.pause();
                videoView.setAlpha(0f);
                videoView.setVideoPath(dataProvider.getVideoUrl(position));

                for(int i =0;i<3;i++){
                    imageViews[i].setAlpha(1f);
                    Glide.with(context).clear(imageViews[i]);
                    imageViews[i].setImageResource(R.drawable.transparent);
                    imageViews[i].setAlpha(1f);

                }
                if(position>1){
                   Glide.with(context).load(dataProvider.getPreviewUrl(position-1)).into(imageViews[(position-1)%3]);
                }
                Glide.with(context).load(dataProvider.getPreviewUrl(position)).into(imageViews[position%3]);
                if(position<totalCount-1){
                    Glide.with(context).load(dataProvider.getPreviewUrl(position+1)).into(imageViews[(position+1)%3]);
                }
            }
            beforePostion = position;
        }


    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.setAlpha(1f);
        videoView.start();

        if(position>1){
            imageViews[(position-1)%3].setAlpha(1f);
        }

        Glide.with(context).clear(imageViews[position%3]);
        imageViews[position%3].setImageResource(R.drawable.transparent);

        if(position<totalCount-1){
            imageViews[(position+1)%3].setAlpha(1f);
        }
    }
}
