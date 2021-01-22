package git.TimReizis.imageviewerplaylist;

import android.app.Activity;
import android.widget.Toast;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UriAndNameList extends ArrayList {
    //класс для редактирования плейлистов
    ArrayList<String> UriList = new ArrayList<>();
    ArrayList<String> NameList = new ArrayList<>();

    public void add2(String input, Activity input2){
        UriList.add(input);
        String name = "!! DECODING ERROR!!";
        try{
            name = URLDecoder.decode(input, StandardCharsets.UTF_8.name()).replaceAll(".+/", "");
        }
        catch (Exception e){
            Toast.makeText(input2, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        NameList.add(name);
    }
    public String[] getAllUris(){
        String[] result =new String[UriList.size()];
        for (int i = 0; i< UriList.size(); i++)result[i] = UriList.get(i);
        return result;
    }
    public ArrayList getAllNames(){
        return NameList;
    }

    public String remove(int i){
        String result = NameList.get(i);
        UriList.remove(i);
        NameList.remove(i);
        return result;
    }

    @Override
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
    }
    public void moveDown(int i){
        String uriTemp = UriList.get(i+1);
        String nameTemp = NameList.get(i+1);
        UriList.set(i+1, UriList.get(i));
        NameList.set(i+1, NameList.get(i));
        UriList.set(i, uriTemp);
        NameList.set(i, nameTemp);
    }

}
