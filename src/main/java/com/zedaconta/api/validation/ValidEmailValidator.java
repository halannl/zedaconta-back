package com.zedaconta.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para endereços de e-mail.
 * Utiliza uma expressão regular para validar o formato do e-mail.
 */
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Nada a inicializar
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // Null emails are validated by @NotNull
        if (email == null) {
            return true;
        }
        
        return pattern.matcher(email).matches();
    }
}
