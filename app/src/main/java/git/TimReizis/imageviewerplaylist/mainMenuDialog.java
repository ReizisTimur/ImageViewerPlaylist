package git.TimReizis.imageviewerplaylist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class mainMenuDialog extends DialogFragment {

    String title;
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder mmdBuilder = new AlertDialog.Builder(getActivity());

        return mmdBuilder
                .setNeutralButton("Edit", new DialogInterface.OnClickListener(){
                    //кнопка для редактирования плейлиста
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), editPlaylist.class);
                intent.putExtra("playlist", title);
                startActivity(intent);
            }
        })
                .setTitle(title)
                //.setPositiveButton("Show", null)
                .setNegativeButton("Show", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), viewPlaylist.class);
                        intent.putExtra("playlist", title);
                        startActivity(intent);
                    }
                })
                .create();

    }
    void setInf(String name){
        title=name;
    }
}
