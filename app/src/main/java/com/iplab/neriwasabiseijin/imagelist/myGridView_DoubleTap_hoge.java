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
public class myGridView_DoubleTap_hoge extends GridView {
    private MainActivity myMainActivity;

    // 一行の画像の数
    public int columnNum = 0;
    // DoubleTap取得のためにGestureDetector使用
    GestureDetector gestureDetector;

    public myGridView_DoubleTap_hoge(Context context, AttributeSet attrs) {
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

        this.setBackgroundColor(Color.rgb(0,0,0));

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

                    myMainActivity.csv += myMainActivity.getNowTime() + "," + "ITEM_CLICKED" + "," + position + "," + getItemSelectedState(position) + "\n";
                }

            }
        });
    }

    public void setSelectedItemLength(){
        myMainActivity.selectedItem = new boolean[this.getCount()];
        myMainActivity.beforeSelectedItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
        //Log.i("view", itemView+"");
        myMainActivity.selectedItem[position] = state;
        if(state){
            itemView.setBackgroundColor(Color.rgb(80, 80, 240));
        }else{
            //itemView.setBackgroundColor(Color.rgb(255, 255, 255));
            itemView.setBackgroundColor(Color.rgb(0, 0, 0));
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
        makeCSVonTouch(ev);
        gestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private void makeCSVonTouch(MotionEvent ev){
        int count = ev.getPointerCount();
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        int action = ev.getActionMasked();
        String act = "";

        switch (action){
            case MotionEvent.ACTION_DOWN:
                act = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                act = "ACTION_POINTER_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                act = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_POINTER_UP:
                act = "ACTION_POINTER_UP";
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                act = "ACTION_UP";
                break;
        }


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
            //Log.i("flag", changeStateFlag + "");
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



    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            if (myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)) {
                myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);

                PointF pos = new PointF(ev.getX(), ev.getY());
                int item = mGetNowMoveItem(pos);
                if(item != -1) {
                    myMainActivity.csv += myMainActivity.getNowTime() + "," + "DOUBLE_TAP" + "," + item + "\n";
                }
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
