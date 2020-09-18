package ir.timurid.smarttask.pages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.timurid.smarttask.adapter.ColorPickerAdapter;
import ir.timurid.smarttask.databinding.LayoutColorPickerModalBinding;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddCategoryVM;


@AndroidEntryPoint
public class ColorPickerModal extends BottomSheetDialogFragment implements ColorPickerAdapter.OnItemClickListener {

    private LayoutColorPickerModalBinding binding;

    @Inject
    ColorPickerAdapter adapter;

    @Inject
    AddCategoryVM viewModel;


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