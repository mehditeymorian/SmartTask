package ir.timurid.smarttask.utils;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.function.Consumer;

import ir.timurid.smarttask.R;

public class NavigationManager {
    private static final int NAV_HOST_FRAGMENT = R.id.activity_fragmentHolder;


    public static <T> Consumer<Integer> navigate(T context) {
        return navigate(context, null);
    }


    public static <T> Consumer<Integer> navigate(T t, Bundle bundle) {
        if (t instanceof Activity) {
            Activity activity = (Activity) t;
            return navigationAction -> getNavController(activity).navigate(navigationAction,bundle);
        }


        Fragment fragment = (Fragment) t;

        return navigationAction -> getNavController(fragment).navigate(navigationAction, bundle);
    }

    public static <T> void popBackFrom(T t) {
        if ( t instanceof Activity)
            getNavController( (Activity) t).popBackStack();
        else
            getNavController( (Fragment) t).popBackStack();
    }

    public static NavController getNavController(Fragment fragment) {
        return NavHostFragment.findNavController(fragment);
    }

    public static NavController getNavController(Activity activity) {
        return Navigation.findNavController(activity, NAV_HOST_FRAGMENT);
    }
}
