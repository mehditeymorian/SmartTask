package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.utils.Delay;
import ir.timurid.smarttask.utils.NavigationManager;


public class SplashFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_splash, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        Delay.forTime(2000).andThen(() -> {
            NavOptions.Builder options = new NavOptions.Builder();
            options.setPopUpTo(R.id.splashFragment,true);

            NavigationManager.getNavController(this).navigate(R.id.navigation_splash_todoList, null,options.build());
        });
    }
}