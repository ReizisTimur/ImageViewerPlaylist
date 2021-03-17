package git.TimReizis.imageviewerplaylist;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class scrollImAdapter extends FragmentStateAdapter {
    scrollImAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position){
        return scrollImage.newInstant(position);
    }
    @Override
    public int getItemCount(){

        return startViewImages.playlistAL.getAllUris().size();
    }
}
