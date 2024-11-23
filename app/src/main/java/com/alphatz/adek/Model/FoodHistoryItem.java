package com.alphatz.adek.Model;

public class FoodHistoryItem {
    private final String date;
    private final String foodName;
    private final int calories;
    private final String portion;

    public FoodHistoryItem(String date, String foodName, int calories, String portion) {
        this.date = date;
        this.foodName = foodName;
        this.calories = calories;
        this.portion = portion;
    }

    public String getDate() {
        return date;
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
}
