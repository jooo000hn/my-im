package com.goku.oauth.authentication;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 验证密码
 * User: user
 * Date: 15/11/5
 * Version: 1.0
 */
public class AcPasswordEncoder implements PasswordEncoder {
	
    @Override
    public String encodePassword(String rawPass, Object salt) {
    	
        String saltedPass = mergePasswordAndSalt(rawPass, ( salt == null ? defaultSalt : salt ), false);
        MessageDigest messageDigest = getMessageDigest();
        byte[] digest;
        try {
            digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
        return new String(Hex.encodeHex(digest));
    }
    
    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        if (encPass == null) {
            return false;
        }
        String pass2 = encodePassword(rawPass, salt);
        return StringUtils.equals(encPass,pass2);
    }

    protected final MessageDigest getMessageDigest() {
        String algorithm = "MD5";
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm ["
                    + algorithm + "]");
        }
    }

    protected String mergePasswordAndSalt(String password, Object salt,
                                          boolean strict) {
        if (password == null) {
            password = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1)
                    || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException(
                        "Cannot use { or } in salt.toString()");
            }
        }
        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 混淆码。防止破解。
     */
    private String defaultSalt;

    /**
     * 获得混淆码
     *
     * @return
     */
    public String getDefaultSalt() {
        return defaultSalt;
    }

    /**
     * 设置混淆码
     *
     * @param defaultSalt
     */
    public void setDefaultSalt(String defaultSalt) {
        this.defaultSalt = defaultSalt;
    }

}
