package com.terminato.moneymanager.ui.newaccount;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.MoneyStore;
import com.terminato.moneymanager.ui.accounts.FragmentAccounts;

/**
 * Új számla hozzáadását lehetpvé tevő oldal.
 */
public class FragmentNewAccount extends Fragment {

    private EditText moneyAmount; //A számlán levő pénz beírását lehetővé tevő edittext
    private EditText accountName; //A számla neve (későbbiekben típusként van számontartva) beírását lehetővé tevő edittext
    private Spinner currency; //Valuta választést lehetővé tevő legördülő lista
    private Button addNew; //új hozzáadás gomb
    private Button save; //mentés gomb

    private View root; //gyökér

    private MoneyStore moneyStore; //Egy adott számlát leíró objektum

    /* valuták */
    private String currencies[] = {
            "Ft"
    };

    private int selectedCurrency = 0; //A kiválasztott valuta indexe

    public FragmentNewAccount() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_new_account, container, false);

        moneyAmount = root.findViewById(R.id.moneyAmount);
        accountName = root.findViewById(R.id.accountName);
        addNew = root.findViewById(R.id.addNew);
        save = root.findViewById(R.id.save);

        /* Ezen a lapon lehet újat hozzáadni de meglévőt is módosítani és ez az if dönt arról mi van */
        if(FragmentAccounts.savePerAdd == -1) {
            save.setVisibility(View.GONE);
            addNew.setVisibility(View.VISIBLE);
        } else {
            addNew.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);

            moneyStore = MainActivity.users.get(MainActivity.currentUserId).getMoneyStore().get(FragmentAccounts.savePerAdd);
            moneyAmount.setText(moneyStore.getValue() + "");
            accountName.setText(moneyStore.getM_name());
        }

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.users.get( MainActivity.currentUserId ).addMoneyStore(
                        accountName.getText().toString(),
                        accountName.getText().toString(),
                        Float.parseFloat( moneyAmount.getText().toString() ),
                        currencies[selectedCurrency] );

                MainActivity.autoSave();

                Navigation.findNavController(view).navigateUp();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyStore.setValue(Float.parseFloat( moneyAmount.getText().toString() ));
                moneyStore.setM_name(accountName.getText().toString());
                moneyStore.setAlias(accountName.getText().toString());
                moneyStore.setCurrency(currencies[selectedCurrency]);

                MainActivity.autoSave();

                Navigation.findNavController(view).navigateUp();
            }
        });

        currency = root.findViewById(R.id.currency);
        ArrayAdapter<String> adapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_dropdown_item, currencies);
        currency.setAdapter(adapter);

        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCurrency = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }
}