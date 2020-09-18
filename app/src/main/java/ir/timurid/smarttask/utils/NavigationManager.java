package ir.timurid.smarttask.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.function.Consumer;

import ir.timurid.smarttask.R;

public class NavigationManager {
    private static final int NAV_HOST_FRAGMENT = R.id.nav_host_fragment;


    public static <T> Consumer<Integer> navigate(T context) {
        return navigate(context, null);
    }


    public static <T> Consumer<Integer> navigate(T t, Bundle bundle) {
        if (t instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) t;
            return navigationAction -> getNavController(activity).navigate(navigationAction,bundle);
        }


        Fragment fragment = (Fragment) t;

        return navigationAction -> getNavController(fragment).navigate(navigationAction, bundle);
    }

    public static <T> void popBackFrom(T t) {
        if ( t instanceof FragmentActivity)
            getNavController( (FragmentActivity) t).popBackStack();
        else
            getNavController( (Fragment) t).popBackStack();
    }

    public static NavController getNavController(Fragment fragment) {
        return NavHostFragment.findNavController(fragment);
    }

    public static NavController getNavController(FragmentActivity activity) {
            return Navigation.findNavController(activity, NAV_HOST_FRAGMENT);
    }
}
