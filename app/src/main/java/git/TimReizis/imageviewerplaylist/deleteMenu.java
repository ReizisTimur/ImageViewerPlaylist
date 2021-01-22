package git.TimReizis.imageviewerplaylist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
public class deleteMenu extends DialogFragment {
    String name;
    ArrayList ArLst;
    ArrayAdapter<String> ArLsAd;
    int runDelete = 0;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dmBuilder = new AlertDialog.Builder(getActivity());
        return dmBuilder
                .setTitle("Really delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                        getActivity().deleteFile(name);
                        MainActivity.deletePlaylist(ArLst,name, ArLsAd);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i) {
                        runDelete =2;
                    }
                })
                .create();
    }
    void setInf(String input, ArrayList input2, ArrayAdapter<String> input3){
        name = input;
        ArLst = input2;
        ArLsAd = input3;


    }

}

