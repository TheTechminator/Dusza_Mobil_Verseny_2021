package com.terminato.moneymanager;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Egy kis felugró ablakot hoz létre ahol a felhasználó tud bevinni adatokat.
 */
public class EditorDialog {
    private AlertDialog editorDialog;

    private ConstraintLayout layout;
    private TextView label;
    private EditText text;
    private Button save;
    private Button cancel;

    private Context context;

    public EditorDialog ( Context context ) {
        this.context = context;
    }

    /**
     * Megjeleníti a felugró ablakot.
     * @param currentText a jelenleg módosítandó szöveg
     * @param labelText a címsorban megjelenő szöveg
     * @param callback a visszahívó ami majd vissza lesz hívva
     */
    public void show( String currentText, String labelText, EditorDialogCallback callback ) {
        init( context, labelText, currentText );
        initSaveButton( callback );
        initCancelButton();
        showDialog();
    }

    /**
     * Inicializálja a layoutot és beállítja a szövegeket.
     * @param context - kontextus
     * @param labelText a címsorban megjelenő szöveg
     * @param currentText a jelenleg módosítandó szöveg
     */
    private void init ( Context context, String labelText, String currentText ) {
        layout = ( ConstraintLayout ) LayoutInflater.from( context ).inflate( R.layout.editor_layout, null );

        label = layout.findViewById( R.id.label );
        text = layout.findViewById( R.id.text );
        save = layout.findViewById( R.id.save );
        cancel = layout.findViewById( R.id.cancel );

        label.setText( labelText + " módosítása!" );
        text.setText( currentText );
    }

    /**
     * Felkészül a méntés gombra történő kattintásra.
     * @param callback a visszahívó amit majd visszahívunk
     */
    private void initSaveButton ( EditorDialogCallback callback ) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = text.getText().toString();

                if( !data.equals( "" ) ) {
                    callback.callBack( data );
                }

                editorDialog.hide();
            }
        });
    }

    /**
     * Inicializálja a cancel gombot.
     */
    private void initCancelButton () {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorDialog.hide();
            }
        });
    }

    /**
     * Megjeleníti a feluró ablakot.
     */
    private void showDialog () {
        editorDialog = new AlertDialog.Builder( context )
                .setView( layout )
                .show();
    }
}
