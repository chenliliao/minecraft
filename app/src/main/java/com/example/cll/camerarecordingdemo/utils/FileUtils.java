package com.example.cll.camerarecordingdemo.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by cll on 2017/12/10.
 */

public class FileUtils {

    public static String createFile(final String path){
        if (path == null){
            return null;
        }
        File file = new File(path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if (!file.exists()){
            try {
                if (file.createNewFile()){
                    Log.w("tag", "test recorderType 111= " + file.getAbsolutePath());
                    return file.getAbsolutePath();
                }else{
                    Log.w("tag", "test recorderType 1222= " + "null");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String[] str = path.split("\\.");
            String newPath = "";
            if (str.length >= 2){
                for (String s : str){
                    if (TextUtils.equals(s, str[str.length - 2])){
                        newPath += str[str.length - 2] + 1 + ".";
                    }else{
                        newPath += s;
                    }
                }
                return createFile(newPath);
            }else{
                return null;
            }
        }
        return null;
    }
}
