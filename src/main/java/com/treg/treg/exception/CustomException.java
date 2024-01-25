package com.treg.treg.exception;

public class CustomException {

    public static class InvalidAccount extends java.lang.Exception {
        public InvalidAccount(String message){
            super(message);
        }
    }

    public static class InvalidCategory extends java.lang.Exception {
        public InvalidCategory(String message){
            super(message);
        }
    }

    public static class InsufficientBalance extends java.lang.Exception {
        public InsufficientBalance(String message){
            super(message);
        }
    }
}
