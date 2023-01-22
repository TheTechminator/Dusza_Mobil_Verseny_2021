package com.terminato.moneymanager.ui.newinorexp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.User;
import com.terminato.moneymanager.ui.home.HomeFragment;

/**
 * Új kiadás vagy bevétel feljegyzését lehetővé tevő oldal.
 */
public class FragmentAddIncomeOrExpenditure extends Fragment {

    /* Megjelenő elemek */
    private EditText moneyAmount;
    private Spinner account;
    private Spinner category;
    private Button date;
    private EditText desc;
    private Button addNew;

    private View root; //gyökér
    private User user; //kiválasztott felhasználó

    /* jelenleg választható kiadás kategóriák (későbbiekben fájból lenne beolvsva) */
    private String[] categories = {
            "Élelmiszer",
            "Tisztítószer",
            "Üzemanyag",
            "Rezsi",
            "Javítás",
            "Karbantartás",
            "Biztosítás",
            "Szórakozás"
    };

    /* jelenleg választható bevétel kategóriák (későbbiekben fájból lenne beolvsva) */
    private String[] incoms = {
            "Rendszeres fizetés",
            "Alkalmi munka",
            "Ajándék",
            "Nyeremény"
    };

    public FragmentAddIncomeOrExpenditure() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_income_or_expenditure, container, false);

        user = MainActivity.users.get(MainActivity.currentUserId);

        moneyAmount = root.findViewById(R.id.moneyAmount);
        account = root.findViewById(R.id.account);
        category = root.findViewById(R.id.category);
        date = root.findViewById(R.id.date);
        desc = root.findViewById(R.id.desc);
        addNew = root.findViewById(R.id.addNew);

        ArrayAdapter<String> adapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_dropdown_item, getAccounts());
        account.setAdapter(adapter);
        account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter1;
        if (HomeFragment.inOrExp == 0)
            adapter1 = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        else
            adapter1 = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_dropdown_item, incoms);

        category.setAdapter(adapter1);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Navigation.findNavController(view).navigateUp();
            }
        });

        return root;
    }

    public String[] getAccounts () {
        String[] data = new String[user.getMoneyStore().size()];

        for(int i = 0; i<data.length; i++) {
            data[i] = user.getMoneyStore().get(i).getM_name();
        }

        return data;
    }

    /**
     * Megjeleníti a dátum kiválasztását lehetővé tevő felugró ablakot.
     */
    public void showDatePickerDialog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(root.getContext());

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(root.getContext()).inflate(R.layout.date_picker, null);
        final DatePicker picker = tesztLayout.findViewById(R.id.datePicker);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                date.setText(picker.getYear()+"/"+(picker.getMonth()+1)+"/"+picker.getDayOfMonth());
            }
        });

        alert.setView(tesztLayout);
        alert.show();
    }

    /**
     * Elmenti az adatokat legyen az bevétel vagy kiadás
     */
    private void saveData () {
        if(HomeFragment.inOrExp == 0) {
            user.addExpenditure(
                    categories[(int)category.getSelectedItemId()],
                    desc.getText().toString(),
                    getAccounts()[(int)account.getSelectedItemId()],
                    "Ft",
                    Float.parseFloat(moneyAmount.getText().toString()),
                    date.getText().toString());

            user.getMoneyStore().get((int)account.getSelectedItemId()).increaseValue(-Float.parseFloat(moneyAmount.getText().toString()));

            MainActivity.autoSave();
        } else {
            user.addIncome(
                    incoms[(int)category.getSelectedItemId()],
                    desc.getText().toString(),
                    "Ft",
                    Float.parseFloat(moneyAmount.getText().toString()),
                    date.getText().toString(),
                    getAccounts()[(int)account.getSelectedItemId()]);

            user.getMoneyStore().get((int)category.getSelectedItemId()).increaseValue(Float.parseFloat(moneyAmount.getText().toString()));

            MainActivity.autoSave();
        }
    }
}