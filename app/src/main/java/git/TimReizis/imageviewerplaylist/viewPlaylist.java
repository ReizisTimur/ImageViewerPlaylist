package git.TimReizis.imageviewerplaylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;

public class viewPlaylist extends AppCompatActivity {
    UriAndNameList playlistAL = new UriAndNameList();
    static String[] playlistMas;
    ArrayAdapter<String> playlistAdapter;
    Bundle bundle;
    String playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewplaylist);
        ListView playlistLV = (ListView) findViewById(R.id.showPlaylist);
        playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playlistAL.getAllNames());
        playlistLV.setAdapter(playlistAdapter);
        bundle = getIntent().getExtras();
        playlistName= bundle.getString("playlist");
        setTitle(playlistName);
        updatePlaylist();
        final Intent intent = new Intent(this, startViewImages.class);
        playlistLV.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                intent.putExtra("startpage", i);
                intent.putExtra("name", playlistName);
                startActivity(intent);
            }
        });
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
            for(String tmp: temp.split("\n"))playlistAL.add2(tmp, this);
            playlistMas = playlistAL.getAllUris();

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
}
