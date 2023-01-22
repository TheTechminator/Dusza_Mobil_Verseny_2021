package com.terminato.moneymanager.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.terminato.moneymanager.MainActivity;
import com.terminato.moneymanager.R;
import com.terminato.moneymanager.core.ExcelWriter;
import com.terminato.moneymanager.core.FileHandler;
import com.terminato.moneymanager.core.MoneyEvent;
import com.terminato.moneymanager.core.PDFWriter;
import com.terminato.moneymanager.core.User;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A főoldalt leíró osztály
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root; //gyökér

    private Button btKiadasok; //A kiadásokat megjelenítő gomb
    private Button btBevetelek;//A bevételeket megjelenítő gomb
    private Button newItem; //Új elem hozzá adását lehetővé tevő gomb
    private Button export; //A kiexportálást lehetővé tevő gomb
    private LinearLayout linearLayoutData;

    public static int inOrExp = 0; //0 = expenditure, 1 = income
    public static int choosenId = 0; //A kiválasztott elem (akkor van jelentősége ha a felhasználó egy adott tételre kattint)

    private final int PDFREQUEST = 9999, EXCELREQUEST = 9998;

    private User user; //Egy kiválasztott felhasználó

    private AlertDialog dialog; //Egy felugró ablak

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        user = MainActivity.users.get( MainActivity.currentUserId );

        linearLayoutData = root.findViewById( R.id.linearLayoutData );
        btKiadasok = root.findViewById( R.id.btKiadasok );
        btBevetelek = root.findViewById( R.id.btBevetelek );
        newItem = root.findViewById(R.id.newItem);
        export = root.findViewById(R.id.export);

        btKiadasok.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btKiadasok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadKiadasok();
                inOrExp = 0;

                btKiadasok.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                btBevetelek.setPaintFlags(0);
            }
        });

        if (inOrExp == 0)
            loadKiadasok();
        else
            loadBevetelek();

        btBevetelek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBevetelek();
                inOrExp = 1;

                btBevetelek.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                btKiadasok.setPaintFlags(0);
            }
        });

        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_fragmentAddIncomeOrExpenditure);
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExportChooserDialog();
            }
        });

        return root;
    }

    /**
     * Betölti és felteszi a layout-ra a felhasználó kiadásait
     */
    private void loadKiadasok () {
        linearLayoutData.removeAllViews();
        ArrayList<MoneyEvent> expenditures = user.getExpenditures();

        for(int i = 0; i<expenditures.size(); i++) {
            linearLayoutData.addView( createLayout( "kep", expenditures.get( i ).getType(), expenditures.get( i ).getValue(), i, R.drawable.kiadas ) );
        }
    }

    /**
     * Létrehoz egy layout-ot ami egy adott tételt jelenít majd meg.
     * @param img - egy kép (még nincs használva)
     * @param type - az adott tétel típusa (kategóriája)
     * @param value - az adott tételhez kapcsolódó pénzmozgás összege
     * @param id - egy azonosító ami majd segít késöbb felismerni az adott elemet
     * @param imgResource - egy statikus icon
     * @return - egy létrehozott layout amit csak fel kell kenni az ablakra
     */
    private LinearLayout createLayout (String img, String type, float value, int id, int imgResource) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from( root.getContext() ).inflate( R.layout.unit_layout, null );
        layout.setId(id);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosenId = view.getId();
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_fragmentDetails);
            }
        });

        ImageView icon = layout.findViewById( R.id.icon );
        TextView title = layout.findViewById( R.id.title );
        TextView amount = layout.findViewById( R.id.amount );

        title.setText( type );
        amount.setText( value + " Ft" );

        icon.setImageResource(imgResource);

        return layout;
    }

    /**
     * Betölti és felteszi a layout-ra a felhasználó bevételeit
     */
    private void loadBevetelek () {
        linearLayoutData.removeAllViews();
        ArrayList<MoneyEvent> incomes = user.getIncomes();

        for(int i = 0; i<incomes.size(); i++) {
            linearLayoutData.addView( createLayout( "kep", incomes.get( i ).getType(), incomes.get( i ).getValue(), i, R.drawable.bevetel ) );
        }
    }

    /**
     * Megjeleníti az exportálás választó ablakot
     */
    public void showExportChooserDialog () {
        AlertDialog.Builder exportChooserDialog = new AlertDialog.Builder(root.getContext());

        ConstraintLayout exportChooserLayout = (ConstraintLayout) LayoutInflater.from(root.getContext()).inflate(R.layout.export_chooser_layout, null);
        Button pdfFormat = exportChooserLayout.findViewById(R.id.pdfFormat);
        Button excelFormat = exportChooserLayout.findViewById(R.id.excelFormat);

        pdfFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToFileDialog(PDFREQUEST);
            }
        });

        excelFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToFileDialog(EXCELREQUEST);
            }
        });

        exportChooserDialog.setNegativeButton("Mégsem", null);

        exportChooserDialog.setView(exportChooserLayout);
        dialog = exportChooserDialog.show();
    }

    /**
     * Feldobja a felhasználónak, hogy biztosan exportálni akar-e és ha igen megnyitja a tallózót
     * @param requestCode - pdf vagy excelt akar menteni
     */
    private void exportToFileDialog (int requestCode) {
        AlertDialog dialog = new AlertDialog.Builder(root.getContext())
                .setTitle("Exportálás")
                .setMessage("Válassza ki a mappát ahová az adatait szeretné exportálni!")
                .setNegativeButton("Mégsem", null)
                .setPositiveButton("Oké",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivityForResult(Intent.createChooser(i, "Mappa kiválasztása"), requestCode);
                        }
                    }
                })
                .show();
    }

    /**
     * Amikor a felhasználó visszatér a mappa válogatásból kimentjük neki az adatokat ha úgy akarta.
     * Abban a formátumban mentünk neki ahogy akarta (pdf, excel)
     * @param requestCode - pdf vagy excelt akar menteni
     * @param resultCode - a felhasználó döntésén alapuló kód (választott-e mappát vagy valami)
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        String formattedDate = df.format(c);

        System.out.println(formattedDate);

        if(requestCode == PDFREQUEST && resultCode != 0) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(root.getContext(), data.getData());
            DocumentFile documentFile1 = documentFile.createFile("application/pdf", user.getUserDisplayName()+"_"+formattedDate+"_kimutatas");

            try {
                new PDFWriter(user, root.getContext().getContentResolver().openOutputStream(documentFile1.getUri()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.hide();
        } else if (requestCode == EXCELREQUEST && resultCode != 0) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(root.getContext(), data.getData());
            DocumentFile documentFile1 = documentFile.createFile("application/vnd.ms-excel", user.getUserDisplayName()+"_"+formattedDate+"_kimutatas");

            try {
                new ExcelWriter(user, root.getContext().getContentResolver().openOutputStream(documentFile1.getUri()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.hide();
        }
    }
}