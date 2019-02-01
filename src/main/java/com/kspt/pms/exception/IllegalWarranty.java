package com.kspt.pms.exception;

public class IllegalWarranty extends RuntimeException{

    private static final String template = "Specified warranty for device %s is illegal";
    public IllegalWarranty(String serial){
        super(String.format(template, serial));
    }
}
