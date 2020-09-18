package ir.timurid.smarttask;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.timurid.smarttask.databinding.ActivityMainBinding;
import ir.timurid.smarttask.db.Preferences;
import ir.timurid.smarttask.pages.AddTodoBottomSheet;
import ir.timurid.smarttask.utils.LocaleManager;
import ir.timurid.smarttask.utils.NavigationManager;
import ir.timurid.smarttask.utils.VMProvider;
import ir.timurid.smarttask.viewModel.AddTodoVM;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Inject
    AddTodoBottomSheet addTodoBottomSheet;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.wrapContext(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(Preferences.getThemeMode(this));
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setParent(this);



        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        NavigationManager.getNavController(this).addOnDestinationChangedListener(this::onDestinationChanged);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addTodoBottomSheet.init();
    }

    public void showAddTodoLayout(View view) {
        addTodoBottomSheet.show();
        binding.addTodoBtn.hide();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        // avoid cycle between Bottom Navigation destinations
        // keep TodoListFragment In BackStack and remove others in each navigation

        boolean inclusive = item.getItemId() == R.id.todoListFragment;
        int destinationId = R.id.todoListFragment;
        NavOptions.Builder options = new NavOptions.Builder();

        // because categoriesFragment doesn't have an active state in BottomSheet, avoiding from clearing backStack is necessary.
        if (item.getItemId() != R.id.categoriesFragment)
            options.setPopUpTo(destinationId, inclusive);

        NavigationManager.getNavController(this).navigate(item.getItemId(), null, options.build());

        // categoriesFragment doesn't have an active state in BottomSheet
        return item.getItemId() != R.id.categoriesFragment;
    }

    private void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {

        if (destination.getId() == R.id.todoDetailFragment || destination.getId() == R.id.splashFragment) {
            binding.bottomNavigationView.setVisibility(View.GONE);
        } else if (binding.bottomNavigationView.getVisibility() == View.GONE) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
        }


        if (destination.getId() == R.id.todoListFragment && addTodoBottomSheet.isHidden())
            binding.addTodoBtn.show();
        else binding.addTodoBtn.hide();

        // addTodoBottomSheet Visibility
        boolean isDestTodoDetail = destination.getId() == R.id.todoDetailFragment;
        if (isDestTodoDetail) addTodoBottomSheet.hide();


        Menu menu = binding.bottomNavigationView.getMenu();
        MenuItem item = menu.findItem(destination.getId());
        if (item != null)
            item.setChecked(true);

        
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        addTodoBottomSheet.onDestroy();
    }


    public static MainActivity get(Fragment fragment) {
        return ((MainActivity) fragment.requireActivity());
    }
}