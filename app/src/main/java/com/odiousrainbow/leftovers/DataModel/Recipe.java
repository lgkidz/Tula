package com.odiousrainbow.leftovers.DataModel;

import java.util.List;

public class Recipe {
    private String imageUrl;
    private String name;

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    private String instruction;
    private String serving;
    private List<Ingredient> ingredients;

    public  Recipe(){}

    public Recipe(String imageUrl, String name, String instruction,String serving, List<Ingredient> ingredients){
        this.imageUrl = imageUrl;
        this.name = name;
        this.instruction = instruction.replace("#", "\n");
        this.serving = serving;
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
