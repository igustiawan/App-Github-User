package com.dicoding.picodiploma.githubuserapi.adapter;

import android.content.Context;
import android.os.Bundle;
import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.fragment.FollowersFragment;
import com.dicoding.picodiploma.githubuserapi.fragment.FollowingFragment;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private UserModel userModel;

    public SectionsPagerAdapter(Context context, FragmentManager fm,UserModel userModel) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = context;
        this.userModel = userModel;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FollowersFragment();
                Bundle bundleFollower = new Bundle();
                bundleFollower.putString(FollowersFragment.EXTRA_FOLLOWERS, userModel.getUsername());
                fragment.setArguments(bundleFollower);
                break;
            case 1:
                fragment = new FollowingFragment();
                Bundle bundleFollowing = new Bundle();
                bundleFollowing.putString(FollowingFragment.EXTRA_FOLLOWING, userModel.getUsername());
                fragment.setArguments(bundleFollowing);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
