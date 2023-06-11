package org.example;

/**
 * Class representing a user.
 */
public class User {
    String id_osoby;
    String imie;
    String nazwisko;
    String email;
    String nr_telefonu;
    String haslo;
    String id_adresu;
    String rola;

    public User(String id_osoby, String imie, String nazwisko, String email, String nr_telefonu, String haslo, String id_adresu, String rola) {
        this.id_osoby = id_osoby;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.nr_telefonu = nr_telefonu;
        this.haslo = haslo;
        this.id_adresu = id_adresu;
        this.rola = rola;
    }

    public String getId_osoby() {
        return id_osoby;
    }

    public void setId_osoby(String id_osoby) {
        this.id_osoby = id_osoby;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNr_telefonu() {
        return nr_telefonu;
    }

    public void setNr_telefonu(String nr_telefonu) {
        this.nr_telefonu = nr_telefonu;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getId_adresu() {
        return id_adresu;
    }

    public void setId_adresu(String id_adresu) {
        this.id_adresu = id_adresu;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }
}
