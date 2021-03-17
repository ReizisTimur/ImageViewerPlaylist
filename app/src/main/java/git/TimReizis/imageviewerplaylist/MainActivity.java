package git.TimReizis.imageviewerplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements createPlaylistInt{
    String name;
    EditText namePL;
    ArrayList<String> names = new ArrayList();
    ArrayAdapter<String> playlistsListAdapter;
    deleteMenu dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MYLOG", "Build API "+ Build.VERSION.SDK_INT);
        //посторение главного окна
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        namePL = (EditText) findViewById(R.id.edit_text);
        ListView plyalistsList = (ListView) findViewById(R.id.viewPlaylists);
        playlistsListAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        plyalistsList.setAdapter(playlistsListAdapter);
        //обработка длинного клика по списку плейлистов,
        // меню удаления
        plyalistsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dm = new deleteMenu();
                dm.setInf(names.get(i),names, playlistsListAdapter);
                dm.show(getSupportFragmentManager(), "deletemenu");

                return true;
            }
        });
        //обработка клика по меню, открывается редактирование
        final Intent intent = new Intent(this, editPlaylist.class);
        plyalistsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("playlist", names.get(i));
                startActivity(intent);
            }
        });
        updateListPlaylists();
    }
        public void addToPlaylist(String name){
        //создание плейлиста, чтение имени
        if (name.equals("")){
            name = "untitle";
        }
        //подбор имени, если совпадение
        int i = 1;
        while (new File(getApplicationContext().getFilesDir(), name).exists()){
            while (new File(getApplicationContext().getFilesDir(), name+i).exists()){
                i++;
            }
            name +=i;

        }
        //создание пустого плейлиста
        FileOutputStream fos = null;
        try{
            fos= openFileOutput(name, MODE_PRIVATE);
            fos.write(null);
        }
        catch (NullPointerException e){
            Log.d("MYLOG","+++Exception+++ "+e);}
        catch(Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
        finally {
            try{
                if(fos!=null) fos.close();
            }
            catch (Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
        }
        updateListPlaylists();
    }

    public void createPlaylist(View view){
        //меню создания нового плейлиста
        createPlaylistMA menu = new createPlaylistMA();
        menu.show(getSupportFragmentManager(),"Create");
        updateListPlaylists();
    }
    void updateListPlaylists(){
        //обновление меню выдора плейлистов
         names.clear();
        for(String path: fileList()){
            names.add(path);
        }
         playlistsListAdapter.notifyDataSetChanged();
    }
    static void deletePlaylist(ArrayList<String> input1, String input2, ArrayAdapter<String> input3){
        int i =input1.indexOf(input2);
        input1.remove(i);
        input3.notifyDataSetChanged();

    }
    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
       return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListPlaylists();
    }

    public void showForEdit(String input){}

    @Override
    public void closeTask() {
    }
}
