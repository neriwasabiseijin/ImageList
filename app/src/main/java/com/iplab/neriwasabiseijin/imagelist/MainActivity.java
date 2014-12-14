package com.iplab.neriwasabiseijin.imagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;


public class MainActivity extends ActionBarActivity {
    static final int TEST_DEBUG = -1;
    static final int TEST_GRIDVIEW = 0;
    static final int TEST_LONG = 1;

    static boolean debugFlag = false;
    public int testMode = 0;

    private ActionBar actionBar;
    Menu myMenu;


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
            myGridView_LongTap gridView_longTap = (myGridView_LongTap)findViewById(R.id.myGridView_longTap);
            //GridView gridView_longTap = (GridView)findViewById(R.id.myGridView_longTap);
            gridView_longTap.setAdapter(new MyImageAdapter(this));
        }else{
            GridView gridView = (GridView)findViewById(R.id.myGridView);
            gridView.setAdapter(new MyImageAdapter(this));
        }
    }
}
