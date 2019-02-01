package com.kspt.pms.exception;

public class ElementNotFound extends RuntimeException {

    private static final String template = "%s with id: %s not found";
    public ElementNotFound(String element, String id) {
        super(String.format(template, element, id));
    }
}
