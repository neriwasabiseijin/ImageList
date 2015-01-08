package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Color;
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
public class myGridView_LongTap extends GridView{

    private MainActivity myMainActivity;

    public myGridView_LongTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        myInit(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        //myMainActivity.selectedItemView = new ArrayList<Object>();
        for(int i=0; i<this.getCount(); i++){
            //myMainActivity.selectedItemView.add(this.getItemAtPosition(i));
        }

        for(int i=0; i<this.getCount(); i++){
            //RelativeLayout r = (RelativeLayout)myMainActivity.selectedItemView.get(i);
            RelativeLayout r = (RelativeLayout) this.getItemAtPosition(i);
            setItemSelectedState(i, false, r);
        }

        this.setBackgroundColor(Color.rgb(0,0,0));

    }


    // 初期化
    private void myInit(Context context){
        myMainActivity = (MainActivity)context;
        myMainActivity.setSelectionMode(MainActivity.MODE_NORMAL);

        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (myMainActivity.checkSelectionMode(MainActivity.MODE_NORMAL)) {
                    myMainActivity.setSelectionMode(MainActivity.MODE_SELECTION);
                    //view.setBackgroundColor(Color.rgb(80, 80, 240));
                    setItemSelectedState(position, true, view);
                    myMainActivity.csv += myMainActivity.getNowTime() + "," + "LONG_TAP" + "," + position + "," + getItemSelectedState(position) + "\n";
                }

                return true;
            }
        });

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

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        makeCSVonTouch(ev);
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

    public void setSelectedItemLength(){
        myMainActivity.selectedItem = new boolean[this.getCount()];
        myMainActivity.beforeSelectedItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
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

}
