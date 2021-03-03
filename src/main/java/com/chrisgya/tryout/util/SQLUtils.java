package com.chrisgya.tryout.util;

import org.springframework.util.StringUtils;

import static com.chrisgya.tryout.util.ConstantsUtils.EMAIL_EXIST;
import static com.chrisgya.tryout.util.ConstantsUtils.MOBILE_EXIST;

public class SQLUtils {

    public static String extractSQLErrorMessage(String sqlErrorMessage){
        if(!StringUtils.hasText(sqlErrorMessage)) return sqlErrorMessage;

        if( sqlErrorMessage.contains(":")) {
            var errors = sqlErrorMessage.split(":");
            return errors[errors.length - 1].trim();
        }
        return sqlErrorMessage;
    }

    public static String conflictSQLErrorMessage(String sqlErrorMessage){
        if(!StringUtils.hasText(sqlErrorMessage)) return sqlErrorMessage;

        if( sqlErrorMessage.contains("idx_users_email")) {
            return EMAIL_EXIST;
        }else if( sqlErrorMessage.contains("idx_users_mobile")) {
            return MOBILE_EXIST;
        }

        return sqlErrorMessage;
    }

}
