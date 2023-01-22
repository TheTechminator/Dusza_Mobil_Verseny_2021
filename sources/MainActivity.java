package com.terminato.moneymanager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.terminato.moneymanager.core.CreateJSONFormat;
import com.terminato.moneymanager.core.FileHandler;
import com.terminato.moneymanager.core.MoneyEvent;
import com.terminato.moneymanager.core.MoneyStore;
import com.terminato.moneymanager.core.ReadUsersData;
import com.terminato.moneymanager.core.User;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static ConstraintLayout navHeaderLayout;

    public static ArrayList<User> users = new ArrayList<User>(); //A felhasználókat tartalmazza
    public static int currentUserId = 0; //A jelenleg kiválasztott felhasználónak az id-ja

    public static FileHandler fileHandler; //Fájlkezelő
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.userProfile, R.id.fragmentStatistics, R.id.fragmentAccounts, R.id.fragmentCategories, R.id.fragmentRegularPayments, R.id.fragmentReminders, R.id.fragmentCurrency, R.id.fragmentSettings)
                .setDrawerLayout( drawer )
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        context = this;

        initNavigationHeader();


        try {
            ReadUsersData readUsersData = new ReadUsersData( this, "user.json" );
            users = readUsersData.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Alapértelmezett adatokat ad a rendszerhez. Csak újjonnan telepítéskor fog lefutni.*/
        if(users.size() == 0) {
            users.add( new User("Ferenc", "nincs", "feri@gmail.com", new ArrayList<MoneyEvent>(), new ArrayList<MoneyEvent>(), new ArrayList<MoneyStore>() ) );
            users.get( 0 ).addIncome("Fizetés", "Kaptam pénzt", "Ft", 12000f, "", "Készpénz");
            users.get( 0 ).addExpenditure("Busz", "Vettem egy buszjegyet", "Készpénz", "Ft", 120f, "");
            users.get( 0 ).addMoneyStore("Készpénz", "Készpénz", 12000f, "Ft");

            try {
                FileHandler fileHandler = new FileHandler( this );
                fileHandler.writeToFile("user.json", new CreateJSONFormat(users).getJSONArray().toString() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        refreshNavigationHeader();

        fileHandler = new FileHandler( this );
    }

    /**
     * Meg lehet bárhonnan hívni mivel statikus és ki fogja írni egy fájlba az összes adatot.
     */
    public static void autoSave () {
        try {
            fileHandler.writeToFile("user.json", new CreateJSONFormat(users).getJSONArray().toString() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshNavigationHeader();
    }

    /**
     * A navigációs fejlécet inicializálja. Ad neki egy click eseményt,
     * ha a felhasználó rákkattint tudja a profilját módosítani.
     */
    private void initNavigationHeader () {
        navHeaderLayout = ( ConstraintLayout ) navigationView.getHeaderView(0 );
        navHeaderLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if( onSupportNavigateUp() ) {
                    navController.navigateUp();
                }

                navController.navigate( R.id.userProfile );
                drawer.close();
            }
        } );
    }

    /**
     * Frissíti a navigációs fejlécet. Akkor fontos amikor a felhasználó neve vagy egyenlege megváltozik.
     */
    public static void refreshNavigationHeader () {
        TextView userName = navHeaderLayout.findViewById(R.id.userName);
        TextView userBalance = navHeaderLayout.findViewById(R.id.userBalance);

        userName.setText(users.get(currentUserId).getUserDisplayName());
        userBalance.setText("Egyenleg: " + users.get(currentUserId).getTotalMoney() + " Ft");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}