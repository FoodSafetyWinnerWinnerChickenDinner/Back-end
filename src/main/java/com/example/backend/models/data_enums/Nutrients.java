package com.example.backend.models.data_enums;

public enum Nutrients {
    CHICKEN(154.2355191160072, 297.17152796650964
            , 6.04717021081533, 32.69640545254722, 17.83580210590874
            , 4.890957143930871, 780.0918948948405
            , 155.64933797240914, 7.494381354888844, 2.4266982420109824),
    PIG(107.9130736442456, 351.5177969434517
            , 6.146302072560938, 19.47847756227281, 30.937706610784634
            , 3.413031898103374, 383.90846399214
            , 56.98245261895729, 11.371367999518133, 1.4611665965469074),
    COW(83.25, 184.18776257307246
            , 8.520825897622093, 24.827127681771632, 20.787475781689913
            , 8.25, 59.32973406862113
            , 47.016752101181936, 12.176002835964388, 8.495472591841027),
    FISH(76.5, 143.5472856087947
            , 1.6051776695296636, 20.36388293927314, 8.446880050865893
            , 1.5, 127.9929387567577
            , 55.08434902349896, 3.160376227585084, 1.5051450317547304),
    RAW_FISH(94.5, 125.70256175760775
            , 20.769522599630008, 33.834026115422304, 24.546080900236895
            , 19.5, 202.1161129479679
            , 33.099669968139175, 20.345064418217866, 19.520105482696298),
    RAMEN(177.83804135931643, 347.71427012828326
            , 66.20750297884769, 23.011320718171913, 26.402680916909674
            , 19.440783868210048, 1148.8858143139175
            , 31.110690823938242, 20.37055753751119, 15.028328607001907),
    COFFEE(172.1137206645626, 44.96437035812312
            , 14.269639533787291, 4.926145471801578, 5.226424517211591
            , 16.771758257616227, 47.86034492038998
            , 4.625, 4.625, 4.625),
    DRINK(35.60310363079829, 65.932951990752
            , 16.281286187291027, 0.7567116952719121, 0.6749999999999999
            , 13.275, 1.801823762720666
            , 0.5,  0.5, 0.5),
    BREAD(110.26995683407152, 334.41669214501144
            , 69.93227964944657, 37.711038245507154, 39.220696836246695
            , 45.225252835488675, 335.675645094218
            , 56.78498943466577,  34.87513753512384, 32.27567324611235),
    PIZZA(92.08103447693415, 258.47832698054475
            , 36.23832118835462, 10.538534851994285, 9.56466288443671
            , 6.614607557484492, 421.47089612977203
            , 12.508027312115436, 5.462385788674933, 0.75),
    KIMCHI(23.0, 3.151085085137254
            , 0.9238813368509694, 0.6838192314226719, 0.5322821773229381
            , 0.6477617158903733, 161.88260736114626
            , 0.5, 0.5, 0.5),
    SOUP(419.43105685654626, 249.82799254533535
            , 37.20081188778288, 35.00256995151759, 31.0087255120371
            , 25.223645088516808, 1177.7360787043224
            , 125.2063010362723, 23.618148012796137, 17.787924210236667),
    RICE(154.73504233964073, 230.30076057520688
            , 51.138193909433, 4.42633545031537, 1.4686004233964074
            , 0.375, 10.840010584910182
            , 0.375, 0.5256677251576849, 0.375),
    RED_RICE_CAKE(229.21903446891116, 325.2369985523237
            , 63.17832807607805, 8.460586809119896, 5.983001531567687
            , 7.959113118861683, 851.184369471159
            , 24.59722222222222, 2.989813352090062, 1.125),
    SUNDAE(168.79139072545127, 298.1278196716666
            , 42.908271439823864, 8.108485159724045, 11.272748035818683
            , 2.32071555075835, 762.8622022706611
            , 22.885278257451645, 3.240820417609902, 0.4111602540378444),
    FRIED(130.1348315813801, 391.72135362169854
            , 34.56782435959144, 21.300617472966337, 26.252391499944682
            , 5.364275554157115, 576.4839133886344
            , 83.60806173731426, 8.382272878340146, 2.295420493123259);

    private double total, kcal;
    private double carbohydrate, protein, fat;
    private double sugar, sodium;
    private double cholesterol, saturatedFattyAcid, transFat;

    Nutrients(double total, double kcal
            , double carbohydrate, double protein, double fat
            , double sugar, double sodium
            , double cholesterol, double saturatedFattyAcid, double transFat) {
        this.total = total;
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

    public double getTotal() {
        return total;
    }

    public double getKcal() {
        return kcal;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getSugar() {
        return sugar;
    }

    public double getSodium() {
        return sodium;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public double getSaturatedFattyAcid() {
        return saturatedFattyAcid;
    }

    public double getTransFat() {
        return transFat;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public void setSaturatedFattyAcid(double saturatedFattyAcid) {
        this.saturatedFattyAcid = saturatedFattyAcid;
    }

    public void setTransFat(double transFat) {
        this.transFat = transFat;
    }
}
