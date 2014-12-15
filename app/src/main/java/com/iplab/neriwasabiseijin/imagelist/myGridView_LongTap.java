package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by neriwasabiseijin on 2014/12/14.
 */
public class myGridView_LongTap extends GridView{

    public boolean[] selectedItem;
    private MainActivity myMainActivity;

    public myGridView_LongTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        myInit(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
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
                    view.setBackgroundColor(Color.rgb(80, 80, 240));
                    setItemSelectedState(position, true, view);
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
                }
            }
        });
    }

    public void setSelectedItemLength(){
        selectedItem = new boolean[this.getCount()];
    }
    public void setItemSelectedState(int position, boolean state, View itemView){
        selectedItem[position] = state;
        if(state){
            itemView.setBackgroundColor(Color.rgb(80, 80, 240));
        }else{
            itemView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        }
    }
    public boolean getItemSelectedState(int position){
        return selectedItem[position];
    }

}
