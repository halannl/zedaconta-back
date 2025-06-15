package com.zedaconta.api.service;

import com.zedaconta.api.config.EmailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Serviço responsável pelo envio de e-mails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    /**
     * Envia um e-mail de verificação com o código de verificação.
     *
     * @param to O endereço de e-mail do destinatário
     * @param firstName O nome do usuário
     * @param verificationCode O código de verificação
     */
    @Async
    public void sendVerificationEmail(String to, String firstName, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, 
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                    StandardCharsets.UTF_8.name());

            helper.setFrom(emailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject(emailProperties.getVerificationSubject());

            // Construir o conteúdo do e-mail
            String emailContent = buildVerificationEmailContent(firstName, verificationCode);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("E-mail de verificação enviado para: {}", to);
        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail de verificação para: {}", to, e);
        }
    }

    /**
     * Constrói o conteúdo HTML do e-mail de verificação.
     *
     * @param firstName O nome do usuário
     * @param verificationCode O código de verificação
     * @return O conteúdo HTML do e-mail
     */
    private String buildVerificationEmailContent(String firstName, String verificationCode) {
        StringBuilder content = new StringBuilder();
        content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        content.append("<h2 style='color: #2E4053;'>Verificação de E-mail ZedaConta</h2>");
        content.append("<p>Olá <b>").append(firstName).append("</b>,</p>");
        content.append("<p>Obrigado por se registrar no ZedaConta. Para completar seu registro, por favor verifique seu e-mail usando o código abaixo:</p>");
        content.append("<div style='background-color: #f2f2f2; padding: 15px; text-align: center; font-size: 24px; letter-spacing: 5px; font-weight: bold;'>");
        content.append(verificationCode);
        content.append("</div>");
        content.append("<p>Este código é válido por ").append(emailProperties.getVerificationExpiryMinutes()).append(" minutos.</p>");
        content.append("<p>Se você não solicitou este e-mail, por favor ignore-o.</p>");
        content.append("<p>Atenciosamente,<br>Equipe ZedaConta</p>");
        content.append("</div>");
        return content.toString();
    }
}
