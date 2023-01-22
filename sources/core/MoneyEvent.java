package com.terminato.moneymanager.core;

/**
 * Egy adott pénzmozgást leíró adatszerkezet.
 */
public class MoneyEvent {
    private String m_type; // peldaul havi, alkalmi, ajandek
    private String m_desc; // leiras, peldaul kapalas
    private String m_date; // esemeny datuma
    private String currency; // osszeg es tipusa
    private String paymentMethod; //fizetési mód
    private float value; // bevont penz tarolo egyseg

    public MoneyEvent(String type, String desc, String date, String currency, String paymentMethod, float value) {
        m_type= type;
        m_desc= desc;
        m_date= date;
        this.currency = currency;
        this.value = value;
        this.paymentMethod = paymentMethod;
    }

    public String getType() { return m_type; }
    public String getDesc() { return m_desc; }
    public String getDate() { return m_date; }
    public String getCurrency() { return currency; }
    public float getValue() { return value; }
    public String getPaymentMethod() { return paymentMethod; }

    public void setType(String x) { m_type= x; }
    public void setDesc(String x) { m_desc= x; }
    public void setDate(String x) { m_date= x; }
    public void setCurrency(String x) { currency= x; }
    public void SetValue(float x) { value= x; }
    public void setPaymentMethod(String x) { paymentMethod = x; }
}