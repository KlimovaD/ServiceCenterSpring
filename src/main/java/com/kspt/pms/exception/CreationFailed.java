package com.kspt.pms.exception;

public class CreationFailed extends RuntimeException{

    public CreationFailed(String cause){
        super(cause);
    }
}
