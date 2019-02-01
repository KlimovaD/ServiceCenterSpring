package com.kspt.pms.exception;

public class InvalidPaymentData extends RuntimeException{
    public InvalidPaymentData(String message){
        super(message);
    }
}
