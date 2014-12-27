package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

/**
 * Created by neriwasabiseijin on 2014/12/21.
 */
public class myGridView_DoubleTap  extends GridView {
    private MainActivity myMainActivity;

    // 一行の画像の数
    public int columnNum = 0;
    // DoubleTap取得のためにGestureDetector使用
    GestureDetector gestureDetector;

    public myGridView_DoubleTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        myInit(context);
        gestureDetector = new GestureDetector(context, onGestureListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        columnNum = getColumnNum();

        //myMainActivity.selectedItemView = new ArrayList<Object>();
        for(int i=0; i<this.getCount(); i++){
           // myMainActivity.selectedItemView.add(this.getItemAtPosition(i));
        }

        for(int i=0; i<this.getCount(); i++){
            //RelativeLayout r = (RelativeLayout)myMainActivity.selectedItemView.get(i);
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            setItemSelectedState(i, false, r);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    // 初期化
    private void myInit(Context context){
        myMainActivity = (MainActivity)context;
        myMainActivity.setSelectionMode(MainActivity.MODE_NORMAL);

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

    public void setSelectedItemLength(){
        myMainActivity.selectedItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
        myMainActivity.selectedItem[position] = state;
        if(state){
            itemView.setBackgroundColor(Color.rgb(80, 80, 240));
        }else{
            itemView.setBackgroundColor(Color.rgb(255, 255, 255));
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

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        gestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            if (myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)) {
                myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);
            }
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {}
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false;}
        @Override
        public void onShowPress(MotionEvent e) {}
        @Override
        public boolean onDown(MotionEvent e) {return false;}
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {return false;}
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {return false;}
    };

    private int mGetNowMoveItem(PointF pos){
        int nowItem = -1;
        for (int i = 0; i < this.getCount(); i++) {
            //RelativeLayout r = (RelativeLayout) myMainActivity.selectedItemView.get(i);
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            RectF rect = new RectF(r.getLeft(), r.getTop(), r.getRight(), r.getBottom());
            if (rect.contains(pos.x, pos.y)) {
                // Log.i("me", "contain:" + r.getId());
                nowItem = i;
                break;
            }
        }
        return nowItem;
    }

}
