package com.terminato.moneymanager.core;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateJSONFormat {

    private ArrayList<User> users;
    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonObject = new JSONObject();

    /**
     *Létrehozza a JSON formátumot a User-ből.
     @param _users : az összes User-t elkéri, és ebből lesz előállítva a JSON formátum
     */
    public CreateJSONFormat(ArrayList<User> _users) throws Exception {
        users = _users;

        for(int i = 0; i < users.size(); i++) {
            ///////////////// users bejárása ////////////////////////////
            String Name = users.get(i).getUserName(),
                    DisplayName = users.get(i).getUserDisplayName(),
                    email = users.get(i).getUserEmail(),
                    ProfilePicPath = users.get(i).getUserProfilePicPath();

            jsonObject.put("userName", Name);
            jsonObject.put("userDisplayName", DisplayName);
            jsonObject.put("userProfilePicPath", ProfilePicPath);
            jsonObject.put("e-mail", email);

            /*
                első négy sor elkéri az alap dolgokat,
                majd egy JSON objektumba teszi őket
            */

            //////////////////// users bevételeinek bejárása ////////////////////////////
            ArrayList<MoneyEvent> income = users.get(i).getIncomes();
            JSONArray arr = new JSONArray();
            for(int j = 0; j < income.size(); j++) {
                String Type = income.get(j).getType(),
                        Description = income.get(j).getDesc(),
                        currency = income.get(j).getCurrency(),
                        paymentMethod = income.get(j).getPaymentMethod(),
                        date = income.get(j).getDate();
                float value = income.get(j).getValue();
                JSONObject obj = new JSONObject();
                obj.put("incomeType", Type);
                obj.put("incomeDescription", Description);
                obj.put("paymentMethod", paymentMethod);
                obj.put("currency", currency);
                obj.put("value", value);
                obj.put("date", date);
                arr.put(obj);
            }

            jsonObject.put("incomeList", arr);

            /*
                a bevételek egy JSON tömben vannak eltárolva,
                de maga egy bevétel egy JSON objektumnak felel meg
                ezért legelsőnek tömböt be kell járni,
                majd az adatagokból összeállítani a JSON objektumot
                és a légvégén a JSON tömbhöz hozzáadni
            */

            //////////////////// users kiadásainak bejárása ////////////////////////////
            ArrayList<MoneyEvent> expenditure = users.get(i).getExpenditures();
            arr = new JSONArray();
            for(int j = 0; j < expenditure.size(); j++) {
                String Type = expenditure.get(j).getType(),
                        Description = expenditure.get(j).getDesc(),
                        currency = expenditure.get(j).getCurrency(),
                        date = expenditure.get(j).getDate(),
                        paymentMethod = expenditure.get(j).getPaymentMethod();
                float value = expenditure.get(j).getValue();
                JSONObject obj = new JSONObject();
                obj.put("expenditureType", Type);
                obj.put("expenditureDescription", Description);
                obj.put("paymentMethod", paymentMethod);
                obj.put("currency", currency);
                obj.put("value", value);
                obj.put("date", date);
                arr.put(obj);
            }
            jsonObject.put("expenditureList", arr);

            /*
                hasonló mint a bevétel csak itt a kiadásokkal csináljuk meg
            */

            //////////////////// users számláinak bejárása ////////////////////////////
            ArrayList<MoneyStore> store = users.get(i).getMoneyStore();
            arr = new JSONArray();

            for(int j = 0; j < store.size(); j++) {
                String name = store.get(j).getM_name(),
                        alias = store.get(j).getAlias(),
                        currency = store.get(j).getCurrency();
                float value = store.get(j).getValue();

                JSONObject obj = new JSONObject();
                obj.put("name", name);
                obj.put("alias", alias);
                obj.put("value", value);
                obj.put("currency", currency);
                arr.put(obj);
            }

            jsonObject.put("moneyStore", arr);

            /*
                itt is hasonló a működése
            */

            jsonArray.put(jsonObject);

            /*
             a legvégén összeállított JSON objektumot (ami egy felhasználó adatait tartalmazz),
              és egy JSON tömbe tesszük, és elkezdjük a következő felhasználót bejárni
            */
            jsonObject = new JSONObject();
        }
    }

    /**
     *Vissza adja a JSON tömböt amit a kiiratásnál lehet felhasználni.
     */
    public JSONArray getJSONArray() { return jsonArray; }

}
