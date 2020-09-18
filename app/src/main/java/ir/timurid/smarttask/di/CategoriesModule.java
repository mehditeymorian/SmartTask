package ir.timurid.smarttask.di;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import ir.timurid.smarttask.adapter.CategoriesAdapter;
import ir.timurid.smarttask.pages.CategoriesModal;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.CategoriesVM;

import static ir.timurid.smarttask.pages.CategoriesModal.MODE_SELECTION;

@Module
@InstallIn(FragmentComponent.class)
public class CategoriesModule {

    @Provides
    public static CategoriesVM provideViewModel(Fragment fragment) {
        return VMProvider.getAndroidModel(fragment, VMProvider.MAIN_GRAPH, CategoriesVM.class);
    }


    @Provides
    public static CategoriesAdapter provideAdapter(Fragment fragment) {
        return new CategoriesAdapter((CategoriesModal) fragment);
    }

    @Provides
    public static boolean provideIsSelectionModeAvailable(Fragment fragment) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) return false;
        return arguments.getBoolean(MODE_SELECTION, false);
    }

}
