package ir.timurid.smarttask.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

import ir.timurid.smarttask.adapter.ColorPickerAdapter;
import ir.timurid.smarttask.databinding.LayoutColorPickerModalBinding;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddCategoryVM;


public class ColorPickerModal extends BottomSheetDialogFragment implements ColorPickerAdapter.OnItemClickListener {
    private LayoutColorPickerModalBinding binding;
    private ColorPickerAdapter adapter;
    private AddCategoryVM viewModel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ColorPickerAdapter(this);
        viewModel = VMProvider.getAndroidModel(this, VMProvider.ADD_CATEGORY_GRAPH, AddCategoryVM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LayoutColorPickerModalBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        binding.recyclerView.setAdapter(adapter);

        adapter.submitList(Arrays.asList(viewModel.getColors()));

    }

    @Override
    public void onItemClick(String color) {
        viewModel.getColorField().set(color);
        NavigationManager.popBackFrom(this);
    }

}