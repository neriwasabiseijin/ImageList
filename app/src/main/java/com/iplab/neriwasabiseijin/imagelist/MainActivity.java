package com.iplab.neriwasabiseijin.imagelist;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;


public class MainActivity extends ActionBarActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Bitmap> list = loadThumbnails();

        BitmapAdapter adapter = new BitmapAdapter(
                getApplicationContext(),R.layout.list_item,list
        );

        GridView gridView = (GridView)findViewById(R.id.gridView1);
        gridView.setAdapter(adapter);

    }

    private ArrayList<Bitmap> loadThumbnails(){
        ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        ContentResolver cr = getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);
        c.moveToFirst();
        int count = (100<c.getCount()) ? 100 : c.getCount();
        for(int i=0; i<count; i++) {
            long id = c.getLong(c.getColumnIndexOrThrow("_id"));
            Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND ,null);
            list.add(bmp);
            c.moveToNext();
        }
        return list;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = (GridView)findViewById(R.id.gridView1);
        gridView.setAdapter(new HueAdapter(this));
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
}
