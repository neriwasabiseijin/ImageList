package com.iplab.neriwasabiseijin.imagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by neriwasabiseijin on 2015/01/08.
 */
public class TestStart extends ActionBarActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_teststart);

            Button btn = (Button)findViewById(R.id.button_testStart);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = getIntent();
                    String testMode = intent.getStringExtra("MODE");
                    String debugFlag = intent.getStringExtra("DEBUG");
                    String testFlag = intent.getStringExtra("TESTFLAG");
                    String subjectName = intent.getStringExtra("SUBJECTNAME");

                    // インテントのインスタンス生成
                    Intent next = new Intent(TestStart.this, MainActivity.class);

                    // 値引き渡しの設定
                    next.putExtra("MODE", testMode);
                    next.putExtra("DEBUG", debugFlag);
                    next.putExtra("TESTFLAG", testFlag);
                    next.putExtra("SUBJECTNAME", subjectName);

                    // 次の画面のアクティビティ起動
                    startActivity(next);
                    // 現在のアクティビティ終了
                    TestStart.this.finish();
                }

            });


        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_start, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            /*
            if (id == R.id.action_settings) {
                return true;
            }
            */
            return super.onOptionsItemSelected(item);
        }
}
