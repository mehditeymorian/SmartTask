package ir.timurid.smarttask.extra;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;

import ir.timurid.smarttask.R;

public class BindingAdapters {
    public static int[] prioritiesIconsRes =
            {R.drawable.ic_priority_high,
                    R.drawable.ic_priority_normal,
                    R.drawable.ic_priority_low};


    @BindingAdapter("app:priorityLevel")
    public static void setPriorityLevel(Chip chip, int level) {
        @DrawableRes int icon = prioritiesIconsRes[level];

        Context context = chip.getContext();

        Drawable iconDrawable = ContextCompat.getDrawable(context, icon);

        int colorTint = context.getResources().getIntArray(R.array.prioritiesColors)[level];

        if (iconDrawable != null)
            iconDrawable.setTint(colorTint);

        chip.setChipIcon(iconDrawable);
    }
}
