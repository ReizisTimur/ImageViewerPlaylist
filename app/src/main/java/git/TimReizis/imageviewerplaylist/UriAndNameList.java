package git.TimReizis.imageviewerplaylist;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;



public class UriAndNameList  implements Serializable {
    //класс для редактирования плейлистов
    ArrayList<String> UriList = new ArrayList<>();
    ArrayList<String> NameList = new ArrayList<>();
    String namePl;
    UriAndNameList(){clear(); }

    public void add2(String input, Activity input2){

        UriList.add(input);
        String name;
        name=getRealPath(input2, input);
        int i = NameList.size()+1;
        name = i+" "+name;
        NameList.add(name);
    }

    public ArrayList<String> getAllUris(){
        return UriList;
    }
    public ArrayList<String> getAllNames(){
        return NameList;
    }

    public String remove(int i){
        String result = NameList.get(i);
        UriList.remove(i);
        NameList.remove(i);
        recountNames();
        return result;
    }


    public void clear() {
        UriList.clear();
        NameList.clear();
    }
    public void moveUp(int i){

        String uriTemp = UriList.get(i-1);
        String nameTemp = NameList.get(i-1);
        UriList.set(i-1, UriList.get(i));
        NameList.set(i-1, NameList.get(i));
        UriList.set(i, uriTemp);
        NameList.set(i, nameTemp);
        recountNames();
    }
    public void moveDown(int i){
        String uriTemp = UriList.get(i+1);
        String nameTemp = NameList.get(i+1);
        UriList.set(i+1, UriList.get(i));
        NameList.set(i+1, NameList.get(i));
        UriList.set(i, uriTemp);
        NameList.set(i, nameTemp);
        recountNames();
    }
    public int size (){
        return UriList.size();
    }
    void recountNames(){
        int i = 1;
        for(String str: NameList){
            NameList.set(i - 1, i+str.replaceAll("\\A\\d+",""));
            i++;
        }
    }
    String getRealPath(final Context context, final String input) {
        Uri uri = Uri.parse(input);
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.d("MYLOG", "NAME FROM CONTENT " + result);
            } else {
                result = input.replaceAll("^.+\\/", "").replaceAll("\\.+.{3}", "").replaceAll("%20", " ");
                Log.d("MYLOG", "NAME FROM PATH " + result);
            }
        } catch (Exception e) {
            Log.e("MYLOG", "ERROR NAME!! " + e.toString());
        } finally {
            if (cursor != null) cursor.close();
        }
        return result;
    }
}



