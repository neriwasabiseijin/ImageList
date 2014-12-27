package com.iplab.neriwasabiseijin.imagelist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    static final int IMGNUM = 35;

    static final int TEST_DEBUG = -1;
    static final int TEST_GRIDVIEW = 0;
    static final int TEST_2POINT = 1;
    static final int TEST_LONG = 2;
    static final int TEST_DOUBLE = 3;

    static boolean debugFlag = false;
    public boolean testModeFlag = false;
    public int testMode = 0;

    // 現在の状態
    static final int MODE_NORMAL = 0;
    static final int MODE_SELECTION = 1;
    public int selectionMode;

    //public int touchItem;

    // 選択された場所を保存
    public boolean[] selectedItem;

    private ActionBar actionBar;
    private Menu myMenu;

    private myGridView gridView;
    private myGridView_LongTap gridView_longTap;
    private myGridView_DoubleTap gridView_doubleTap;

    // CSVファイル生成用
    public long startTime;
    public String csv;
    public String subjectName;

    // 問題
    //public ArrayList<Object> selectedItemView;
    public int[] questionStartPoint = {
            0,3,6,
            14,17,20,
            28,31,34
    };
    public int[] question = new int[27];
    public boolean[] questionPos;
    public int nowQuestion;
    public View questionView;
    private boolean questionEndFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myInit();
        if(testModeFlag) {
            mQuestionInit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        myMenu = menu;
        return true;
    }
