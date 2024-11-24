package com.example.boost.intro;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SlideFragment2();
            case 1:
                return new SlideFragment3();
            case 2:
                return new SlideFragment4();
            case 3:
                return new SlideFragment5();
            case 4:
                return new SlideFragment6();
            default:
                return new SlideFragment2();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
