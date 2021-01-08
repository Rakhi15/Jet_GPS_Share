package com.oakspro.jetgpsshare.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oakspro.jetgpsshare.ShareFragment;
import com.oakspro.jetgpsshare.R;
import com.oakspro.jetgpsshare.MapFragment;

public class HomeFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        bottomNavigationView=root.findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new MapFragment());
        return root;
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selected_fragment=null;

            switch (item.getItemId()){

                case R.id.mapFragment:
                    openFragment(new MapFragment());
                    return true;
                case R.id.shareFragment:
                    openFragment(new ShareFragment());
                    return true;

            }

            return false;
        }
    };
}