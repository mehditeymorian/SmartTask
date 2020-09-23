package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;

import com.google.android.material.transition.MaterialFadeThrough;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.utils.Delay;
import ir.timurid.smarttask.utils.NavigationManager;


public class SplashFragment extends Fragment {


    public static final int SPLASH_DURATION = 2000;
    private TextView splashText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialFadeThrough());
        setExitTransition(new MaterialFadeThrough());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        splashText = view.findViewById(R.id.splashText);
        splashText.setScaleX(0);
        splashText.setScaleY(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        Delay.forTime(SPLASH_DURATION).andThen(() -> {
            NavOptions.Builder options = new NavOptions.Builder();
            options.setPopUpTo(R.id.splashFragment,true);

            NavigationManager.getNavController(this).navigate(R.id.navigation_splash_todoList, null,options.build());
        });

        splashText.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(SPLASH_DURATION/4)
                .withEndAction(() -> {
                    splashText.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(SPLASH_DURATION/2)
                            .start();
                }).start();
    }


}