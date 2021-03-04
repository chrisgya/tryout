package com.chrisgya.tryout.util.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        try {
            var region = "NG";
            if (value.startsWith("+")) {
                region = phoneUtil.getRegionCodeForCountryCode(Integer.parseInt(value.substring(1, 3)));
            }
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(value, region);
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            log.error("NumberParseException was thrown: {}", e);
            return false;
        }
    }
}