package com.goku.oauth;

/**
 * User: user
 * Date: 15/11/25
 * Version: 1.0
 */
public class Constants {
    public static final String FRESH_TOKEN_KEY = "_oauth_fresh_token_key_";
    public static final String ACCESS_TOKEN_KEY = "_oauth_access_token_key_";
    public static final String USER_KEY = "_oauth_user_key_";
    public static final String CLIENT_KEY = "_oauth_client_key_";
    public static final int FRESH_CLIENT_SECONDS = 60 * 60 * 24 *2;
    public static final String AUTHENTICATION_KEY = "_oauth_key_";
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS_DEFAULT = 60 * 60 * 12;
    public static final int REFRESH_TOKEN_VALIDAITY_SECONDS_DEFAULT = 60 * 60 * 24 * 30;
    public static final String OAUTH2ACCESS_TOKEN_KEY = "_oauth2access_token_key_";
    public static int FRESH_USER_SECONDS = 60 * 60 * 24 *1;

}
