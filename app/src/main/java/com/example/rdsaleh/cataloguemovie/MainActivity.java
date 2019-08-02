package com.example.rdsaleh.cataloguemovie;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rdsaleh.cataloguemovie.fragment.FavoriteFragment;
import com.example.rdsaleh.cataloguemovie.fragment.NowPlayingFragment;
import com.example.rdsaleh.cataloguemovie.fragment.SearchMovieFragment;
import com.example.rdsaleh.cataloguemovie.fragment.UpcomingFragment;

public class MainActivity extends AppCompatActivity {

    private NowPlayingFragment nowPlayingFragment;
    private UpcomingFragment upComingFragment;
    private FavoriteFragment favoriteFragment;
    private SearchMovieFragment searchFragment;

    private int navTab = 1;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_nowPlaying:
                    setTitle(R.string.now_playing);
                    fragment = new NowPlayingFragment();
                    navTab = 1;
                    break;
                case R.id.navigation_upcoming:
                    setTitle(R.string.up_coming);
                    fragment = new UpcomingFragment();
                    navTab = 2;
                    break;
                case R.id.navigation_favorite:
                    setTitle(R.string.favorite);
                    fragment = new FavoriteFragment();
                    navTab = 3;
                    break;
                case R.id.navigation_search:
                    setTitle(R.string.search_movie);
                    fragment = new SearchMovieFragment();
                    navTab = 4;
                    break;
            }
            loadFragment();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.now_playing);
        setContentView(R.layout.activity_main);

        nowPlayingFragment = new NowPlayingFragment();
        upComingFragment   = new UpcomingFragment();
        favoriteFragment   = new FavoriteFragment();
        searchFragment     = new SearchMovieFragment();
        mFragmentManager   = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) {
            Fragment f = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            if (f instanceof NowPlayingFragment) {
                nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 1;
            }else if(f instanceof UpcomingFragment) {
                upComingFragment = (UpcomingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 2;
            }else if(f instanceof FavoriteFragment) {
                favoriteFragment = (FavoriteFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 3;
            }else if(f instanceof SearchMovieFragment) {
                searchFragment = (SearchMovieFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
                navTab = 4;
            }
        }

        loadFragment();

    }

    private void loadFragment(){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (navTab) {
            case 1:
                mFragmentTransaction.replace(R.id.fl_container, nowPlayingFragment, NowPlayingFragment.class.getSimpleName());
                break;
            case 2:
                mFragmentTransaction.replace(R.id.fl_container, upComingFragment, UpcomingFragment.class.getSimpleName());
                break;
            case 3:
                mFragmentTransaction.replace(R.id.fl_container, favoriteFragment, FavoriteFragment.class.getSimpleName());
                break;
            case 4:
                mFragmentTransaction.replace(R.id.fl_container, searchFragment, SearchMovieFragment.class.getSimpleName());
                break;
        }
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if(item.getItemId() == R.id.action_settings){
            Intent mIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        switch (navTab) {
            case 1:
                getSupportFragmentManager().putFragment(outState, "fragment", nowPlayingFragment);
                break;
            case 2:
                getSupportFragmentManager().putFragment(outState, "fragment", upComingFragment);
                break;
            case 3:
                getSupportFragmentManager().putFragment(outState, "fragment", favoriteFragment);
                break;
            case 4:
                getSupportFragmentManager().putFragment(outState, "fragment", searchFragment);
                break;
        }
    }
}
