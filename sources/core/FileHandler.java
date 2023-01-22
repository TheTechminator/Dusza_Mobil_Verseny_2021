package com.terminato.moneymanager.core;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A fájlkezelés lehetővé tevő osztáály.
 */
public class FileHandler {

    private Context context;

    /**
     * Szüksége van a kontextusra.
     * @param context
     */
    public FileHandler(Context context) {
        this.context = context;
    }

    /**
     * Egy megadott fáljnévvel kiírja a megadott adatokat.
     * @param filePath - fájl neve/utvónala
     * @param data - kiírabdó adatok
     */
    public void writeToFile (String filePath, String data) {
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE), "utf-8");
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Egy megadott elérési útvonalra kiírja a megadott adatokat.
     * @param uri - fájl elérési útvonala
     * @param data - kiírabdó adatok
     */
    public void writeToExternalFile(Uri uri, String data){
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Objects.requireNonNull(outputStream)));
            writer.write(data);
            writer.close();
            Toast.makeText(context,"Az exportálás sikeres", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Az exportálás sikertelen", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Beolvassa az adatokat a megadott fájlból.
     * @param filePath - a beolvasni kívánt fájl
     * @return - beolvasot adatok egy szövegként tárolva
     */
    public String readFromFile ( String filePath ) {
        String data = "";

        try {
            InputStream inputStream = context.openFileInput(filePath);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    data += receiveString;
                }

                inputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  data;
    }

    /**
     * A megadott fájl törlése.
     * @param filePath - a törlendő fájl
     */
    public void deleteFile (String filePath) {
        context.deleteFile(filePath);
    }

    /**
     * Megnézi hogy a megadott fájl egyáltalán létezik-e
     * @param filePath - megadott fájl
     * @return - igaz, hamis (létezik vagy sem)
     */
    public boolean isFileExisting (String filePath) {
        boolean van = false;

        String[] files = context.fileList();

        for (int i = 0; i<files.length; i++){
            if (files[i].equals(filePath)) {
                van = true;
            }
        }

        return van;
    }

    public void debugExistingFiles () {
        String[] files = context.fileList();

        for (int i = 0; i<files.length; i++){
            Log.d("TESZT", files[i]);
        }
    }

}
