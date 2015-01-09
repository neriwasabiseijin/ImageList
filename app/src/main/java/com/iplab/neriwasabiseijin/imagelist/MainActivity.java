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
    public boolean[] beforeSelectedItem;

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

        csv += getNowTime() + "," + "TEST_APP_START" + "\n";

        myInit();
        if(testModeFlag) {

            mQuestionInit();

            /*
            Button expStartButton = (Button)findViewById(R.id.button_testStart);
            expStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    if (testMode == TEST_LONG) {
                        for (int i = 0; i < gridView_longTap.getCount(); i++) {
                            RelativeLayout r = (RelativeLayout) gridView_longTap.getItemAtPosition(i);
                            gridView_longTap.setItemSelectedState(i, false, r);
                        }
                    } else if (testMode == TEST_DOUBLE) {
                        for (int i = 0; i < gridView_doubleTap.getCount(); i++) {
                            RelativeLayout r = (RelativeLayout) gridView_doubleTap.getItemAtPosition(i);
                            gridView_doubleTap.setItemSelectedState(i, false, r);
                        }
                    } else {
                        for (int i = 0; i < gridView.getCount(); i++) {
                            gridView.setVisibility(View.VISIBLE);
                            RelativeLayout r = (RelativeLayout) gridView.getItemAtPosition(i);
                            //gridView.setItemSelectedState(i, false, r);
                            mShowQuestion(i, r);
                            Log.i("hoge", "i"+i);
                        }
                    }
                }
            });
            */
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

        switch(testMode){
            case TEST_GRIDVIEW:
                csv += getNowTime() + "," + "TEST_MODE" + "," + "3POINT" + "\n";
                break;
            case TEST_2POINT:
                csv += getNowTime() + "," + "TEST_MODE" + "," + "2POINT" + "\n";
                break;
            case TEST_LONG:
                csv += getNowTime() + "," + "TEST_MODE" + "," + "LONG" + "\n";
                break;
            case TEST_DOUBLE:
                csv += getNowTime() + "," + "TEST_MODE" + "," + "DOUBLE" + "\n";
                break;
        }

        if(Integer.parseInt(intent.getStringExtra("DEBUG")) == 0){
            csv += getNowTime() + "," + "DEBUGMODE" + "\n";
            debugFlag = true;
        }

        //Log.i("testmode", "mode:"+testMode+",debugF:"+debugFlag);
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

//            gridView.setVisibility(View.INVISIBLE);

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

            switch (mode){
                case MODE_NORMAL:
                    csv += getNowTime() + "," + "MODE_NORMAL" + "\n";
                    break;
                case MODE_SELECTION:
                    csv += getNowTime() + "," + "MODE_SELECTION" + "\n";
                    break;
            }

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
                csv += getNowTime() + "," + "ACTIONBAR_OPEN" + "\n";
                break;
        }
        Drawable bgDrawable = getApplicationContext().getResources().getDrawable(bgColor);
        actionBar.setBackgroundDrawable(bgDrawable);
    }

    public void clickMenuCopy(){
        csv += getNowTime() + "," + "COPY_BUTTON_CLICKED" + "\n";

        csv += getNowTime() + "," + "SELECTED_ITEMS";
        for(int i=0; i<selectedItem.length; i++){
            if(selectedItem[i]){csv += "," + i;}
        }
        csv += "," + "END\n";

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

        String testtype = "";
        switch (testMode){
            case TEST_GRIDVIEW:
                testtype = "3point";
                break;
            case TEST_2POINT:
                testtype = "2point";
                break;
            case TEST_LONG:
                testtype = "long";
                break;
            case TEST_DOUBLE:
                testtype = "double";
                break;
        }

        String filename = testtype + "_" + subjectName + "_" + sdf.format(date);
        String file = sd_root + "/IPLAB/ImageList/" + subjectName + "/" + filename + ".csv";

        csv += getNowTime() + "," + "CSV_CREATE" + "," + filename + ".csv" + "\n";

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
        csv += getNowTime() + "," + "QUESTION_SET";
        for(int i=0; i<question.length; i++){
            csv += "," + question[i];
        }
        csv += "\n";

        mSetQuestion();



        /*
        for(int i=0; i<question.length; i++){
            mSetQuestion();
            nowQuestion++;
        }
        */
    }
    public void mShowQuestion(int pos, View itemView){
        //Log.i("item", itemView.toString());
        if(!questionEndFlag) {
            if (questionPos[pos]) {
                int startPos = question[nowQuestion] % 9;
                if (pos == questionStartPoint[startPos]) {
                    itemView.setBackgroundColor(Color.rgb(240, 80, 80));
                    //Log.i("showQ", pos+",red");
                } else {
                    itemView.setBackgroundColor(Color.rgb(240, 240, 0));
                    //Log.i("showQ", pos+",y");
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

        actionBar.setTitle((nowQuestion+1) + "/" + question.length);

        csv += getNowTime() + "," + "QUESTION" + "," + nowQuestion + "," + questionStartPoint[startPos] + "," + width+"*"+width + "\n";
        csv += getNowTime() + "," + "QUESTION_ITEM";
        for(int i=0; i< questionPos.length; i++){
            if(questionPos[i]){
                csv += "," + i;
            }
        }
        csv += "," + "END\n";

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
            csv += getNowTime() + "," + "ANSWER" + "," + "CORRECT" + "\n";
            mNextQuestion();
        }else{
            csv += getNowTime() + "," + "ANSWER" + "," + "FALSE" +"\n";
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
        if(!questionEndFlag){
            mExportCSV();
        }
        questionEndFlag = true;

        // インテントのインスタンス生成
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        // 値引き渡しの設定
        intent.putExtra("SUBJECTNAME", subjectName);

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
