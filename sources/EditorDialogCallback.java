package com.terminato.moneymanager;

/**
 * Amikor egy szerkeztő dialógus megnyílik a felhasználó tud bevinni adatokat.
 * Arra szolgál, hogy a felhasználó modosításaira fel tudjunk iratkozni és optimálisan reagálni.
 */
public interface EditorDialogCallback {

    /**
     * Megkapja paraméterben a dialógus ablakban található szerkeztődoboz tartalmát ha a felhasználó menti.
     * @param data - egyszerű szöveg
     */
    public void callBack ( String data );
}
