package ir.timurid.smarttask.extra;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;

import ir.timurid.smarttask.R;
import ir.timurid.smarttask.db.Preferences;

public class BindingAdapters {
    public static int[] prioritiesIconsRes =
            {R.drawable.ic_priority_high,
                    R.drawable.ic_priority_normal,
                    R.drawable.ic_priority_low};


    @BindingAdapter("priorityLevel")
    public static void setPriorityLevel(Chip chip, int level) {
        @DrawableRes int icon = prioritiesIconsRes[level];

        Context context = chip.getContext();

        Drawable iconDrawable = ContextCompat.getDrawable(context, icon);

        int colorTint = context.getResources().getIntArray(R.array.prioritiesColors)[level];

        if (iconDrawable != null)
            iconDrawable.setTint(colorTint);

        chip.setChipIcon(iconDrawable);
    }


    @BindingAdapter("animResource")
    public static void setAnimResource(LottieAnimationView animationView, @RawRes int res) {
        animationView.setAnimation(res);
    }

    @BindingAdapter("priorityValue")
    public static void setPriorityValue(TextView chip, int priority) {
        Resources resources = chip.getResources();
        String priorityTitle = resources.getString(R.string.title_priority);
        String priorityValue = resources.getStringArray(R.array.priorities)[priority];
        String result = String.format("%s %s", priorityTitle, priorityValue);
        chip.setText(result);
    }
}
