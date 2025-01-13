package com.example.flascash.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class IbanValidator implements ConstraintValidator<ValidIban, String> {
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$");

    @Override
    public void initialize(ValidIban constraintAnnotation) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null) {
            return false;
        }
        return IBAN_PATTERN.matcher(iban).matches();
    }
}