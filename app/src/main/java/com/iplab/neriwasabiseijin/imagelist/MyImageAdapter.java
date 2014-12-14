package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by neriwasabiseijin on 2014/12/09.
 */
public class MyImageAdapter extends BaseAdapter{
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
        public ImageView myImageView;
    }

    public MyImageAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mImageArray = loadThumbnails();
    }

    public int getCount(){
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
            holder.myImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.myImageView.setImageBitmap(mImageArray.get(position));

        return convertView;
    }

    private ArrayList<Bitmap> loadThumbnails(){
        ArrayList<Bitmap> list = new ArrayList<Bitmap>();

        String sd_root = Get_SDroot.getMount_sd();
        File dir = new File(sd_root+"/IPLAB/img");
        File[] imgs = dir.listFiles();
        if(imgs != null){
            for(int i=0; i<imgs.length; i++){
                if(imgs[i].isFile()){
                    try{
                        // もし画像が大きかったら縮小して読み込む
                        Bitmap bitmap;
                        FileInputStream fis = new FileInputStream(imgs[i]);
                        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                        imageOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(fis, null, imageOptions);
                        fis.close();

                        fis = new FileInputStream(imgs[i]);

                        int imageSizeMax = 80;
                        float imageScaleWidth = (float)imageOptions.outWidth / imageSizeMax;
                        float imageScaleHeight = (float)imageOptions.outHeight / imageSizeMax;

                        // 縮小できるサイズならば，縮小して読み込む
                        if(imageScaleWidth > 2 && imageScaleHeight > 2){
                            BitmapFactory.Options imageOptions2 = new BitmapFactory.Options();
                            // 縦横，小さい方に縮小するスケールを合わせる
                            int imageScale = (int)Math.floor((imageScaleWidth>imageScaleHeight ? imageScaleHeight:imageScaleWidth));
                            // BitmapFactory.OptionsのinSampleSizeには2のべき乗が入る
                            // imageScaleに最も近く，それ以下の2のべき乗の数を探す
                            for(int j=2; j<=imageScale; j*=2){
                                imageOptions2.inSampleSize = j;
                            }

                            bitmap = BitmapFactory.decodeStream(fis, null, imageOptions2);
                            Log.v("image", "Sample Size: 1/" + imageOptions2.inSampleSize);
                        }else{
                            bitmap = BitmapFactory.decodeStream(fis);
                        }
                        fis.close();
                        list.add(bitmap);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return list;
    }

}
