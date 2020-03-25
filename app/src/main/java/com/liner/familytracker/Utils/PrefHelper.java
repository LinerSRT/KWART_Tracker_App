package com.liner.familytracker.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class PrefHelper {
    private Context context;

    public PrefHelper(Context context) {
        this.context = context;
    }

    public void saveUser(UserModel userModel){
        try {
            File path = new File(context.getApplicationInfo().dataDir+File.separator);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, "klt_"+userModel.getUid());
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(new Gson().toJson(userModel).getBytes());
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public boolean isUserExist(String userUID){
        File path = new File(context.getApplicationInfo().dataDir+File.separator+"klt_"+userUID);
        return path.exists();
    }


    public UserModel getUser(String userUID){
        File path = new File(context.getApplicationInfo().dataDir+File.separator);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, "klt_"+userUID);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Type user = new TypeToken<UserModel>(){}.getType();
        return new Gson().fromJson(new String(bytes), user);
    }
}
