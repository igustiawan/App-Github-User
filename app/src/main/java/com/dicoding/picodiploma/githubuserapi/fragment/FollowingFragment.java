package com.dicoding.picodiploma.githubuserapi.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.activity.DetailActivity;
import com.dicoding.picodiploma.githubuserapi.adapter.UserAdapter;
import com.dicoding.picodiploma.githubuserapi.model.UserModel;
import com.dicoding.picodiploma.githubuserapi.viewmodel.FollowingViewModel;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    public static String EXTRA_FOLLOWING = "extra_following";
    private ProgressBar progressBar;
    private UserAdapter adapter;
    private RecyclerView rvUserFollowing;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar_following);
        showLoading(true);
        rvUserFollowing = view.findViewById(R.id.rvUserFollowing);
        rvUserFollowing.setHasFixedSize(true);

        FollowingViewModel followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        if(getArguments() != null){
            String username = getArguments().getString(FollowingFragment.EXTRA_FOLLOWING);
            followingViewModel.setUserFollowing(username);
            followingViewModel.getListFollowing().observe(getViewLifecycleOwner(), new Observer<ArrayList<UserModel>>() {
                @Override
                public void onChanged(ArrayList<UserModel> userModels) {
                    adapter.setData(userModels);
                    showLoading(false);
                }
            });
            showRecyclerUser();
            moveIntent();
        }
    }

    private void moveIntent(){
        adapter.setOnItemClickCallback(new UserAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(UserModel userModel) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, userModel);
                startActivity(intent);
            }
        });
    }

    private void showRecyclerUser(){
        rvUserFollowing.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        rvUserFollowing.setAdapter(adapter);
    }

    private void showLoading(boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
