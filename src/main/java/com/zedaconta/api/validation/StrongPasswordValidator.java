package com.zedaconta.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para senhas fortes.
 * Verifica se a senha atende aos seguintes critérios:
 * - Entre 6 e 30 caracteres
 * - Pelo menos uma letra maiúscula
 * - Pelo menos uma letra minúscula
 * - Pelo menos um número
 * - Pelo menos um caractere especial
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        // Nada a inicializar
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Null passwords are validated by @NotNull
        if (password == null) {
            return true;
        }

        // Verificar o tamanho
        if (password.length() < 6 || password.length() > 30) {
            return false;
        }

        // Verificar se contém pelo menos uma letra maiúscula
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            return false;
        }

        // Verificar se contém pelo menos uma letra minúscula
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            return false;
        }

        // Verificar se contém pelo menos um número
        if (!DIGIT_PATTERN.matcher(password).find()) {
            return false;
        }

        // Verificar se contém pelo menos um caractere especial
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            return false;
        }

        return true;
    }
}
