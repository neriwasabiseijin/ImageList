package com.iplab.neriwasabiseijin.imagelist;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by neriwasabiseijin on 2014/12/13.
 */

/* 参考
* http://wada811.blogspot.com/2013/10/get-external-sd-card-path-in-android.html
* http://androidkaihatu.blog.fc2.com/blog-entry-67.html
* http://nobuo-create.net/sdcard-2/
* */
public class Get_SDroot {
    // SDカードのマウント先を得る
    @TargetApi(9)
    public static String getMount_sd() {
        ArrayList<String> mountList = new ArrayList<String>();
        String mount_sdcard = null;

        Scanner scanner = null;

        try {
            // システム設定ファイルにアクセス
            File vold_fstab = new File("/system/etc/vold.fstab");
            scanner = new Scanner(new FileInputStream(vold_fstab));

            // 一行ずつ読み込む
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // dev_mountまたはfuse_mountで始まる行
                if (line.startsWith("dev_mount") || line.startsWith("fuse_mount")) {
                    String pos = line.replaceAll("\t", "").split(" ")[1];
                    if(pos.contains("sdcard")) {
                        // 半角スペースorタブ区切り3つめ(がpath)を取得
                        String path = line.replaceAll("\t", "").split(" ")[2];
                        // 取得したpathを重複しないようにリストに登録
                        if (!mountList.contains(path)) {
                            mountList.add(path);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        // Environment.isExternalStorageRemovable()はGINGERBREAD以降しか使えない
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            // getExternalStorageDirectory()が罠であれば，そのpathをリストから除外
            // isExternalStorageRemovable()は取り外し可能かどうかを調べる
            if (!Environment.isExternalStorageRemovable()) {
                mountList.remove(Environment.getExternalStorageDirectory().getPath());
            }
        }

        // マウントされていないpathは除外
        ArrayList<String> tmpList = mountList;
        mountList = new ArrayList<String>();
        for(int i=0; i<tmpList.size(); i++){
            if(isMounted(tmpList.get(i))){
                mountList.add(tmpList.get(i));
            }
        }

        // 除外されずに残ったものがSDカードのマウント先
        if(mountList.size() > 0) {
            mount_sdcard = mountList.get(0);
        }

        // マウント先をreturn(or null)
        return mount_sdcard;
    }

    // 引数のpathがマウントされているかをチェックするメソッド
    private static boolean isMounted(String path){
        boolean isMounted = false;
        Scanner scanner = null;

        try{
            // マウントポイントを取得する
            File mounts = new File("/proc/mounts");
            scanner = new Scanner(new FileInputStream(mounts));
            // マウントポイントに該当するパスがあるかチェックする
            while(scanner.hasNextLine()){
                if(scanner.nextLine().contains(path)){
                    // 該当するパスがあればマウントされている
                    isMounted = true;
                    break;
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        }finally{
            if(scanner!= null){
                scanner.close();
            }
        }

        // マウント状態をreturn
        return isMounted;
    }
}
