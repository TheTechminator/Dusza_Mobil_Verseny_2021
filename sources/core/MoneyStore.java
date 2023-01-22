package com.terminato.moneymanager.core;

/**
 * Egy adott számlát leíró adatszerkezet.
 */
public class MoneyStore {
    private String m_name; //penz tarolo egyseg neve, peldaul keszpenz
    private String alias; //penz tarolo egyseg neve, peldaul keszpenz
    private float value; //a számlán levő pénz összege
    private String currency; //valuta

    /**
     * Egy számla létrehozása.
     * @param name - számla neve
     * @param alias - számla neve
     * @param value - számlán levő pénz mennyisége
     * @param currency - valuta
     */
    public MoneyStore(String name, String alias, float value, String currency) {
        this.m_name = name;
        this.alias = alias;
        this.value = value;
        this.currency = currency;
    }

    public String getM_name() {
        return m_name;
    }

    public String getAlias() {
        return alias;
    }

    public float getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void increaseValue(float value) {
        this.value += value;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}