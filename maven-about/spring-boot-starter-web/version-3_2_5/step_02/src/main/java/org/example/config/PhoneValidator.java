package org.example.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null / 空串交给 @NotNull / @NotBlank 处理，这里放行
        if (value == null || value.isBlank()) {
            return true;
        }
        return PATTERN.matcher(value).matches();
    }
}
