package com.iplab.neriwasabiseijin.imagelist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    static final int TEST_DEBUG = -1;
    static final int TEST_GRIDVIEW = 0;
    static final int TEST_LONG = 1;

    static boolean debugFlag = false;
    public int testMode = 0;

    // 現在の状態
    static final int MODE_NORMAL = 0;
    static final int MODE_SELECTION = 1;
    public int selectionMode;

    public int touchItem;

    private ActionBar actionBar;
    private Menu myMenu;

    private myGridView_LongTap gridView_longTap;
    private myGridView gridView;

@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debugFlag = false;
        setTestMode();

        actionBar = getSupportActionBar();
        actionBar.setTitle("ImageGridView");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch(id){
            case R.id.menu_copy:
                clickMenuCopy();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTestMode(){
        Intent intent = getIntent();
        testMode = Integer.parseInt(intent.getStringExtra("MODE"));
        if(Integer.parseInt(intent.getStringExtra("DEBUG")) == 0){
            debugFlag = true;
        }

        if(testMode == TEST_LONG){
            setContentView(R.layout.activity_main_long);
            gridView_longTap = (myGridView_LongTap)findViewById(R.id.myGridView_longTap);
            gridView_longTap.setAdapter(new MyImageAdapter(this));
            gridView_longTap.setSelectedItemLength();
        }else{
            gridView = (myGridView)findViewById(R.id.myGridView);
            gridView.setAdapter(new MyImageAdapter(this));
            gridView.setSelectedItemLength();
        }
    }

    public boolean checkSelectionMode(int mode){
        if(selectionMode == mode){
            return true;
        }
        return false;
    }

    public void setSelectionMode(int mode){
        if(selectionMode != mode) {
            selectionMode = mode;
            changeActionBar();
        }
    }

    public void changeActionBar(){
        int bgColor = R.color.action_bar_bg_black;

        myMenu.clear();
        switch (selectionMode){
            case MODE_NORMAL:
                getMenuInflater().inflate(R.menu.menu_main, myMenu);
                break;
            case MODE_SELECTION:
                bgColor = R.color.action_bar_bg_blue;
                getMenuInflater().inflate(R.menu.menu_main_selected, myMenu);
                break;
        }
        Drawable bgDrawable = getApplicationContext().getResources().getDrawable(bgColor);
        actionBar.setBackgroundDrawable(bgDrawable);
    }

    public void clickMenuCopy(){
        if(testMode == TEST_LONG){
            for(int i=0; i<gridView_longTap.selectedItem.length; i++){
                if(gridView_longTap.getItemSelectedState(i)){
                    gridView_longTap.setItemSelectedState(i, false, findViewById(i));
                }
            }
        }else{
            for(int i=0; i<gridView.selectedItem.length; i++){
                if(gridView.getItemSelectedState(i)){
                    gridView.setItemSelectedState(i, false, findViewById(i));
                }
            }
        }
        setSelectionMode(MODE_NORMAL);
    }

}
