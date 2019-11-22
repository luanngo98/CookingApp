package com.example.cookapptheme;

import java.util.Date;

public class Recipe {
    private int RecipeID;
    private String RecipeName;
    private String RecipeSummary;
    private Date RecipeDate;
    private int UserID;

    public Recipe(int recipeID, String recipeName, String recipeSummary, Date recipeDate, int userID) {
        RecipeID = recipeID;
        RecipeName = recipeName;
        RecipeSummary = recipeSummary;
        RecipeDate = recipeDate;
        UserID = userID;
    }


    public int getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(int recipeID) {
        RecipeID = recipeID;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public String getRecipeSummary() {
        return RecipeSummary;
    }

    public void setRecipeSummary(String recipeSummary) {
        RecipeSummary = recipeSummary;
    }

    public Date getRecipeDate() {
        return RecipeDate;
    }

    public void setRecipeDate(Date recipeDate) {
        RecipeDate = recipeDate;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
