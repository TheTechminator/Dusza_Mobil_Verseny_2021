package com.terminato.moneymanager.core;

import android.content.Context;
import java.util.ArrayList;
import org.json.*;

public class ReadUsersData {
    private ArrayList<User> users = new ArrayList<User>(); // felhasználók listája
    private ArrayList<MoneyEvent> userIncome = new ArrayList<MoneyEvent>(); // egy felhasználó bevételei
    private ArrayList<MoneyEvent> userExpenditure = new ArrayList<MoneyEvent>(); // egy felhasználó kiadásai
    private ArrayList<MoneyStore> moneyStore = new ArrayList<MoneyStore>(); // egy felhasználó számlái
    private String userName = "",
            userDisplayName = "",
            userProfilePicPath = "",
            incomeType = "",
            incomeDescription = "",
            expenditureType = "",
            expenditureDescription = "",           // segéd változók
            currency = "",
            date = "",
            paymentMethod = "",
            email = "",
            jsonText = "";



    private float value = 0;
    private JSONArray jsonArray = null;

    private FileHandler fileHandler;

    public ReadUsersData( Context context, String filePath ) throws Exception {
        fileHandler = new FileHandler( context );

        System.out.println( fileHandler.readFromFile( filePath ) );

        jsonArray = new JSONArray( fileHandler.readFromFile( filePath ) );

        //JSON tömb léterhozása mit majd be járunk

		/*System.out.println(jsonArray.get(0));
		System.out.println(jsonArray.get(1));*/
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            userName = jsonObject.getString("userName");
            userDisplayName = jsonObject.getString("userDisplayName");
            userProfilePicPath = jsonObject.getString("userProfilePicPath");
            email = jsonObject.getString("e-mail");

            /*
                alap adatok elkérése
            */

            JSONArray jsonList = jsonObject.getJSONArray("incomeList");

            //System.out.println(jsonList.get(1));
            for(int j = 0; j < jsonList.length(); j++) {
                JSONObject incomeObj = jsonList.getJSONObject(j);
                incomeType = incomeObj.getString("incomeType");
                incomeDescription = incomeObj.getString("incomeDescription");
                currency = incomeObj.getString("currency");
                date = incomeObj.getString("date");
                value = (float) incomeObj.getDouble("value");
                paymentMethod = incomeObj.getString("paymentMethod");
                userIncome.add(new MoneyEvent(incomeType, incomeDescription, date, currency, paymentMethod, value));
            }

            /*
                felhasználó bevételeinek kiolvasása
                ezek egy JSON tömbben  vannak eltárolva,
                és azon belül az összetartozó dolgok egy JSON objektumot alkotnak
            */

            jsonList = jsonObject.getJSONArray("expenditureList");
            for(int k = 0; k < jsonList.length(); k++) {
                JSONObject expenditureObj = jsonList.getJSONObject(k);
                expenditureType = expenditureObj.getString("expenditureType");
                expenditureDescription = expenditureObj.getString("expenditureDescription");
                currency = expenditureObj.getString("currency");
                paymentMethod = expenditureObj.getString("paymentMethod");
                date = expenditureObj.getString("date");
                value = (float) expenditureObj.getDouble("value");
                userExpenditure.add(new MoneyEvent(expenditureType, expenditureDescription, date, currency, paymentMethod, value));
            }

            /*
                felhasználó kiadásainak kiolvasása
                ezek egy JSON tömbben  vannak eltárolva,
                és azon belül az összetartozó dolgok egy JSON objektumot alkotnak
            */

            jsonList = jsonObject.getJSONArray("moneyStore");
            for(int k = 0; k < jsonList.length(); k++) {
                JSONObject h = jsonList.getJSONObject(k);
                String type = h.getString("name"),
                        alias = h.getString("alias");
                currency = h.getString("currency");
                float value = (float) h.getDouble("value");
                moneyStore.add(new MoneyStore(type, alias, value, currency));
            }

            /*
                felhasználó számláinak kiolvasása
                ezek egy JSON tömbben  vannak eltárolva,
                és azon belül az összetartozó dolgok egy JSON objektumot alkotnak
            */

            users.add(new User(userDisplayName, userProfilePicPath, email, userIncome, userExpenditure, moneyStore));

            /*
                új felhasználó léterhozása
                minden adatával együtt
            */

            userIncome = new ArrayList<MoneyEvent>();
            userExpenditure = new ArrayList<MoneyEvent>();
            moneyStore = new ArrayList<MoneyStore>();

            /*
                felkészülés a következő felhasználó feldolgozására
            */

            //System.out.println(userName + userDisplayName + userProfilePicPath);

        }
    }

    /**
     * visszaadja a felhasználókat
     */

    public ArrayList<User> getUsers() { return users; }

    /**
     * visszadja a felhasználókat egy JSON tömbként
     */
    public JSONArray getJSONArray () { return jsonArray; }
}