package com.iplab.neriwasabiseijin.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neriwasabiseijin on 2014/12/09.
 */
public class MyImageAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MainActivity myMainActivity;
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

    private Integer[] mImgIdArray = {
            R.drawable.i1,
            R.drawable.i2,
            R.drawable.i3,
            R.drawable.i4,
            R.drawable.i5,
            R.drawable.i6,
            R.drawable.i7,
            R.drawable.i8,
            R.drawable.i9,
            R.drawable.i10,
            R.drawable.i11,
            R.drawable.i12,
            R.drawable.i13,
            R.drawable.i14,
            R.drawable.i15,
            R.drawable.i16,
            R.drawable.i17,
            R.drawable.i18,
            R.drawable.i19,
            R.drawable.i20,
            R.drawable.i21,
            R.drawable.i22,
            R.drawable.i23,
            R.drawable.i24,
            R.drawable.i25,
            R.drawable.i26,
            R.drawable.i27,
            R.drawable.i28,
            R.drawable.i29,
            R.drawable.i30,
            R.drawable.i31,
            R.drawable.i32,
            R.drawable.i33,
            R.drawable.i34,
            R.drawable.i35,
    };

    private ArrayList<Bitmap> mImageArray;
    private ArrayList<View> mViewList;

    private static class ViewHolder{
        public ImageView myImageView;
        public CheckBox myCheckBox;
    }

    public MyImageAdapter(Context context){
        mViewList = new ArrayList<View>();

        myMainActivity = (MainActivity)context;
        mContext = context;
        mImageArray = loadThumbnails();


        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount(){
        //Log.i("size", mViewList.size()+"");
        //return mViewList.size();
        return mImageArray.size();
    }

    public Object getItem(int position){
        //return mImageArray.get(position);
        //Log.i("getItem", position+",");
        /*このへんあやしい*/
        //return mViewList.get(position+1);
        return (position<0 || mViewList.size()<=position) ? mViewList.get(0) : mViewList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.myImageView = (ImageView)convertView.findViewById(R.id.list_imageview);
            //holder.myCheckBox = (CheckBox)convertView.findViewById(R.id.list_selected);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.myImageView.setImageBitmap(mImageArray.get(position));

        convertView.setId(position);
        boolean setIdFlag = true;
        for(int i=0; i<mViewList.size(); i++){
            if(mViewList.get(i).getId() == position){
                setIdFlag = false;
                break;
            }
        }
        if(setIdFlag){
            mViewList.add(convertView);
            Log.i("add", "convertView:"+convertView.getId()+","+mViewList.size()+","+position+","+mImageArray.size());
        }

        return convertView;
    }

    private ArrayList<Bitmap> loadThumbnails(){
        ArrayList<Bitmap> list = new ArrayList<Bitmap>();

        String sd_root = Get_SDroot.getMount_sd();
        File dir = new File(sd_root+"/IPLAB/img");
        File[] imgs = dir.listFiles();

        if(!myMainActivity.debugFlag) {
            /*
            if (imgs != null) {
                for (int i = 0; i < imgs.length; i++) {
                    if (imgs[i].isFile()) {
                        try {
                            // もし画像が大きかったら縮小して読み込む
                            Bitmap bitmap;
                            FileInputStream fis = new FileInputStream(imgs[i]);
                            BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                            imageOptions.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream(fis, null, imageOptions);
                            fis.close();

                            fis = new FileInputStream(imgs[i]);

                            int imageSizeMax = 80;
                            float imageScaleWidth = (float) imageOptions.outWidth / imageSizeMax;
                            float imageScaleHeight = (float) imageOptions.outHeight / imageSizeMax;

                            // 縮小できるサイズならば，縮小して読み込む
                            if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                                BitmapFactory.Options imageOptions2 = new BitmapFactory.Options();
                                // 縦横，小さい方に縮小するスケールを合わせる
                                int imageScale = (int) Math.floor((imageScaleWidth > imageScaleHeight ? imageScaleHeight : imageScaleWidth));
                                // BitmapFactory.OptionsのinSampleSizeには2のべき乗が入る
                                // imageScaleに最も近く，それ以下の2のべき乗の数を探す
                                for (int j = 2; j <= imageScale; j *= 2) {
                                    imageOptions2.inSampleSize = j;
                                }

                                bitmap = BitmapFactory.decodeStream(fis, null, imageOptions2);
                                //Log.v("image", "Sample Size: 1/" + imageOptions2.inSampleSize);
                            } else {
                                bitmap = BitmapFactory.decodeStream(fis);
                            }
                            fis.close();
                            list.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            */
            for (int i = 0; i < 35; i++) {
                list.add(BitmapFactory.decodeResource(mContext.getResources(), mImgIdArray[i]));
            }
        }else{
            for (int i = 0; i < imgs.length; i++) {
                list.add(BitmapFactory.decodeResource(mContext.getResources(), mHueIdArray[i % mHueIdArray.length]));
            }
        }

        //return list;

        ArrayList<Bitmap> testList35 = new ArrayList<Bitmap>();
        for(int i=0; i<35; i++){
            testList35.add(list.get(i));
        }
        return testList35;
    }
}
