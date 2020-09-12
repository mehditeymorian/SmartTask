package ir.timurid.smarttask.viewModel;

import android.app.Application;
import android.os.Bundle;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.db.Repository;
import ir.timurid.smarttask.model.Category;
import lombok.Getter;
import lombok.Setter;

import static ir.timurid.smarttask.pages.AddCategoryModal.EDIT_MODE;
import static ir.timurid.smarttask.pages.AddCategoryModal.EDIT_MODE_COLOR;
import static ir.timurid.smarttask.pages.AddCategoryModal.EDIT_MODE_ID;
import static ir.timurid.smarttask.pages.AddCategoryModal.EDIT_MODE_TITLE;

public class AddCategoryVM extends AndroidViewModel {
    public static final int TITLE_MAX_LENGTH = 30;
    private Repository repository;


    @Getter
    @Setter
    private ObservableField<String> titleField = new ObservableField<>();
    @Getter
    @Setter
    private ObservableField<String> colorField = new ObservableField<>();

    @Getter
    private String[] colors;

    private Bundle editBundle;


    public AddCategoryVM(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        colors = application.getResources().getStringArray(R.array.category_colors);
        colorField.set(colors[0]);
    }


    public void insertCategory() {
        String title = getTitleField().get();
        if (title != null) title = title.trim();

        String color = getColorField().get();
        if (color != null) color = color.trim();

        Category category = new Category();
        category.setTitle(title);
        category.setColor(color);

        if (isEditMode()) {
            category.setCategoryId(getEditId());
            repository.updateCategory(category);
        } else repository.insertCategory(category);
    }

    public static boolean isTitleAcceptable(Editable editable) {
        String title = editable.toString();
        title = title.trim();
        return title.length() <= TITLE_MAX_LENGTH && !title.isEmpty();
    }


    //region Edit Mode
    public void setEditBundle(Bundle editBundle) {
        this.editBundle = editBundle;

        if (editBundle == null) return;

        String title = editBundle.getString(EDIT_MODE_TITLE, null);
        titleField.set(title);
        String color = editBundle.getString(EDIT_MODE_COLOR, "#000000");
        colorField.set(color);
    }

    public boolean isEditMode() {
        if (editBundle == null) return false;
        return editBundle.getBoolean(EDIT_MODE, false);
    }

    public long getEditId() {
        if (editBundle == null)
            return -1;
        return editBundle.getLong(EDIT_MODE_ID, -1);
    }

    public void clearStates() {
        editBundle = null;
    }
    //endregion

}
