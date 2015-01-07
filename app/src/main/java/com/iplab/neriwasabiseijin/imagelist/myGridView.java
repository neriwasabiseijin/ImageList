package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

/**
 * Created by neriwasabiseijin on 2014/12/14.
 */
public class myGridView extends GridView{
    private MainActivity myMainActivity;

    // 何点タッチで選択を起動するか
    static int HOLDFINGER = 3;
    private boolean holdFlag;

    // タッチ位置
    private PointF touchPos = new PointF(0f, 0f);

    // マルチタッチ用
    static int[] mPointerID;
    static PointF[] candidateTouchStart; // 3点タッチの開始点候補

    // 範囲選択用
    private int selectionStartItem;
    private int selectionEndItem;

    private boolean[] tmpSelectItem;

    // 一行の画像の数
    public int columnNum = 0;

    public myGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myInit(context);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        columnNum = getColumnNum();

        //myMainActivity.selectedItemView = new ArrayList<Object>();
        /*
        for(int i=0; i<this.getCount(); i++){
            myMainActivity.selectedItemView.add(this.getItemAtPosition(0));
            Log.i("getitem", "count:"+this.getCount()+","+i);
        }
        */

        for(int i=0; i<this.getCount(); i++){
            //RelativeLayout r = (RelativeLayout)myMainActivity.selectedItemView.get(i);
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            setItemSelectedState(i, false, r);
        }

        this.setBackgroundColor(Color.rgb(0,0,0));


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        touchPos = new PointF(ev.getX(), ev.getY());

        /*
        RelativeLayout r = (RelativeLayout)getItemAtPosition(mGetNowMoveItem(touchPos));
        setItemSelectedState(0, true, r);
        */


