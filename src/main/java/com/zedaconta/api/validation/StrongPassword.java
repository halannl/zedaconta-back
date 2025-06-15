package com.zedaconta.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para validar senhas fortes.
 * Uma senha forte deve ter entre 6 e 30 caracteres e conter pelo menos:
 * - Uma letra maiúscula
 * - Uma letra minúscula
 * - Um número
 * - Um caractere especial
 */
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "A senha deve ter entre 6 e 30 caracteres e conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
