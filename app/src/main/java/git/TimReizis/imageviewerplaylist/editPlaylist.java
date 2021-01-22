package git.TimReizis.imageviewerplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class editPlaylist extends AppCompatActivity implements Removeble{
    UriAndNameList playlistAL = new UriAndNameList();
    String playlistName;
    ArrayAdapter<String> playlistAdapter;
    private static final int READ_REQUEST_CODE = 1337;
    Bundle bundle;
    Uri urL = null;
    int selected;
    ListView playlistLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);
        playlistLV = (ListView) findViewById(R.id.viewPlaylist);
        playlistLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playlistAL.getAllNames());
        playlistLV.setAdapter(playlistAdapter);



        bundle = getIntent().getExtras();
        playlistName= bundle.getString("playlist");
        setTitle("Edit "+playlistName);
        updatePlaylist();
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editplaylist_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==R.id.openFile){
            performFileSearch();
        }
        return true;
    }


    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        String uriString = null;
        String tempAL = null;
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            if (resultData != null) {
                urL = resultData.getData();
                uriString = urL.toString()+"\n";
            }
        }
        FileOutputStream fos = null;
        try{
            //uriString = URLDecoder.decode(uriString, StandardCharsets.UTF_8.name());
            fos= openFileOutput(playlistName, MODE_APPEND);
            fos.write(uriString.getBytes());

        }
        catch(Exception e){Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();}
        finally {
            try{
                if(fos!=null) fos.close();
            }
            catch (Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
        }
        updatePlaylist();
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
                playlistAL.add2(tmp, this);
                //display.add(URLDecoder.decode(tmp, StandardCharsets.UTF_8.name()).replaceAll(".+/", ""));
            }
        }

        catch (Exception e){Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();}
        finally{

            try{
                if(fis!=null)
                    fis.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        playlistAdapter.notifyDataSetChanged();

    }

    void savePlaylist(String input){
        FileOutputStream fos = null;
        try{
            fos= openFileOutput(playlistName, MODE_PRIVATE);
            fos.write(input.getBytes());
        }
        catch(Exception e){Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();}
        finally {
            try{
                if(fos!=null) fos.close();
            }
            catch (Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
        }
        updatePlaylist();

    }
    @Override
    public void deleteImage(){
        playlistAL.remove(selected);
        String result = "";
        for(String str: playlistAL.getAllUris()){
            result+=str+"\n";
        }
        savePlaylist(result);
        if(selected>=playlistAL.size())selected--;
        colorSelect();
    }
    public void moveUpImage(View view){
        if(selected>0){
            playlistAL.moveUp(selected);
            String result = "";
            for(String str: playlistAL.getAllUris()){
                result+=str+"\n";
            }
            savePlaylist(result);
            selected--;
            colorSelect();
        }
    }
    public void moveDownImage(View view){
        if(selected<playlistAL.getAllUris().length-1){
            playlistAL.moveDown(selected);
            String result = "";
            for (String str : playlistAL.getAllUris()) {
                result += str + "\n";
            }
            savePlaylist(result);
            selected++;
            colorSelect();
        }
    }
    public void colorSelect(){
        for(int i = 0; i<playlistLV.getChildCount(); i++){
            if(i==selected){
                playlistLV.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorSelect));
            }
            else playlistLV.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorBackgroundLV));
        }
    }

}
