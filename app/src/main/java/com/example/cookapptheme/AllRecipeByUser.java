package com.example.cookapptheme;

import java.util.Date;

public class AllRecipeByUser {
    private String userName;
    private String userImage;
    private String recipeName;
    private String recipeImage;
    private String recipeSummary;
    private String recipeDate;

    public AllRecipeByUser(String userName, String userImage, String recipeName, String recipeImage, String recipeSummary, String recipeDate) {
        this.userName = userName;
        this.userImage = userImage;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeSummary = recipeSummary;
        this.recipeDate = recipeDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getRecipeSummary() {
        return recipeSummary;
    }

    public void setRecipeSummary(String recipeSummary) {
        this.recipeSummary = recipeSummary;
    }

    public String getRecipeDate() {
        return recipeDate;
    }

    public void setRecipeDate(String recipeDate) {
        this.recipeDate = recipeDate;
    }
}
