package com.altamirasoft.vertical_swipe_video_view;

import android.util.Log;

import java.util.Date;

/**
 * Created by bdhwan on 15. 4. 30..
 */
public class VelocityTracker {

    int trackCount = 20;

    float[] beforeXArray = new float[trackCount];
    float[] beforeYArray = new float[trackCount];
    long[] timeArray = new long[trackCount];


    public int totalMoveDistance = 0;
    public int totalMoveDistanceX = 0;
    public int totalMoveDistanceY = 0;

    static VelocityTracker instance;

    public int checkCount = 0;


    public static VelocityTracker getInstance(){
        if(instance==null){
           instance = new VelocityTracker();
        }
        return instance;
    }


    public void resetValue(float x, float y){

        long now = new Date().getTime();

        for(int i =0;i<trackCount;i++){
            beforeXArray[i] = x;
            beforeYArray[i] = y;
            timeArray[i] = now;
        }
        checkCount = 0;

        totalMoveDistance = 0;
        totalMoveDistanceX = 0;
        totalMoveDistanceY = 0;

    }

    public int getCheckCount(){
        return checkCount;
    }

    public int getTrackCount(){
        return  trackCount;
    }

    public void addHistory(float x, float y){

        checkCount++;
        long now = new Date().getTime();

        for(int i = trackCount-1;i>0;i--){
            timeArray[i] = timeArray[i-1];
            beforeXArray[i] = beforeXArray[i-1];
            beforeYArray[i] = beforeYArray[i-1];
        }

        timeArray[0] = now;
        beforeXArray[0] = x;
        beforeYArray[0] = y;


        totalMoveDistance += Math.sqrt(Math.pow(beforeXArray[0] - beforeXArray[1], 2) + Math.pow(beforeYArray[0] - beforeYArray[1], 2));
        totalMoveDistanceX+= Math.abs(beforeXArray[0] - beforeXArray[1]);
        totalMoveDistanceY+= Math.abs(beforeYArray[0] - beforeYArray[1]);

    }


    public float getLastVelocityY(int count){

        long now = new Date().getTime();

        float totalDx = 0;
        long from = timeArray[count-1];
        for(int i =1;i<count;i++){
            if(i>checkCount-1)continue;
            totalDx+=(beforeYArray[i-1]-beforeYArray[i]);
        }
        return totalDx/(float)(now - from);
    }


    public float getLastVelocityX(int count){

        long now = new Date().getTime();

        float totalDx = 0;
        long from = timeArray[count-1];
        for(int i =1;i<count;i++){
            totalDx+=(beforeXArray[i-1]-beforeXArray[i]);
        }
        return totalDx/(float)(now - from);
    }



    public float getAverageVelocityX(){

        float totalDx = 0;

        for(int i =1;i<trackCount;i++){
            totalDx+=(Math.abs(beforeXArray[i - 1] - beforeXArray[i]));
        }
        return totalDx/trackCount;
    }

    public float getAverageX(){

        float totalDx = 0;
        for(int i =1;i<trackCount;i++){
            totalDx+=beforeXArray[i];
        }
        return totalDx/trackCount;
    }




    private void printValue(){
        String valueString = beforeXArray[0]+"";
        for(int i =1;i<trackCount;i++){
            valueString +=(","+beforeXArray[i]);
        }
        Log.d("log", "valuex = " + valueString);

        valueString = beforeYArray[0]+"";
        for(int i =1;i<trackCount;i++){
            valueString +=(","+beforeYArray[i]);
        }
        Log.d("log", "valueY = " + valueString);

    }


    public float getAverageVelocityY(int count){


        int targetCount = count;
        if(targetCount>trackCount){
            targetCount = trackCount;
        }

        float totalDy = 0;
        for(int i =1;i<targetCount;i++){
            totalDy+=(Math.abs(beforeYArray[i - 1] - beforeYArray[i]));
        }

        return totalDy/targetCount;

    }


    public float getAverageVelocityX(int count){


        int targetCount = count;
        if(targetCount>trackCount){
            targetCount = trackCount;
        }

        float totalDx = 0;
        for(int i =1;i<targetCount;i++){
            totalDx+=(beforeXArray[i-1]-beforeXArray[i]);
        }

        return totalDx/targetCount;
    }


    public float getVerticalDragPercent(int count){

//        printValue();

        float okCount = 0;

        int targetCount = count;
        if(checkCount<targetCount){
            targetCount = checkCount;
        }

        for(int i =0;i<targetCount-1;i++){

            float dx = beforeXArray[i] - beforeXArray[i+1];
            float dy = beforeYArray[i] - beforeYArray[i+1];
            if(Math.abs(dy)> Math.abs(dx)){
                okCount++;
            }
        }
        return okCount/(float)(count-1);
    }

    public float getHorizontalDragPercent(int count){

        float okCount = 0;

        int targetCount = count;
        if(checkCount<targetCount){
            targetCount = checkCount;
        }

        for(int i =0;i<targetCount-1;i++){

            float dx = beforeXArray[i] - beforeXArray[i+1];
            float dy = beforeYArray[i] - beforeYArray[i+1];
            if(Math.abs(dy)< Math.abs(dx)){
                okCount++;
            }
        }
        return okCount/(float)(count-1);
    }




    public int getTouchMode(int count){

        float verticalPercent = getVerticalDragPercent(count);
        float horiznoalPercent = getHorizontalDragPercent(count);


        if(horiznoalPercent>0.8f){

            if(getAverageVelocityX(count)>0){

                return 1;
            }
            else{
                return 2;
            }

        }
        else{

            if(verticalPercent>0.8f){

                if(getAverageVelocityY(count)>0){
                    return 3;
                }
                else{
                    return 4;
                }
            }
            else{
                //아무것도 아님
                return 5;
            }
        }
    }


    public boolean isSlow(){

        if(getAverageVelocityY(20)<15f){
            return true;
        }
        else{
            return false;
        }
    }


}
