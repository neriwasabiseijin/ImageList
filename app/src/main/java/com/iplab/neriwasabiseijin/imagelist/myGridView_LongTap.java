package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by neriwasabiseijin on 2014/12/21.
 */
public class myGridView_LongTap  extends GridView {
    private MainActivity myMainActivity;

    // ロングタップ判定用
    private Timer longTapTimer;
    private boolean longTapFlag;
    private long  PERIOD_LONG_TAP = 500;
    private PointF nowTouchPoint;

    // タッチ位置
    private PointF touchPos = new PointF(0f, 0f);

    // 範囲選択用
    private int selectionStartItem;
    private int selectionEndItem;

    private boolean[] tmpSelectItem;

    // 一行の画像の数
    public int columnNum = 0;

    public myGridView_LongTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        myInit(context);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        columnNum = getColumnNum();

        for(int i=0; i<this.getCount(); i++){
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            setItemSelectedState(i, false, r);
        }

        this.setBackgroundColor(Color.rgb(0,0,0));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        touchPos = new PointF(ev.getX(), ev.getY());

        myGridViewTouchEvent(ev);

        return true;
    }

    // 初期化
    public void myInit(Context context){
        myMainActivity = (MainActivity)context;
        myMainActivity.setSelectionMode(MainActivity.MODE_NORMAL);

        longTapTimer = null;
        longTapFlag = false;
        nowTouchPoint = new PointF(-1f, -1f);

        selectionStartItem = -1;
        selectionEndItem = -1;
    }

    private boolean myGridViewTouchEvent(MotionEvent ev){
        int action = ev.getActionMasked();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                makeCSVonTouch(ev, "ACTION_DOWN");
                return myGridViewActionDown(ev);
            case MotionEvent.ACTION_MOVE:
                makeCSVonTouch(ev, "ACTION_MOVE");
                return myGridViewActionMove(ev);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                makeCSVonTouch(ev, "ACTION_UP");
                return myGridViewActionUp(ev);
        }

        return true;
    }

    private void makeCSVonTouch(MotionEvent ev, String act){
        int count = ev.getPointerCount();
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);

        myMainActivity.csv += myMainActivity.getNowTime() + "," + "TOUCH_EVENT" + "," + act
                + "," + pointerId + "," + ev.getX(pointerIndex) + "," + ev.getY(pointerIndex)
                + "," + ev.getSize(pointerIndex) + "," + count + "\n";

        if(myMainActivity.selectionMode == myMainActivity.MODE_SELECTION){
            boolean changeStateFlag = false;
            for(int i=0; i<this.getCount(); i++){
                if(myMainActivity.selectedItem[i] != myMainActivity.beforeSelectedItem[i]){
                    changeStateFlag = true;
                    break;
                }
            }

            if(changeStateFlag) {
                myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECT_ITEM_MOVE";
                for (int i = 0; i < this.getCount(); i++) {
                    if (myMainActivity.selectedItem[i]) {
                        myMainActivity.csv += "," + i;
                    }
                    myMainActivity.beforeSelectedItem[i] = myMainActivity.selectedItem[i];
                }
                myMainActivity.csv += "," + "END\n";
            }
        }

    }

    private boolean myGridViewActionDown(MotionEvent ev){
        nowTouchPoint = new PointF(ev.getX(), ev.getY());
        checkLongTap();
/*
        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
            if(longTapFlag) {
                myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);

                PointF p = new PointF(ev.getX(), ev.getY());
                if(selectionStartItem == -1) {
                    selectionStartItem = mGetNowMoveItem(p);
                    mSetSelection(selectionStartItem, selectionStartItem);
                    myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECTION_START" + "," + selectionStartItem + "\n";
                }
            }
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            if(longTapFlag) {
                for(int i=0; i<myMainActivity.selectedItem.length; i++){
                    tmpSelectItem[i] = myMainActivity.selectedItem[i];
                }

                PointF p = new PointF(ev.getX(), ev.getY());
                if(selectionStartItem == -1) {
                    selectionStartItem = mGetNowMoveItem(p);
                    mSetSelection(selectionStartItem, selectionStartItem);
                    myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECTION_START" + "," + selectionStartItem + "\n";
                }
            }
        }*/

        return true;
    }

    private boolean myGridViewActionMove(MotionEvent ev){
        int count = ev.getPointerCount();
        nowTouchPoint = new PointF(ev.getX(), ev.getY());

        if(count == 1){
            if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
                return true;
            }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
                if(longTapFlag) {
                    PointF pos = new PointF(ev.getX(), ev.getY());

                    if (selectionStartItem == -1) {
                        selectionStartItem = mGetNowMoveItem(pos);
                        myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECTION_START" + "," + selectionStartItem + "\n";
                    }
                    // Layoutの関係で-1が返ってくるかもしれないので対策
                    int tmpItem = mGetNowMoveItem(pos);
                    if (tmpItem >= 0) {
                        selectionEndItem = tmpItem;
                    }

                    if (selectionStartItem != -1 && selectionEndItem != -1) {
                        mSetSelection(selectionStartItem, selectionEndItem);
                    }

                    return false;
                }
                return true;
            }
        }
        return true;
    }

    private boolean myGridViewActionUp(MotionEvent ev){
        longTapFlag = false;
        nowTouchPoint = new PointF(-1f, -1f);

        if(longTapTimer != null){
            longTapTimer.cancel();
            longTapTimer = null;
        }

        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
            return true;
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            PointF p = new PointF(ev.getX(), ev.getY());
            if(selectionStartItem != -1 || selectionEndItem != -1) {
                myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECTION_END" + "," + selectionStartItem + "," + selectionEndItem + "\n";
                selectionStartItem = -1;
                selectionEndItem = -1;
                return false;
            }

            int tmp =  mGetNowMoveItem(p);
            if(tmp != -1) {
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(tmp);
                setItemSelectedState(tmp, !getItemSelectedState(tmp), r);
            }
            return true;
        }

        return true;
    }

    private void checkLongTap(){
        if(longTapTimer == null) {
            TimerTask timerTask = new TaskCheckLongTap(myMainActivity);
            longTapTimer = new Timer(true);
            longTapTimer.schedule(timerTask, PERIOD_LONG_TAP);
        }
    }

    public class TaskCheckLongTap extends TimerTask{
        private PointF startPos;
        private Handler handler;
        private Context context;

        public TaskCheckLongTap(Context cont){
            handler = new Handler();
            this.context = cont;

            startPos = nowTouchPoint;
        }

        @Override
        public void run(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(nowTouchPoint.x != -1 && nowTouchPoint.y != -1) {
                        if (FloatMath.sqrt((startPos.x - nowTouchPoint.x) * (startPos.x - nowTouchPoint.x) + (startPos.y - nowTouchPoint.y) * (startPos.y - nowTouchPoint.y)) < 50) {
                            Log.i("CHECK", "LONGTAP");
                            myMainActivity.csv += myMainActivity.getNowTime() + "," + "LONG_TAP" + "," + nowTouchPoint.x +"," + nowTouchPoint.y + "\n";

                            if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
                                myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);
                            }

                            longTapFlag = true;

                            for(int i=0; i<myMainActivity.selectedItem.length; i++){
                                tmpSelectItem[i] = myMainActivity.selectedItem[i];
                            }

                            PointF p = nowTouchPoint;
                            if(selectionStartItem == -1) {
                                selectionStartItem = mGetNowMoveItem(p);
                                mSetSelection(selectionStartItem, selectionStartItem);
                                myMainActivity.csv += myMainActivity.getNowTime() + "," + "SELECTION_START" + "," + selectionStartItem + "\n";
                            }


                        }
                    }

                    longTapTimer.cancel();
                    longTapTimer = null;
                }
            });
        }

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setSelectedItemLength(){
        myMainActivity.selectedItem = new boolean[this.getCount()];
        myMainActivity.beforeSelectedItem = new boolean[this.getCount()];
        tmpSelectItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
        myMainActivity.selectedItem[position] = state;
        if(state){
            itemView.setBackgroundColor(Color.rgb(80, 80, 240));
        }else{
            itemView.setBackgroundColor(Color.rgb(0,0,0));
            if(myMainActivity.testModeFlag) {
                myMainActivity.mShowQuestion(position, itemView);
            }
        }
    }

    public boolean getItemSelectedState(int position){
        return myMainActivity.selectedItem[position];
    }

    private int getColumnNum(){
        float layoutWidth = this.getMeasuredWidth();
        float columnWidth = 160; // this.getColumnWidth();
        float holizontalSpace = 8; //this.getHorizontalSpacing();

        int count = (int)(layoutWidth/columnWidth);
        int mod = (int)(layoutWidth%columnWidth);

        if(mod != 0) {
            for (; mod / holizontalSpace < count; ) {
                count--;
            }
        }else{
            count--;
        }

        return count;
    }

    private int mGetNowMoveItem(PointF pos){
        int nowItem = -1;
        for (int i = 0; i < this.getCount(); i++) {
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            RectF rect = new RectF(r.getLeft(), r.getTop(), r.getRight(), r.getBottom());
            if (rect.contains(pos.x, pos.y)) {
                nowItem = i;
                break;
            }
        }
        return nowItem;
    }

    private void mSetSelection(int start, int end){
        int startX, startY, endX, endY;
        int colNum = getColumnNum();

        startX = start % colNum;
        endX = end % colNum;
        if(startX > endX){
            int tmp = startX;
            startX = endX;
            endX = tmp;
        }

        startY = start / colNum;
        endY = end / colNum;
        if(startY > endY){
            int tmp = startY;
            startY = endY;
            endY = tmp;
        }

        for(int i=0; i<this.getCount(); i++){
            if(!tmpSelectItem[i]){
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
                setItemSelectedState(i, false, r);
            }
        }

        for(int i=startY; i<=endY; i++){
            for(int j=startX; j<=endX; j++){
                int pos = j+i*colNum;
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(pos);
                setItemSelectedState(pos, true, r);
                if(tmpSelectItem[pos]){tmpSelectItem[pos] = false;}
            }
        }
    }
}
