package com.verwaltungssoftware.objects;

public class User {

    //Banner Unterseite
    private String bankName, kontoNr, blz, steuerNummer, ustId, company, street, town, country, companyNo;
    //Adressbanner
    private String preName, lastName, aStreet, aPlz, aLand, aOrt, aTel, aFax, aBankName, aBic, aIban, aAmt, aHrb;

    public User(String bankName, String kontoNr, String blz, String steuerNummer, String ustId,
            String company, String street, String town, String country, String companyNo, 
            String preName, String lastName, String aStreet, String aPlz, String aLand, 
            String aOrt, String aTel, String aFax, String aBankName, String aBic, 
            String aIban, String aAmt, String aHrb) {
        this.bankName = bankName;
        this.kontoNr = kontoNr;
        this.blz = blz;
        this.steuerNummer = steuerNummer;
        this.ustId = ustId;
        this.company = company;
        this.street = street;
        this.town = town;
        this.country = country;
        this.companyNo = companyNo;
        this.preName = preName;
        this.lastName = lastName;
        this.aStreet = aStreet;
        this.aPlz = aPlz;
        this.aLand = aLand;
        this.aOrt = aOrt;
        this.aTel = aTel;
        this.aFax = aFax;
        this.aBankName = aBankName;
        this.aBic = aBic;
        this.aIban = aIban;
        this.aAmt = aAmt;
        this.aHrb = aHrb;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getKontoNr() {
        return kontoNr;
    }

    public void setKontoNr(String kontoNr) {
        this.kontoNr = kontoNr;
    }

    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public String getSteuerNummer() {
        return steuerNummer;
    }

    public void setSteuerNummer(String steuerNummer) {
        this.steuerNummer = steuerNummer;
    }

    public String getUstId() {
        return ustId;
    }

    public void setUstId(String ustId) {
        this.ustId = ustId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getaStreet() {
        return aStreet;
    }

    public void setaStreet(String aStreet) {
        this.aStreet = aStreet;
    }

    public String getaPlz() {
        return aPlz;
    }

    public void setaPlz(String aPlz) {
        this.aPlz = aPlz;
    }

    public String getaLand() {
        return aLand;
    }

    public void setaLand(String aLand) {
        this.aLand = aLand;
    }

    public String getaOrt() {
        return aOrt;
    }

    public void setaOrt(String aOrt) {
        this.aOrt = aOrt;
    }

    public String getaTel() {
        return aTel;
    }

    public void setaTel(String aTel) {
        this.aTel = aTel;
    }

    public String getaFax() {
        return aFax;
    }

    public void setaFax(String aFax) {
        this.aFax = aFax;
    }

    public String getaBankName() {
        return aBankName;
    }

    public void setaBankName(String aBankName) {
        this.aBankName = aBankName;
    }

    public String getaBic() {
        return aBic;
    }

    public void setaBic(String aBic) {
        this.aBic = aBic;
    }

    public String getaIban() {
        return aIban;
    }

    public void setaIban(String aIban) {
        this.aIban = aIban;
    }

    public String getaAmt() {
        return aAmt;
    }

    public void setaAmt(String aAmt) {
        this.aAmt = aAmt;
    }

    public String getaHrb() {
        return aHrb;
    }

    public void setaHrb(String aHrb) {
        this.aHrb = aHrb;
    }

}
