package com.odiousrainbow.leftovers.DataModel;

import java.util.List;

public class IngredientCategory {
    private String name;
    private String imageURL;
    private List<String> prods;

    public IngredientCategory(){}

    public IngredientCategory(String name, String imageURL, List<String> prods) {
        this.name = name;
        this.imageURL = imageURL;
        this.prods = prods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getProds() {
        return prods;
    }

    public void setProds(List<String> prods) {
        this.prods = prods;
    }
}
