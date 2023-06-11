package org.example;

/**
 * Class representing a product in shopping cart.
 */
public class ShoppingList {
    String id_produktu_w_koszyku;
    String id_osoby;
    String id_produktu;
    String ilosc;
    String nazwa;
    String cena;
    String total;

    public ShoppingList(String id_produktu_w_koszyku, String id_osoby, String id_produktu, String ilosc, String nazwa, String cena, String total) {
        this.id_produktu_w_koszyku = id_produktu_w_koszyku;
        this.id_osoby = id_osoby;
        this.id_produktu = id_produktu;
        this.ilosc = ilosc;
        this.nazwa = nazwa;
        this.cena = cena;
        this.total = total;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId_produktu_w_koszyku() {
        return id_produktu_w_koszyku;
    }

    public void setId_produktu_w_koszyku(String id_produktu_w_koszyku) {
        this.id_produktu_w_koszyku = id_produktu_w_koszyku;
    }

    public String getId_osoby() {
        return id_osoby;
    }

    public void setId_osoby(String id_osoby) {
        this.id_osoby = id_osoby;
    }

    public String getId_produktu() {
        return id_produktu;
    }

    public void setId_produktu(String id_produktu) {
        this.id_produktu = id_produktu;
    }

    public String getIlosc() {
        return ilosc;
    }

    public void setIlosc(String ilosc) {
        this.ilosc = ilosc;
    }
}
