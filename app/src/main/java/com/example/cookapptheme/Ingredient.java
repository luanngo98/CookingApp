package com.example.cookapptheme;

public class Ingredient {
    private int IngredientID;
    private String IngredientName;
    private int RecipeID;

    public Ingredient(String ingredientName) {
        IngredientName = ingredientName;
    }

    public Ingredient(int ingredientID, String ingredientName, int recipeID) {
        IngredientID = ingredientID;
        IngredientName = ingredientName;
        RecipeID = recipeID;
    }

    public int getIngredientID() {
        return IngredientID;
    }

    public void setIngredientID(int ingredientID) {
        IngredientID = ingredientID;
    }

    public String getIngredientName() {
        return IngredientName;
    }

    public void setIngredientName(String ingredientName) {
        IngredientName = ingredientName;
    }

    public int getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(int recipeID) {
        RecipeID = recipeID;
    }
}
