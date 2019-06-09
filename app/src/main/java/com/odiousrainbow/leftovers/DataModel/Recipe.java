package com.odiousrainbow.leftovers.DataModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("serial")
public class Recipe implements Serializable {
    private String imageUrl;
    private String name;
    private String instruction;
    private String serving;
    private List<Ingredient> ingredients;
    private String cookingTime;
    private String totalCal;

    public  Recipe(){}

    public Recipe(String imageUrl, String name, String instruction,String serving,String cookingTime,String totalCal, List<Ingredient> ingredients){
        this.imageUrl = imageUrl;
        this.name = name;
        //this.instruction = instruction.replace("#", "\n");
        this.instruction = instruction;
        this.serving = serving;
        this.cookingTime = cookingTime;
        this.totalCal = totalCal;
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

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getTotalCal() {
        return totalCal;
    }

    public void setTotalCal(String totalCal) {
        this.totalCal = totalCal;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", serving='" + serving + '\'' +
                ", ingredients=" + ingredients +
                ", cookingTime='" + cookingTime + '\'' +
                ", totalCal='" + totalCal + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        boolean eqs = false;
        if(this.name.equals(((Recipe) o).name) &&
                this.imageUrl.equals(((Recipe) o).imageUrl) &&
                this.instruction.equals(((Recipe) o).instruction)){
            eqs = true;
        }
        return eqs;
    }

}