        setMyPointerId(ev);
        if(myGridViewTouchEvent(ev)) {
            //return super.onTouchEvent(ev);
        }
        return true;
    }

    // 初期化
    public void myInit(Context context){
        myMainActivity = (MainActivity)context;
        myMainActivity.setSelectionMode(MainActivity.MODE_NORMAL);
        holdFlag = false;
        mPointerID = new int[HOLDFINGER];
        candidateTouchStart = new PointF[HOLDFINGER];
        for(int i=0; i<HOLDFINGER; i++){
            mPointerID[i] = -1;
            candidateTouchStart[i] = new PointF(-1, -1);
        }

        selectionStartItem = -1;
        selectionEndItem = -1;

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
                    if(!getItemSelectedState(position)) {
                        setItemSelectedState(position, true, view);
                    }else{
                        setItemSelectedState(position, false, view);
                    }
                }
            }
        });
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

        switch(action){
            case MotionEvent.ACTION_DOWN:
                return myGridViewActionDown(ev);
            case MotionEvent.ACTION_POINTER_DOWN:
                return myGridViewActionPointerDown(ev);
            case MotionEvent.ACTION_MOVE:
                return myGridViewActionMove(ev);
            case MotionEvent.ACTION_POINTER_UP:
                return myGridViewActionPointerUp(ev);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return myGridViewActionUp(ev);
        }
        return true;
    }

    private boolean myGridViewActionDown(MotionEvent ev){
        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
            return true;
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            return true;
        }
        return true;
    }
    private boolean myGridViewActionPointerDown(MotionEvent ev){
        int count = ev.getPointerCount();
        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
            if (count == HOLDFINGER) {
                holdFlag = true;
            }
            return true;
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            if (count == HOLDFINGER) {
                holdFlag = true;
            }
            return false;
        }
        return true;
    }
    private boolean myGridViewActionMove(MotionEvent ev){
        int count = ev.getPointerCount();
        if(count == 1){
            if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
                return true;
            }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
                if(holdFlag) {
                    PointF pos = new PointF(ev.getX(), ev.getY());
                    generateFakeEvent(pos, MotionEvent.ACTION_DOWN);
                    if (selectionStartItem == -1) {
                        selectionStartItem = mGetNowMoveItem(pos);
                    }
                    // Layoutの関係で-1が返ってくるかもしれないので対策
                    int tmpItem = mGetNowMoveItem(pos);
                    if (tmpItem >= 0) {
                        selectionEndItem = tmpItem;
                    }

                    //Log.i("pos", selectionStartItem+","+selectionEndItem);

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
    private boolean myGridViewActionPointerUp(MotionEvent ev){
        int count = ev.getPointerCount();
        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)) {
            // ここで1本残っているのは，countが2のとき
            if (count == 2 && holdFlag) {
                myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);

                // 残っている指の下にタッチダウンを発生させる
                for(int i=0; i<HOLDFINGER; i++){
                    if(mPointerID[i] != -1){
                        int mPointerIndex = ev.findPointerIndex(mPointerID[i]);
                        PointF p = new PointF(ev.getX(mPointerIndex), ev.getY(mPointerIndex));
                        generateFakeEvent(p, MotionEvent.ACTION_DOWN);
                        if(selectionStartItem == -1) {
                            selectionStartItem = mGetNowMoveItem(p);
                            selectionEndItem = selectionStartItem;
                            if(selectionStartItem != -1 && selectionEndItem != -1) {
                                mSetSelection(selectionStartItem, selectionEndItem);
                            }
                        }
                        break;
                    }
                }
                return false;
            }
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            if (count == 2 && holdFlag) {
                // 残っている指の下にタッチダウンを発生させる
                for(int i=0; i<HOLDFINGER; i++){
                    if(mPointerID[i] != -1){
                        int mPointerIndex = ev.findPointerIndex(mPointerID[i]);
                        PointF p = new PointF(ev.getX(mPointerIndex), ev.getY(mPointerIndex));
                        generateFakeEvent(p, MotionEvent.ACTION_DOWN);
                        if(selectionStartItem == -1) {
                            selectionStartItem = mGetNowMoveItem(p);
                        }
                        break;
                    }
                }

                for(int i=0; i<myMainActivity.selectedItem.length; i++){
                    tmpSelectItem[i] = myMainActivity.selectedItem[i];
                }
                return false;
            }
            return false;
        }
        return true;
    }
    private boolean myGridViewActionUp(MotionEvent ev){
        holdFlag = false;

        if(myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)){
            return true;
        }else if(myMainActivity.checkSelectionMode(MainActivity.MODE_SELECTION)){
            PointF p = new PointF(ev.getX(), ev.getY());
            if(selectionStartItem != -1 || selectionEndItem != -1) {
                selectionStartItem = -1;
                selectionEndItem = -1;
                return false;
            }

            //generateFakeEvent(p, MotionEvent.ACTION_DOWN);
            int tmp =  mGetNowMoveItem(p);
            if(tmp != -1) {
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(tmp);
                setItemSelectedState(tmp, !getItemSelectedState(tmp), r);
            }
            return true;
        }

        return true;
    }

    private void generateFakeEvent(PointF pos, int action){
        long downTime = SystemClock.uptimeMillis();
        long eventTime = downTime + 1;
        MotionEvent mEvent = MotionEvent.obtain(downTime, eventTime, action, pos.x, pos.y, 0);
        super.onTouchEvent(mEvent);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setSelectedItemLength(){
        myMainActivity.selectedItem = new boolean[this.getCount()];
        tmpSelectItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
        myMainActivity.selectedItem[position] = state;
        if(state){
            itemView.setBackgroundColor(Color.rgb(80, 80, 240));
        }else{
            //itemView.setBackgroundColor(Color.rgb(255, 255, 255));
            itemView.setBackgroundColor(Color.rgb(0,0,0));
            if(myMainActivity.testModeFlag) {
                myMainActivity.mShowQuestion(position, itemView);
            }
            /*
            for(int i=0; i<myMainActivity.questionStartPoint.length; i++){
                if(myMainActivity.questionStartPoint[i] == position){
                    itemView.setBackgroundColor(Color.rgb(255,100,100));
                    break;
                }
            }
            */
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
            //RelativeLayout r = (RelativeLayout) myMainActivity.selectedItemView.get(i);
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
                //RelativeLayout r = (RelativeLayout)myMainActivity.selectedItemView.get(i);
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
                setItemSelectedState(i, false, r);
            }
        }

        for(int i=startY; i<=endY; i++){
            for(int j=startX; j<=endX; j++){
                int pos = j+i*colNum;
                //RelativeLayout r = (RelativeLayout)myMainActivity.selectedItemView.get(pos);
                RelativeLayout r = (RelativeLayout) this.getItemAtPosition(pos);
                setItemSelectedState(pos, true, r);
                if(tmpSelectItem[pos]){tmpSelectItem[pos] = false;}
            }
        }



    }
}
