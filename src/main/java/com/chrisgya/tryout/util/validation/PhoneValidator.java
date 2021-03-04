package com.chrisgya.tryout.util.validation;

import com.chrisgya.tryout.util.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(value) || PhoneUtil.isValid(value);
    }
}