/*
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);

        if(testModeFlag) {
            mQuestionInit();
        }

    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        switch(id){
            case R.id.menu_copy:
                clickMenuCopy();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void myInit(){
        startTime = System.currentTimeMillis();
        csv = "";

        actionBar = getSupportActionBar();
        actionBar.setTitle("ImageGridView");

        debugFlag = false;
        testModeFlag = false;
        setTestMode();
    }

    public void setTestMode(){
        Intent intent = getIntent();
        testMode = Integer.parseInt(intent.getStringExtra("MODE"));

        if(Integer.parseInt(intent.getStringExtra("DEBUG")) == 0){
            debugFlag = true;
        }

        Log.i("testmode", "mode:"+testMode+",debugF:"+debugFlag);
        if(Integer.parseInt(intent.getStringExtra("TESTFLAG")) == 0) {
            testModeFlag = true;
        }


        subjectName = intent.getStringExtra("SUBJECTNAME");

        if(testMode == TEST_GRIDVIEW){
            gridView = (myGridView)findViewById(R.id.myGridView);
            gridView.setAdapter(new MyImageAdapter(this));
            gridView.setSelectedItemLength();
            gridView.HOLDFINGER = 3;
            gridView.myInit(this);
            questionView = gridView;
        }else if(testMode == TEST_2POINT){
            gridView = (myGridView)findViewById(R.id.myGridView);
            gridView.setAdapter(new MyImageAdapter(this));
            gridView.setSelectedItemLength();
            gridView.HOLDFINGER = 2;
            gridView.myInit(this);
            questionView = gridView;
        }else if(testMode == TEST_LONG) {
            setContentView(R.layout.activity_main_long);
            gridView_longTap = (myGridView_LongTap) findViewById(R.id.myGridView_longTap);
            gridView_longTap.setAdapter(new MyImageAdapter(this));
            gridView_longTap.setSelectedItemLength();
            questionView = gridView_longTap;
        }else if(testMode == TEST_DOUBLE){
            setContentView(R.layout.activity_main_double);
            gridView_doubleTap = (myGridView_DoubleTap) findViewById(R.id.myGridView_doubleTap);
            gridView_doubleTap.setAdapter(new MyImageAdapter(this));
            gridView_doubleTap.setSelectedItemLength();
            questionView = gridView_doubleTap;
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
        if(testModeFlag) {
            mCheckAnswer();
        }
        if(!questionEndFlag) {
            if (testMode == TEST_LONG) {
                for (int i = 0; i < selectedItem.length; i++) {
                    //if (gridView_longTap.getItemSelectedState(i)) {
                    gridView_longTap.setItemSelectedState(i, false, findViewById(i));
                    //}
                }
            } else if (testMode == TEST_DOUBLE) {
                for (int i = 0; i < selectedItem.length; i++) {
                    //if (gridView_doubleTap.getItemSelectedState(i)) {
                    gridView_doubleTap.setItemSelectedState(i, false, findViewById(i));
                    //}
                }
            } else {
                for (int i = 0; i < selectedItem.length; i++) {
                    //if(gridView.getItemSelectedState(i)){
                    gridView.setItemSelectedState(i, false, findViewById(i));
                    //}
                }
            }
            setSelectionMode(MODE_NORMAL);
        }
    }

    public long getNowTime(){
        return System.currentTimeMillis() - startTime;
    }

    public void mExportCSV(){
        String sd_root = Get_SDroot.getMount_sd();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'_'kk_mm_ss");
        String filename = subjectName + "_" + sdf.format(date);
        String file = sd_root + "/IPLAB/ImageList/" + subjectName + "/" + filename + ".csv";

        csv += getNowTime() + "," + "CSV_CREATE" + "," + filename + ".csv";

        File f = new File(file);
        File dir = f.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();    //make folders
        }

        try{
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, true), "UTF-8"
                    )
            );
            bw.write(csv);
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        csv = "";
    }

    private void mArrayShuffleInt(int[] array){
        for(int i=0; i<array.length; i++){
            int num = (int)Math.floor(Math.random() * (i+1));

            int tmp = array[i];
            array[i] = array[num];
            array[num] = tmp;
        }
    }

    private void mQuestionInit(){
        questionPos = new boolean[IMGNUM];
        nowQuestion = 0;
        for(int i=0; i<question.length; i++){
            question[i] = i;
        }

        questionEndFlag = false;

        mArrayShuffleInt(question);
        mSetQuestion();

        /*
        for(int i=0; i<question.length; i++){
            mSetQuestion();
            nowQuestion++;
        }
        */
    }
    public void mShowQuestion(int pos, View itemView){
        if(!questionEndFlag) {
            if (questionPos[pos]) {
                int startPos = question[nowQuestion] % 9;

                if (pos == questionStartPoint[startPos]) {
                    itemView.setBackgroundColor(Color.rgb(240, 80, 80));
                } else {
                    itemView.setBackgroundColor(Color.rgb(240, 240, 0));
                }
            }
        }
    }
    public void mSetQuestion(){
        int questionType = question[nowQuestion] / 9;
        int startPos = question[nowQuestion] % 9;

        int width = questionType*2+1;

        for(int i=0; i< questionPos.length; i++){
            questionPos[i] = false;
        }

        for(int i=0; i<width; i++){
            for(int j=0; j<width; j++){
                int qnum = (questionStartPoint[startPos] + j) - (startPos%3)*questionType + (i - (startPos/3)*questionType)*7;
                questionPos[qnum] = true;
                //Log.i("hoge", question[nowQuestion]+","+questionType+","+startPos+",,,,"+qnum);
            }
        }
    }
    public void mCheckAnswer(){
        boolean correctFlag = true;
        for(int i=0; i< questionPos.length; i++){
            if(questionPos[i] != selectedItem[i]){
                correctFlag = false;
                break;
            }
        }

        if(correctFlag){
            mNextQuestion();
        }else{
            mBeep();
        }

    }
    public void mNextQuestion(){
        nowQuestion++;
        if(nowQuestion < question.length){
            Log.i("q", nowQuestion+"");
            mSetQuestion();
        }else{
            Log.i("q", "endddd");
            mQuestionEnd();
        }
    }

    public void mQuestionEnd(){
        questionEndFlag = true;

        // インテントのインスタンス生成
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        // 値引き渡しの設定
        //intent.putExtra("MODE", "2");

        // 次の画面のアクティビティ起動
        startActivity(intent);
        // 現在のアクティビティ終了
        MainActivity.this.finish();
    }

    // ビープ音を鳴らす
    public void mBeep(){
        ToneGenerator tG = new ToneGenerator(
                AudioManager.STREAM_SYSTEM,
                ToneGenerator.MAX_VOLUME
        );
        tG.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
    }




}
