package com.example.backend.models.data_enums;

public enum RDA {
//    RECOMMENDED_DAILY_ALLOWANCE(2600, 390
//            , 130, 86, 78
//            , 1.5, 0.3
//            , 20, 3);

    A_MEAL_RECOMMENDED_DAILY_ALLOWANCE(919.916666667, 106.665833333
            , 45.2033333333, 28.4203333333, 26
            , 0.5, 0.1
            , 6.6666667, 1);

    private double kcal;
    private double carbohydrate, protein, fat;
    private double sugar, sodium;
    private double cholesterol, saturatedFattyAcid, transFat;

    RDA(double kcal, double carbohydrate, double protein, double fat, double sugar, double sodium, double cholesterol, double saturatedFattyAcid, double transFat) {
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
        this.saturatedFattyAcid = saturatedFattyAcid;
        this.transFat = transFat;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getSaturatedFattyAcid() {
        return saturatedFattyAcid;
    }

    public void setSaturatedFattyAcid(double saturatedFattyAcid) {
        this.saturatedFattyAcid = saturatedFattyAcid;
    }

    public double getTransFat() {
        return transFat;
    }

    public void setTransFat(double transFat) {
        this.transFat = transFat;
    }
}
