package git.TimReizis.imageviewerplaylist;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class deleteMenuEdit extends DialogFragment{
    private Removeble removeble;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        removeble = (Removeble) context;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder dme = new AlertDialog.Builder(getActivity());
        return dme.setTitle("Really delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeble.deleteImage();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}