package com.terminato.moneymanager.core;

import java.util.ArrayList;
import java.text.*;
import java.util.HashMap;

/**
 * Egy felhasználót leíró adatszerkezet.
 */
public class User {
    private String _userName; //like Nagy_David
    private String _userDisplayName; //like Nagy Dávid
    private String _userProfilePicPath; //felhasználó profilképének az elérési útvonala
    private String _email; //a felhasználó email címe
    private ArrayList<MoneyEvent> _incomeList = new ArrayList<MoneyEvent>(); //a bevételeket tartalmazó lista
    private ArrayList<MoneyEvent> _expenditureList = new ArrayList<MoneyEvent>(); //a kiadásokat tartalmazó lista
    private ArrayList<MoneyStore> _moneyStore = new ArrayList<MoneyStore>(); //a számlákat tartalmazó lista

    public User(String userDisplayName, String userProfilePicPath, String email, ArrayList<MoneyEvent> incomeList, ArrayList<MoneyEvent> expenditureList, ArrayList<MoneyStore> moneyStore) {
        _userDisplayName = userDisplayName;
        _userName = makeUserName(userDisplayName);
        _userProfilePicPath = userProfilePicPath;
        _email = email;
        _incomeList = incomeList;
        _expenditureList = expenditureList;
        _moneyStore = moneyStore;
    }


    ////////////////////////////////set////////////////////////////////////
    public void addIncome(String incomeType, String incomeDescription, String currency, float value, String date, String paymentMethod) {
        _incomeList.add(new MoneyEvent(incomeType, incomeDescription, date, currency, paymentMethod, value));
    }

    public void addExpenditure(String expenditureType, String expenditureDescription, String paymentMethod, String currency, float value, String date) {
        _expenditureList.add(new MoneyEvent(expenditureType, expenditureDescription, date, currency, paymentMethod, value));
    }

    public void addMoneyStore(String name, String alias, float value, String currency) {
        _moneyStore.add(new MoneyStore(name, alias, value, currency));
    }

    public void setUserName( String userName ) { _userName = userName; }

    public void setDisplayName ( String userDisplayName ) {
        _userDisplayName = userDisplayName;
        _userName = makeUserName(userDisplayName);
    }

    public void setEmail ( String email ) {
        this._email = email;
    }

    public void setUserProfilePicPath(String userProfilePicPath) { _userProfilePicPath = userProfilePicPath; }

    ////////////////////////////////////get/////////////////////////////////////
    public ArrayList<MoneyEvent> getIncomes() { return _incomeList; }

    public ArrayList<MoneyEvent> getExpenditures() { return _expenditureList; }

    public ArrayList<MoneyStore> getMoneyStore() { return _moneyStore; }

    public String getUserName() { return _userName; }

    public String getUserDisplayName() { return _userDisplayName; }

    public String getUserProfilePicPath() { return _userProfilePicPath; }

    public String getUserEmail() { return _email; }

    ////////////////////////////////törlések/////////////////////////////////
    public void deleteIncome(int incomeIndex) {
        _incomeList.remove(incomeIndex);
    }

    public void deleteExpenditure(int expenditureIndex) {
        _expenditureList.remove(expenditureIndex);
    }


    //////////////////////////////egyéb/////////////////////////////////////////////////
    private String makeUserName(String userName) {
        String x = userName.replaceAll(" ", "_");
        return Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * A felhasználó számlájit átnézi és összegzi mennyi pénz van rajtuk.
     * @return a számlákon levő összes pénz
     */
    public float getTotalMoney () {
        float sum = 0;
        for(int i = 0; i<_moneyStore.size(); i++) {
            sum += _moneyStore.get(i).getValue();
        }
        return sum;
    }

    /**
     * A felhasználó számlájit átnézi és összegzi mennyi pénz van rajtuk.
     * @return a számlákon levő összes pénz
     */
    public float getSumOfCash() {
        float sum = 0f;
        for(int i = 0; i < _moneyStore.size(); i++) {
            sum += _moneyStore.get(i).getValue();
        }
        return sum;
    }

    /**
     * Kategóriánként összegzi a felhasználó bevételeit késsőbbi adatfeldolgozáshoz.
     * @return - kategóriánként összegzett bevételek
     */
    public HashMap<String, Float> getFilteredIncomePrice() {
        HashMap<String, Float> h = new HashMap<String, Float>();

        for(int i = 0; i < _incomeList.size(); i++) {
            h.put(_incomeList.get(i).getType(), 0f);
        }

        for(int i = 0; i < _incomeList.size(); i++) {
            h.put(_incomeList.get(i).getType(), h.get(_incomeList.get(i).getType()) + _incomeList.get(i).getValue());
        }
        return h;
    }

    /**
     * Kategóriánként összegzi a felhasználó kiadásait késsőbbi adatfeldolgozáshoz.
     * @return - kategóriánként összegzett kiadások
     */
    public HashMap<String, Float> getFilteredExpenditurePrice() {
        HashMap<String, Float> h = new HashMap<String, Float>();

        for(int i = 0; i < _expenditureList.size(); i++) {
            h.put(_expenditureList.get(i).getType(), 0f);
        }

        for(int i = 0; i < _expenditureList.size(); i++) {
            h.put(_expenditureList.get(i).getType(), h.get(_expenditureList.get(i).getType()) + _expenditureList.get(i).getValue());
        }
        return h;
    }

}