package com.kspt.pms.exception;


import com.kspt.pms.entity.User;

public class AccessPermission extends RuntimeException {

    private static final String template = "Access denied for user %s.";
    public AccessPermission(User user) {
        super(String.format(template, user.getName()));
    }
}
