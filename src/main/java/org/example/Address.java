package org.example;

/**
 * Class representing an address.
 */
public class Address {
    String id_adresu;
    String ulica;
    String kod_pocztowy;
    String miasto;

    public Address(String id_adresu, String ulica, String kod_pocztowy, String miasto) {
        this.id_adresu = id_adresu;
        this.ulica = ulica;
        this.kod_pocztowy = kod_pocztowy;
        this.miasto = miasto;
    }

    public String getId_adresu() {
        return id_adresu;
    }

    public void setId_adresu(String id_adresu) {
        this.id_adresu = id_adresu;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getKod_pocztowy() {
        return kod_pocztowy;
    }

    public void setKod_pocztowy(String kod_pocztowy) {
        this.kod_pocztowy = kod_pocztowy;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }
}
