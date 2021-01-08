package it.polimi.db2.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
