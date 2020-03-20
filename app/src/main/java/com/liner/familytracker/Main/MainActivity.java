package com.liner.familytracker.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.MapFragment;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Main.Fragments.MapViewFragment;
import com.liner.familytracker.Main.Fragments.SettingsFragmnet;
import com.liner.familytracker.R;
import com.liner.familytracker.Utils.FragmentAdapter;

import java.util.ArrayList;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends CoreActivity {
    private ViewPager mainPager;
    private SmoothBottomBar appBar;
    private FragmentAdapter fragmentAdapter;

    private MapFragment map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPager = findViewById(R.id.mainViewPager);
        appBar = findViewById(R.id.mainAppBar);
        ArrayList<Class<? extends Fragment>> pages = new ArrayList<>();
        pages.add(MapViewFragment.class);
        pages.add(SettingsFragmnet.class);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),this, pages);
        mainPager.setAdapter(fragmentAdapter);
        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                appBar.setActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        appBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int i) {
                mainPager.setCurrentItem(i, true);
            }
        });
        //map = ((MapViewFragment) getFragmentManager().findFragmentById(R.id.mapView));

    }

    @Override
    public void onUserDataChanged(UserModel userModel) {

    }

    @Override
    public void onUserLoggedOut() {

    }

    @Override
    public void onUserSignedIn() {

    }
}
