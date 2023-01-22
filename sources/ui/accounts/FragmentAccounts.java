package com.terminato.moneymanager.ui.accounts;

import android.os.Bundle;

import androidx.arch.core.executor.TaskExecutor;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.MoneyEvent;
import com.terminato.moneymanager.core.MoneyStore;
import com.terminato.moneymanager.core.User;

import java.util.ArrayList;

/**
 * A felhasználó számláinak kezelését lehetővé tevő oldal.
 */
public class FragmentAccounts extends Fragment {

    private FloatingActionButton addNewAccount; //A gomb amire kattintva hozzá tudunk adni egy új számlát
    private View root; //A gyökér
    private LinearLayout accountsLayout;
    private TextView totalAmount; //A felhasználó összes pénze ide lesz írva

    private User user; //Egy adott kiválasztott felhasználó

    /* Nem csak új számlát lehet hozzáadni hanem lehet meglévőt módosítani, olyankor egy id van benne különben -1 */
    public static int savePerAdd = -1; //-1 = add, other = id


    public FragmentAccounts() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_accounts, container, false);

        accountsLayout = root.findViewById(R.id.accountsLayout);
        user = MainActivity.users.get(MainActivity.currentUserId);
        totalAmount = root.findViewById(R.id.totalAmount);

        totalAmount.setText(user.getTotalMoney()+" Ft");

        addNewAccount = root.findViewById(R.id.addNewAccount);
        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePerAdd = -1;
                Navigation.findNavController(view).navigate(R.id.action_fragmentAccounts_to_fragmentNewAccount);
            }
        });

        showAccounts();

        return root;
    }

    /**
     * Egy adott sor kinézetét hozza létre. Ezeken vannak tárolva a számlák pl.
     * @param img - egy kép url je (még nincs használva)
     * @param type - a számla típusa amit a felhasználó ír be
     * @param value - a számlán levő pénz értéke
     * @param id - egy azonosító a késöbbi felismeréshez
     * @return - vissza ad egy linear layout-ot amit fel tudunk tenni az ablakra
     */
    private LinearLayout createLayout (String img, String type, float value, int id) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from( root.getContext() ).inflate( R.layout.unit_layout, null );
        layout.setId( id );

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePerAdd = view.getId();
                Navigation.findNavController(view).navigate(R.id.action_fragmentAccounts_to_fragmentNewAccount);
            }
        });

        ImageView icon = layout.findViewById( R.id.icon );
        TextView title = layout.findViewById( R.id.title );
        TextView amount = layout.findViewById( R.id.amount );

        title.setText( type );
        amount.setText( value + " Ft" );

        return layout;
    }

    /**
     * Előszedi majd megjeleníti a felhasználó összes számláját
     */
    private void showAccounts () {
        accountsLayout.removeAllViews();
        ArrayList<MoneyStore> moneyStore = user.getMoneyStore();

        for(int i = 0; i<moneyStore.size(); i++) {
            accountsLayout.addView( createLayout( "kep", moneyStore.get( i ).getM_name(), moneyStore.get( i ).getValue(), i ) );
        }
    }
}