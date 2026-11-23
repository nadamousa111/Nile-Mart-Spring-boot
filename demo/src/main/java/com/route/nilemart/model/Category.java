package com.route.nilemart.model;

public enum Category {

    ELECTRONICS("Electronics"),
    FASHION("Fashion"),
    HOME_KITCHEN("Home & Kitchen"),
    BEAUTY("Beauty"),
    BOOKS("Books");

    private String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}