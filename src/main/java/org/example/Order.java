package org.example;

/**
 * Class representing an order.
 */
public class Order {
    String id_zamowienia;
    String id_klienta;
    String kwota;
    String status_zamowienia;

    public Order(String id_zamowienia, String id_klienta, String kwota, String status) {
        this.id_zamowienia = id_zamowienia;
        this.id_klienta = id_klienta;
        this.kwota = kwota;
        this.status_zamowienia = status;
    }

    public String getId_zamowienia() {
        return id_zamowienia;
    }

    public void setId_zamowienia(String id_zamowienia) {
        this.id_zamowienia = id_zamowienia;
    }

    public String getId_klienta() {
        return id_klienta;
    }

    public void setId_klienta(String id_klienta) {
        this.id_klienta = id_klienta;
    }

    public String getKwota() {
        return kwota;
    }

    public void setKwota(String kwota) {
        this.kwota = kwota;
    }

    public String getStatus() {
        return status_zamowienia;
    }

    public void setStatus(String status) {
        this.status_zamowienia = status;
    }
}
