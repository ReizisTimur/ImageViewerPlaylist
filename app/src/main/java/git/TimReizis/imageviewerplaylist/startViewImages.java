package git.TimReizis.imageviewerplaylist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class startViewImages extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startviewimages);
        Bundle strPage = getIntent().getExtras();
        final int startpage = strPage.getInt("startpage", 1);
        final String name = strPage.getString("name");
        setTitle(name);
        final ViewPager2 viewPager = (ViewPager2) findViewById(R.id.pager2);
        final FragmentStateAdapter sia = new scrollImAdapter(this);
        viewPager.setAdapter(sia);
        viewPager.setCurrentItem(startpage, false);
    }

}
