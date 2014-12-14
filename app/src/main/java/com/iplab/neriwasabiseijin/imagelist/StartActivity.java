package com.iplab.neriwasabiseijin.imagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by neriwasabiseijin on 2014/12/14.
 */
public class StartActivity extends ActionBarActivity {
    private boolean isDebug = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);

            CheckBox debug_flag_check = (CheckBox)findViewById(R.id.debug_flag);
            debug_flag_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox c = (CheckBox)v;
                    isDebug = c.isChecked();
                }
            });



            Button btn = (Button)findViewById(R.id.button_grid);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // インテントのインスタンス生成
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    // 値引き渡しの設定
                    intent.putExtra("MODE", "0");
                    if(isDebug) {intent.putExtra("DEBUG", "0");}
                    else{intent.putExtra("DEBUG", "1");}
                    // 次の画面のアクティビティ起動
                    startActivity(intent);
                    // 現在のアクティビティ終了
                    StartActivity.this.finish();
                }

            });

            btn = (Button)findViewById(R.id.button_longTap);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // インテントのインスタンス生成
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    // 値引き渡しの設定
                    intent.putExtra("MODE", "1");
                    if(isDebug) {intent.putExtra("DEBUG", "0");}
                    else{intent.putExtra("DEBUG", "1");}
                    // 次の画面のアクティビティ起動
                    startActivity(intent);
                    // 現在のアクティビティ終了
                    StartActivity.this.finish();
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
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

