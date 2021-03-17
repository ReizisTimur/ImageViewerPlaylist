package git.TimReizis.imageviewerplaylist;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class scrollImage extends Fragment {

    private int PageNumber;
    public  static scrollImage newInstant(int page){
        scrollImage fragmentSI = new scrollImage();
        Bundle args=new Bundle();
        args.putInt("number", page);
        fragmentSI.setArguments(args);
        return fragmentSI;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        PageNumber = getArguments() != null ? getArguments().getInt("number") : 1;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_scrollimage, container, false);
        ImageView imageView = (ImageView) result.findViewById(R.id.scrollimageView);
        String str= startViewImages.playlistAL.getAllUris().get(PageNumber);
        Uri uri = Uri.parse(str);
        imageView.setImageURI(uri);
        return result;
    }
}
