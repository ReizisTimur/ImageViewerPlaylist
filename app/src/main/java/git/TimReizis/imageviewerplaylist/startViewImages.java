package git.TimReizis.imageviewerplaylist;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.net.URL;
import java.util.ArrayList;


public class startViewImages extends AppCompatActivity {
    static  UriAndNameList playlistAL = new UriAndNameList();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startviewimages);
        Bundle arguments = getIntent().getExtras();
        playlistAL = (UriAndNameList) arguments.getSerializable(UriAndNameList.class.getSimpleName());
        final int startpage = arguments.getInt("startpage", 1);
        setTitle(playlistAL.namePl);
        final ViewPager2 viewPager = (ViewPager2) findViewById(R.id.pager2);
        final FragmentStateAdapter sia = new scrollImAdapter(this);
        viewPager.setAdapter(sia);
        viewPager.setCurrentItem(startpage, false);
    }

}
