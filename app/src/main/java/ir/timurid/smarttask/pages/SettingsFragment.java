package ir.timurid.smarttask.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.transition.MaterialFadeThrough;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.databinding.LayoutSettingsBinding;
import ir.timurid.smarttask.db.Preferences;

import static androidx.appcompat.app.AppCompatDelegate.*;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;


public class SettingsFragment extends Fragment {

    private LayoutSettingsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialFadeThrough());
        setExitTransition(new MaterialFadeThrough());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LayoutSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureWaterfallTodoAddSwitch();

        checkLanguageButton();
        binding.languageToggleGroup.addOnButtonCheckedListener(this::onLanguageChanged);

        checkThemeButton();
        binding.themeToggleGroup.addOnButtonCheckedListener(this::onThemeChanged);
    }

    private void configureWaterfallTodoAddSwitch() {
        boolean enable = Preferences.getWaterfallTodoAdd(requireContext());
        binding.waterfallAddSwitch.setChecked(enable);
        binding.waterfallAddSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> Preferences.setWaterfallTodoAdd(requireContext(),isChecked));
    }

    private void onLanguageChanged(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        String languageLocaleId = Preferences.getLanguage(requireContext());
        String currentLocale = group.getCheckedButtonId() == R.id.languageFaBtn ? Preferences.FA : Preferences.EN;

        if (languageLocaleId.equals(currentLocale)) return;

        Preferences.setLanguage(requireContext(), currentLocale);
        requireActivity().recreate();
    }

    public void checkLanguageButton() {
        String languageId = Preferences.getLanguage(requireContext());
        int id = languageId.equals(Preferences.FA) ? R.id.languageFaBtn : R.id.languageEnBtn;
        binding.languageToggleGroup.check(id);
    }

    private void onThemeChanged(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        int themeMode = Preferences.getThemeMode(requireContext());

        int currentMode = (group.getCheckedButtonId() == R.id.lightThemeBtn) ? MODE_NIGHT_NO :
                ((group.getCheckedButtonId() == R.id.darkThemeBtn) ? MODE_NIGHT_YES : MODE_NIGHT_FOLLOW_SYSTEM);

        if (currentMode == themeMode) return;

        int mode;
        switch (group.getCheckedButtonId()) {
            case R.id.lightThemeBtn:
                mode = MODE_NIGHT_NO;
                break;
            case R.id.darkThemeBtn:
                mode = MODE_NIGHT_YES;
                break;
            default:
                mode = MODE_NIGHT_FOLLOW_SYSTEM;
        }
        setDefaultNightMode(mode);
        Preferences.setThemeMode(requireContext(),mode);

    }

    public void checkThemeButton() {
        int defaultNightMode = Preferences.getThemeMode(requireContext());
        int id;
        switch (defaultNightMode) {
            case MODE_NIGHT_NO:
                id = R.id.lightThemeBtn;
                break;
            case MODE_NIGHT_YES:
                id = R.id.darkThemeBtn;
                break;
            default:
                id = R.id.systemThemeBtn;
        }

        binding.themeToggleGroup.check(id);
    }
}