package com.security.lab5.config;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String AUTHORIZATION_COOKIE = "Authorization";
    public static final String[] ROUTES_WHITE_LIST = {"/", "/login", "/registration", "/logout"};

}