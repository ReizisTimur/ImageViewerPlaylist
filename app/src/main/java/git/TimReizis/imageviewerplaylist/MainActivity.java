package git.TimReizis.imageviewerplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {
    String name;
    EditText namePL;
    ArrayList<String> names = new ArrayList();
    ArrayAdapter<String> playlistsListAdapter;
    deleteMenu dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //посторение главного окна
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namePL = (EditText) findViewById(R.id.edit_text);
        ListView plyalistsList = (ListView) findViewById(R.id.viewPlaylists);
        playlistsListAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        plyalistsList.setAdapter(playlistsListAdapter);

        //обработка клика по списку плейлистов, открывается всплывающее меню выбора
        // между редактированием и просмотром
        plyalistsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dm = new deleteMenu();
                dm.setInf(names.get(i),names, playlistsListAdapter);
                dm.show(getSupportFragmentManager(), "deletemenu");

                return true;
            }
        });
        plyalistsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //плейлист для редактирования
                name = names.get(i);
                //запуск меню
                mainMenuDialog mmd = new mainMenuDialog();
                mmd.setInf(name);
                mmd.show(getSupportFragmentManager(), "mainmenu");
            }
        });
        updateListPlaylists();
    }
        public void createPlaylist(View view){
        //создание плейлиста, чтение имени
        String name = namePL.getText().toString();
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
        namePL.setText("");
        FileOutputStream fos = null;
        try{
            fos= openFileOutput(name, MODE_PRIVATE);
            fos.write("".getBytes());
        }
        catch(Exception e){Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();}
        finally {
            try{
                if(fos!=null) fos.close();
            }
            catch (Exception e){Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();}
        }
        updateListPlaylists();
    }
     void updateListPlaylists(){
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


}
