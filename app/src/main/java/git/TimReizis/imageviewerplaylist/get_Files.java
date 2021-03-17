package git.TimReizis.imageviewerplaylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class get_Files extends AppCompatActivity implements createPlaylistInt{
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> uris = new ArrayList<>();
    ArrayAdapter menuAddAdapter;
    createPlaylistMenu createMenu;
    Boolean add = true;
    String nameForEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__files);
        setTitle("Select playlist");
        ListView menuAdd = (ListView) findViewById(R.id.listViewAdd);
        menuAddAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        menuAdd.setAdapter(menuAddAdapter);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }
        else Log.e("MYLOG", "NULL URI!!!!");

//        for(int i =0; i < intent.getClipData().getItemCount(); i++) {
//            Uri uri= null;
//            try{
//                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", new File(intent.getClipData().getItemAt(i).getUri().toString()));
//            }
//            catch (Exception e){Log.d("MYLOG",e.toString());}
//            if(uri==null){
//                uri =intent.getClipData().getItemAt(i).getUri();
//                Log.d("MYLOG","URI FROM FILE "+uri);
//            }
//            else {Log.d("MYLOG","URI FROM FILEPROVIDER "+uri);}
//            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            uris.add(uri.toString());


        updateMenu();
        menuAdd.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                if(i ==0){
                    add =false;
                    createMenu = new createPlaylistMenu(add);
                    createMenu.show(getSupportFragmentManager(),"create");
                }
                else{
                    createMenu = new createPlaylistMenu(add);
                    createMenu.nameForEdit = names.get(i);
                    createMenu.show(getSupportFragmentManager(),"add");
                }
            }
        });
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            getContentResolver().takePersistableUriPermission(imageUri,  Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uris.add(imageUri.toString());
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            for (Uri imageUri: imageUris){
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uris.add(imageUri.toString());
            }
        }
    }

    void updateMenu(){
        names.clear();
        names.add("New playlist");
        for(String path: fileList()){
            names.add(path);
        }
        menuAddAdapter.notifyDataSetChanged();

    }
    public void addToPlaylist(String name){
        String result = "";
        if(!add) {
            if (name.equals("")) {
                name = "untitle";
            }
            //подбор имени, если совпадение
            int i = 1;
            while (new File(getApplicationContext().getFilesDir(), name).exists()) {
                while (new File(getApplicationContext().getFilesDir(), name + i).exists()) {
                    i++;
                }
                name += i;

            }
        }
        nameForEdit = name;
        for (String str:uris){
            result += str + "\n";
        }
        FileOutputStream fos = null;
        try{
            fos= openFileOutput(name, MODE_APPEND);
            fos.write(result.getBytes());
        }
        catch (NullPointerException e){
            Log.d("MYLOG","+++Exception+++ "+e);}
        catch(Exception e){
            Log.e("MYLOG",e.toString());}
        finally {
            try{
                if(fos!=null) fos.close();
            }
            catch (Exception e){Log.e("MYLOG",e.toString());}
        }
        Toast.makeText(this, "Add to "+name, Toast.LENGTH_SHORT).show();
    }
    public void showForEdit(String input){
        Intent intent = new Intent(this, editPlaylist.class);
        intent.putExtra("playlist", nameForEdit);
        startActivity(intent);
        finish();
    }
    public void closeTask(){
        finishAffinity();
    }
}
