package com.terminato.moneymanager.ui.details;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.MoneyEvent;
import com.terminato.moneymanager.ui.home.HomeFragment;

/**
 * A felhasználó adott bevételének vagy éppen kiadásának a részletes megjelenítése.
 */
public class FragmentDetails extends Fragment {

    private TextView amount; //pénz összege
    private TextView account; //melyik számlán történt
    private TextView category; //milyen kategóriában történt
    private TextView date; //mikor történt
    private TextView desc; //leírás

    private View root; //gyökér

    private MoneyEvent moneyEvent; //Az adott esemény leíró objektum

    public FragmentDetails() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_details, container, false);

        amount = root.findViewById(R.id.amount);
        account = root.findViewById(R.id.account);
        category = root.findViewById(R.id.category);
        date = root.findViewById(R.id.date);
        desc = root.findViewById(R.id.desc);

        if(HomeFragment.inOrExp == 0) {
            moneyEvent = MainActivity.users.get(MainActivity.currentUserId).getExpenditures().get(HomeFragment.choosenId);
        } else {
            moneyEvent = MainActivity.users.get(MainActivity.currentUserId).getIncomes().get(HomeFragment.choosenId);
        }

        amount.setText(moneyEvent.getValue()+" Ft");
        account.setText(moneyEvent.getPaymentMethod());
        category.setText(moneyEvent.getType());
        date.setText(moneyEvent.getDate());
        desc.setText(moneyEvent.getDesc());

        return root;
    }
}