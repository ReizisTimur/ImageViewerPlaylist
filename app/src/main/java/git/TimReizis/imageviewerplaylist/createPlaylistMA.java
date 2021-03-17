package git.TimReizis.imageviewerplaylist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class createPlaylistMA extends DialogFragment {
    EditText name;
    createPlaylistInt cpma;
    public void onAttach(Context context){
        super.onAttach(context);
        cpma = (createPlaylistInt) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return adb.setView(inflater.inflate(R.layout.createplaylistma,null))
                .setTitle("Save and...")
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        name =(EditText) getDialog().findViewById(R.id.namePL);
                        cpma.addToPlaylist(name.getText().toString());
                    }
                })
                .create();
    }
}
