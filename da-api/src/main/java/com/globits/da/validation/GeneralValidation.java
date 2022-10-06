package com.globits.da.validation;

import com.globits.da.Constants;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class GeneralValidation {

    public static boolean hasWhiteSpace(String string) {
        return string.matches(Constants.BLANK_REGEX);
    }

    public static boolean isCodeLengthValid(String code) {
        return Pattern.matches(Constants.CODE_REGEX, code);
    }

    public static boolean isNameValid(String name) {
        return Pattern.matches(Constants.NAME_REGEX, name);
    }

    public static boolean isEmailValid(String email) {
        return Pattern.matches(Constants.EMAIL_REGEX, email);
    }

    public static boolean isPhoneValid(String phone) {
        return Pattern.matches(Constants.PHONE_REGEX, phone);
    }

    public static boolean isAgeValid(Integer age) {
        return Pattern.matches(Constants.AGE_REGEX, age.toString());
    }

    public static boolean isEffectiveDateValid(LocalDate effectiveDate) {
        return Pattern.matches(Constants.DATE_DIPLOMA_REGEX, effectiveDate.toString());
    }

    public static boolean isExpirationDateValid(LocalDate effectiveDate, LocalDate expirationDate) {
        if (!Pattern.matches(Constants.DATE_DIPLOMA_REGEX, effectiveDate.toString())) {
            return false;
        }
        return expirationDate.isAfter(effectiveDate);
    }

    public static boolean isEffective(LocalDate expirationDate) {
        LocalDate now = LocalDate.now();
        return expirationDate.isBefore(now);
    }
}
