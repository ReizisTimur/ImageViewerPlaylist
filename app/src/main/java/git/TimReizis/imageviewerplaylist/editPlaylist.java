package git.TimReizis.imageviewerplaylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static androidx.core.app.ShareCompat.getCallingPackage;
import static java.security.AccessController.getContext;


public class editPlaylist extends AppCompatActivity implements Removeble{
    UriAndNameList playlistAL = new UriAndNameList();
    Boolean edited = false;
    String playlistName;
    ArrayAdapter<String> playlistAdapter;
    private static final int READ_REQUEST_CODE = 1337;
    Bundle bundle;
    Uri urL = null;
    int selected;
    ListView playlistLV;
    Bundle saveState = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);
        setTitle("Select playlist");
        playlistLV = (ListView) findViewById(R.id.viewPlaylist);
        playlistLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        playlistAdapter = new ArrayAdapter<String>(this, R.layout.list_item_ed, playlistAL.NameList);
        playlistLV.setAdapter(playlistAdapter);
        bundle = getIntent().getExtras();
        playlistName= bundle.getString("playlist");
        setTitle("Edit "+playlistName);
        updatePlaylist();
        testArrayList();
        playlistLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected =i;
                colorSelect();
                deleteMenuEdit dme = new deleteMenuEdit();
                dme.show(getSupportFragmentManager(), "deletemenu");


                return true;
            }
        });
        playlistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = i;
                colorSelect();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("UriAndName", playlistAL);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        playlistAL = (UriAndNameList) savedInstanceState.getSerializable("UriAndName");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editplaylist_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==R.id.openFile){
            performFileSearch();
        }
        if(item.getItemId()==R.id.sendPlaylist){
            StringBuilder sendStr= new StringBuilder();

            for(String temp :playlistAL.NameList){
                sendStr.append(temp+"\n");
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,sendStr.toString());
            Intent ChooserIntent = Intent.createChooser(intent, "Send message ViaVia...");
            startActivity(ChooserIntent);
        }
        if(item.getItemId()==R.id.editImage){
            String sendImg = playlistAL.getAllUris().get(selected);
            Uri uriImage = Uri.parse(sendImg);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uriImage);
            startActivity(Intent.createChooser(intent, "Send image"));
        }
        return true;
    }


    public void performFileSearch() {
        //открытие файла через меню
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        String uriString = "";
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            if (resultData != null) {
                urL = resultData.getData();
                //сохранить разрешение
               getContentResolver().takePersistableUriPermission(urL, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                uriString = urL.toString();
                Log.d("MYLOG","URISTRING "+uriString);
                playlistAL.add2(uriString, this);
                testArrayList();
                edited=true;
                setTitle("Edit "+playlistName+"*");
                playlistAdapter.notifyDataSetChanged();
            }
        }

    }
    void updatePlaylist(){

        //читает плайлист из файла в аррайлист
        FileInputStream fis = null;
        try{
            fis = openFileInput(bundle.getString("playlist"));
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String temp = new String (bytes);
            playlistAL.clear();
            for(String tmp: temp.split("\n")){
                if(!tmp.equals("")){
                    playlistAL.add2(tmp, this);
                }
            }
        }

        catch (Exception ex){Log.e("MYLOG", ex.toString());}
        finally{

            try{
                if(fis!=null)
                    fis.close();
            }
            catch(IOException ex){Log.e("MYLOG", ex.toString());}
        }
        playlistAdapter.notifyDataSetChanged();
    }

    void savePlaylist(){
        if(edited) {
            String result = "";
            for (String str : playlistAL.getAllUris()) {
                result += str + "\n";
            }
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(playlistName, MODE_PRIVATE);
                fos.write(result.getBytes());
            } catch (Exception e) {}
            finally {
                try {
                    if (fos != null) fos.close();
                } catch (Exception e) {}
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            edited=false;
            setTitle("Edit "+playlistName);
        }
    }
    @Override
    public void deleteImage(){
        playlistAL.remove(selected);
        if(selected>=playlistAL.size())selected--;
        playlistAdapter.notifyDataSetChanged();
        edited=true;
        setTitle("Edit "+playlistName+"*");
    }
    public void moveUpImage(View view){

        if(selected>0){
            playlistAL.moveUp(selected);
            selected--;
            playlistLV.setItemChecked(selected, true);
        }
        playlistAdapter.notifyDataSetChanged();
        //фокус плейлиста
        if (selected - 2 <= playlistLV.getFirstVisiblePosition()) {
            playlistLV.setSelection(selected-2);
        }
        edited=true;
        setTitle("Edit "+playlistName+"*");
        testArrayList();
    }
    public void moveDownImage(View view){
        if(selected<playlistAL.getAllUris().size()-1){
            playlistAL.moveDown(selected);
            selected++;
            playlistLV.setItemChecked(selected, true);
        }

        playlistAdapter.notifyDataSetChanged();
        //фокус плейлиста
        if (selected + 2 >= playlistLV.getLastVisiblePosition()) {
            playlistLV.setSelection(selected+2);
        }
        edited=true;
        setTitle("Edit "+playlistName+"*");
        testArrayList();
    }
    public void ShowPlaylist(View view){
        //сохранение и старт просмотра плейлиста
        savePlaylist();
        playlistAL.namePl = playlistName;
        Intent intent = new Intent(this, startViewImages.class);
        if(Build.VERSION.SDK_INT>25) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra("startpage", selected);
        playlistAL.namePl = playlistName;
        intent.putExtra(UriAndNameList.class.getSimpleName(), playlistAL);
        startActivity(intent);
    }
    public void colorSelect(){
        Log.d("MYLOG", "+++FirstChild++++ " +playlistLV.getFirstVisiblePosition());
        Log.d("MYLOG","++++++SelectedItem++++  "+selected);
    }
    void testArrayList(){
        //лог
        int ii = 1;
        String result = "+++++URI++++  ";
        for(String str:playlistAL.UriList){
            result += "["+ii + " " + str + "]";
            ii++;
        }
        result += "\n+++++NAME++++  ";
        ii=1;
        for(String str:playlistAL.NameList){
            result += "["+ii + " " + str + "]";
            ii++;
        }
        Log.d("MYLOG", result);
    }
}
