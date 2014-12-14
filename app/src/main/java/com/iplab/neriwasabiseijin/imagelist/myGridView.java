package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by neriwasabiseijin on 2014/12/14.
 */
public class myGridView extends GridView{
    // 何点タッチで選択を起動するか
    static int HOLDFINGER = 3;

    // 現在の状態
    static final int MODE_NORMAL = 0;
    static final int MODE_SELECTION = 1;
    private int selectionMode;

    // タッチ位置
    private PointF touchPos = new PointF(0f, 0f);

    // マルチタッチ用
    static int[] mPointerID;
    static PointF[] candidateTouchStart; // 3点タッチの開始点候補

    public myGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myInit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        touchPos = new PointF(ev.getX(), ev.getY());
        setMyPointerId(ev);
        if(myGridViewTouchEvent(ev)) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    // 初期化
    private void myInit(){
        selectionMode = 0;
        mPointerID = new int[HOLDFINGER];
        candidateTouchStart = new PointF[HOLDFINGER];
        for(int i=0; i<HOLDFINGER; i++){
            mPointerID[i] = -1;
            candidateTouchStart[i] = new PointF(-1, -1);
        }
    }

    // マルチタッチ用ポインタと，3点タッチ開始点候補の処理
    private void setMyPointerId(MotionEvent ev){
        int action = ev.getActionMasked();
        int pointerId = ev.getPointerId(ev.getActionIndex());
        int count = ev.getPointerCount();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                for(int i=0; i<HOLDFINGER; i++){mPointerID[i] = -1;}
                mPointerID[0] = pointerId;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for(int i=0; i<HOLDFINGER; i++){
                    if(mPointerID[i] == -1){
                        mPointerID[i] = pointerId;
                        break;
                    }
                }

                if(count == HOLDFINGER){
                    for(int i=0; i<HOLDFINGER; i++){
                        int mPointerIndex = ev.findPointerIndex(mPointerID[i]);
                        candidateTouchStart[i] = new PointF(ev.getX(mPointerIndex), ev.getY(mPointerIndex));
                        if(candidateTouchStart[i].x < 0f){candidateTouchStart[i].x = 0f;}
                        if(candidateTouchStart[i].y < 0f){candidateTouchStart[i].y = 0f;}
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                for(int i=0; i<HOLDFINGER; i++){
                    if(mPointerID[i] == pointerId){
                        mPointerID[i] = -1;
                        candidateTouchStart[i] = new PointF(-1f, -1f);
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                for(int i=0; i<HOLDFINGER; i++){
                    mPointerID[i] = -1;
                    candidateTouchStart[i] = new PointF(-1f, -1f);
                }
                break;
        }
    }

    private boolean myGridViewTouchEvent(MotionEvent ev){
        int action = ev.getActionMasked();
        int pointerId = ev.getPointerId(ev.getActionIndex());
        int count = ev.getPointerCount();

        Log.i("count", "finger:"+count);

        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(selectionMode == MODE_NORMAL){
                    return true;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(selectionMode == MODE_NORMAL){
                    if(count == HOLDFINGER){
                        selectionMode = MODE_SELECTION;
                        Log.i("MODE", "selection");
                        return false;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(count == 1){
                    if(selectionMode == MODE_NORMAL){
                        return true;
                    }else if(selectionMode == MODE_SELECTION){
                        return false;
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(selectionMode == MODE_NORMAL){
                    return true;
                }else if(selectionMode == MODE_SELECTION){
                    return false;
                }
                break;
        }
        return true;
    }

    private boolean myGridViewActionDown(MotionEvent ev){
        return false;
    }
    private boolean myGridViewActionPointerDown(MotionEvent ev){
        return false;
    }
    private boolean myGridViewActionMove(MotionEvent ev){
        return false;
    }
    private boolean myGridViewActionPointerUp(MotionEvent ev){
        return false;
    }
    private boolean myGridViewActionUp(MotionEvent ev){
        return false;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }
}
