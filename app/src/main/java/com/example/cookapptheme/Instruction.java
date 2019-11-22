package com.example.cookapptheme;

public class Instruction {
    private int InstructionID;
    private String Describtion;
    private byte[] DescribetionImage;
    private int RecipeID;

    public Instruction(String describtion, byte[] describetionImage) {
        Describtion = describtion;
        DescribetionImage = describetionImage;
    }

    public Instruction(int instructionID, String describtion, byte[] describetionImage, int recipeID) {
        InstructionID = instructionID;
        Describtion = describtion;
        DescribetionImage = describetionImage;
        RecipeID = recipeID;
    }

    public int getInstructionID() {
        return InstructionID;
    }

    public void setInstructionID(int instructionID) {
        InstructionID = instructionID;
    }

    public String getDescribtion() {
        return Describtion;
    }

    public void setDescribtion(String describtion) {
        Describtion = describtion;
    }

    public byte[] getDescribetionImage() {
        return DescribetionImage;
    }

    public void setDescribetionImage(byte[] describetionImage) {
        DescribetionImage = describetionImage;
    }

    public int getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(int recipeID) {
        RecipeID = recipeID;
    }
}
