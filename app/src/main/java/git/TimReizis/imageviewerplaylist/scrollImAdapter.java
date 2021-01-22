package git.TimReizis.imageviewerplaylist;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class scrollImAdapter extends FragmentStateAdapter {
    boolean first = true;
    scrollImAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position){
//        if (first){
//            position=startViewImages.startpage;
//        }
//        first=false;
        return scrollImage.newInstant(position);
    }
    @Override
    public int getItemCount(){

        return viewPlaylist.playlistMas.length;
    }
}
