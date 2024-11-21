package com.alphatz.adek.Model;

public class FoodHistoryItem {
    private String foodName;
    private int calories;
    private String portion;
    private String date;

    public FoodHistoryItem(String foodName, int calories, String portion, String date) {
        this.foodName = foodName;
        this.calories = calories;
        this.portion = portion;
        this.date = date;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories() {
        return calories;
    }

    public String getPortion() {
        return portion;
    }

    public String getDate() {
        return date;
    }
}
