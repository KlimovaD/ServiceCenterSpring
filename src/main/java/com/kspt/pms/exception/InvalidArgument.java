package com.kspt.pms.exception;

public class InvalidArgument extends RuntimeException{
    private static String template = "Invalid usage of % in %. Value %s";

    public InvalidArgument(String argument, String cause, String value){
        super(String.format(template, argument, cause, value));
    }
}
