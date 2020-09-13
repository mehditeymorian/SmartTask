package ir.timurid.smarttask.utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import ir.timurid.smarttask.R;

import static ir.timurid.smarttask.utils.NavigationManager.getNavController;

public class VMProvider {
    public static final int MAIN_GRAPH = R.id.navigation_mainGraph;
    public static final int ADD_CATEGORY_GRAPH = R.id.navigation_addCategoryGraph;
    public static final int TODO_DETAIL_GRAPH = R.id.navigation_graph_todoDetail;

    public static <T extends ViewModel> T getAndroidModel(Fragment fragment, int navigationGraphId, Class<T> mClass) {
        return getViewModelProvider(fragment, navigationGraphId, getAndroidViewModelFactory(fragment.requireActivity())).get(mClass);
    }

    public static <T extends ViewModel> T getAndroidModel(FragmentActivity activity, int navigationGraphId, Class<T> mClass) {
        return new ViewModelProvider(getNavController(activity).getViewModelStoreOwner(navigationGraphId)).get(mClass);
    }

    public static <T extends ViewModel> T getModel(Fragment fragment, int navigationGraphId, Class<T> mClass) {
        return getViewModelProvider(fragment, navigationGraphId, null).get(mClass);
    }

    public static <T extends ViewModel> T getModelFactory(Fragment fragment, int navigationGraphId, Class<T> mClass, androidx.lifecycle.ViewModelProvider.Factory factory) {
        return getViewModelProvider(fragment, navigationGraphId, factory).get(mClass);
    }

    private static androidx.lifecycle.ViewModelProvider getViewModelProvider(Fragment fragment, int navigationGraphId, androidx.lifecycle.ViewModelProvider.Factory factory) {
        if (factory == null) return new androidx.lifecycle.ViewModelProvider(getViewModelStoreOwner(fragment, navigationGraphId));
        return new androidx.lifecycle.ViewModelProvider(getViewModelStoreOwner(fragment, navigationGraphId), factory);
    }

    private static ViewModelStoreOwner getViewModelStoreOwner(Fragment fragment, int navigationGraphId) {
        return getNavController(fragment).getViewModelStoreOwner(navigationGraphId);
    }


    private static androidx.lifecycle.ViewModelProvider.Factory getAndroidViewModelFactory(Activity activity) {
        return new androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory(activity.getApplication());
    }


}
