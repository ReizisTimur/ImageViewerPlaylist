package git.TimReizis.imageviewerplaylist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class createPlaylistMenu extends DialogFragment {
    EditText name;
    createPlaylistInt createObj;
    Boolean addVar;
    String nameForEdit = "";
    createPlaylistMenu(Boolean input){
        addVar = input;
    }
    public void onAttach(Context context){
        super.onAttach(context);
        createObj = (createPlaylistInt) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!addVar) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
//
            return adb.setTitle("Create playlist and...")
                    .setView(inflater.inflate(R.layout.createplaylistmenu, null))
                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            name = (EditText) getDialog().findViewById(R.id.enter);
                            createObj.addToPlaylist(name.getText().toString());
                            createObj.closeTask();
                        }
                    })
                    .setNeutralButton("show", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            name = (EditText) getDialog().findViewById(R.id.enter);
                            createObj.addToPlaylist(name.getText().toString());
                            createObj.showForEdit(name.getText().toString());
                        }
                    })
                    .create();

        }
        else{
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//
            return adb.setTitle("Add to playlist and...")
                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            createObj.addToPlaylist(nameForEdit);
                            createObj.closeTask();
                        }
                    })
                    .setNeutralButton("show", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            createObj.addToPlaylist(nameForEdit);
                            createObj.showForEdit(nameForEdit);
                        }
                    })
                    .create();
        }
    }

}

