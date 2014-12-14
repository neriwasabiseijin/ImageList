package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by neriwasabiseijin on 2014/12/14.
 */
public class myGridView_LongTap extends GridView{

    public myGridView_LongTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("longtap", "hogeeeeeeeee");

        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setSelection(position);
                Log.i("longtap", position + "");

               // CheckBox c = (CheckBox) findViewById(R.id.list_selected);
                //c.setChecked(true);
                return true;
            }
        });

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelection(position);
                Log.i("tap", position + "");

            }
        });

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

}
