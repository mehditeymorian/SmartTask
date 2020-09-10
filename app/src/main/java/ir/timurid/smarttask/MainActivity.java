package ir.timurid.smarttask;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;

import ir.timurid.smarttask.databinding.ActivityMainBinding;
import ir.timurid.smarttask.pages.AddTodoBottomSheet;
import ir.timurid.smarttask.utils.NavigationManager;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    private AddTodoBottomSheet addTodoBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setParent(this);

        // TODO: 8/16/2020 inject
        addTodoBottomSheet = new AddTodoBottomSheet(binding.addTodoLayout, this);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        NavigationManager.getNavController(this).addOnDestinationChangedListener(this::onDestinationChanged);
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

        NavigationManager.getNavController(this).navigate(item.getItemId(),null,options.build());

        // categoriesFragment doesn't have an active state in BottomSheet
        return item.getItemId() != R.id.categoriesFragment;
    }

    private void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {

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



        binding.bottomNavigationView.setVisibility(destination.getId() == R.id.splashFragment ? View.GONE : View.VISIBLE);

}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        addTodoBottomSheet.onDestroy();
    }
}