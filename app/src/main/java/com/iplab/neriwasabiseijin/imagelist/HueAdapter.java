package com.iplab.neriwasabiseijin.imagelist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by neriwasabiseijin on 2014/12/09.
 */
public class HueAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] mHueArray = {
            "FF4040", "FFCF40", "9FFF40", "40FF6F",
            "40FFFF", "406FFF", "9F40FF", "FF40CF"
    };
    private Integer[] mHueIdArray = {
            R.drawable.hue_ff4040,
            R.drawable.hue_ffcf40,
            R.drawable.hue_9fff40,
            R.drawable.hue_40ff6f,
            R.drawable.hue_40ffff,
            R.drawable.hue_406fff,
            R.drawable.hue_9f40ff,
            R.drawable.hue_ff40cf,
    };
    private ArrayList<Bitmap> mImageArray;
    private static class ViewHolder{
        public ImageView hueImageView;
        //public TextView hueTextView;
    }

    public  HueAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mImageArray = loadThumbnails();
    }

    public int getCount(){
        //return mHueArray.length;
        return mImageArray.size();
    }

    public Object getItem(int position){
        return mHueArray[position];
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.grid_item_hue, null);
            holder = new ViewHolder();
            holder.hueImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
            //holder.hueTextView = (TextView)convertView.findViewById(R.id.hue_textview);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //holder.hueImageView.setImageResource(mHueIdArray[position]);
        holder.hueImageView.setImageBitmap(mImageArray.get(position));
        //holder.hueTextView.setText(mHueArray[position]);

        return convertView;
    }

    private ArrayList<Bitmap> loadThumbnails(){
        ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor c = cr.query(uri, null, null, null, null);

        c.moveToFirst();
        for(int i=0; i<c.getCount(); i++){
            long id = c.getLong(c.getColumnIndexOrThrow("_id"));
            Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            list.add(bmp);
            c.moveToNext();
        }

        return list;
    }

}
