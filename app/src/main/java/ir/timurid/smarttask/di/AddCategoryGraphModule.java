package ir.timurid.smarttask.di;


import androidx.fragment.app.Fragment;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import ir.timurid.smarttask.adapter.ColorPickerAdapter;
import ir.timurid.smarttask.pages.ColorPickerModal;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddCategoryVM;

@Module
@InstallIn(FragmentComponent.class)
public class AddCategoryGraphModule {

    @Provides
    public static AddCategoryVM provideAddCategoryViewModel(Fragment fragment) {
        AddCategoryVM androidModel = VMProvider.getAndroidModel(fragment, VMProvider.CATEGORIES_GRAPH, AddCategoryVM.class);
        androidModel.setEditCategory(fragment.getArguments());
        return androidModel;
    }



    @Provides
    public static ColorPickerAdapter provideColorPickerAdapter(Fragment fragment) {
        return new ColorPickerAdapter((ColorPickerModal) fragment);
    }

}
