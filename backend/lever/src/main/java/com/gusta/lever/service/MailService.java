package com.gusta.lever.service;

import com.gusta.lever.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void pickupRequestMail(ProductDTO dto) {
        String scheduledDate = formatter.format(dto.getScheduledDate());

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(dto.getCompanyEmail());
        message.setSubject("Coleta para o dia " + scheduledDate + " agendada com sucesso!");
        message.setText("A data prevista para a coleta é " + scheduledDate + " porém pode estar sujeita a alterações pela empresa X." +
                " Caso a data for alterada, será enviado outra mensagem através deste mesmo endereço de Email.");
        mailSender.send(message);
        pickupRequestMailToBec(dto, scheduledDate);
    }

    private void pickupRequestMailToBec(ProductDTO dto, String scheduledDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        // Enter the email address of the company responsible for collecting the items.
        message.setTo("");
        message.setSubject("Coleta para o dia: " + scheduledDate);
        message.setText(
                "   - Empresa: " + dto.getCompany() +
                        "\n   - CNPJ: " + dto.getCnpj() +
                        "\n   - Email para contato: " + dto.getCompanyEmail() +
                        "\n   - Endereço para coleta: " + dto.getAddress() +
                        "\n   - Tipo de Resíduo: " + dto.getProdType() +
                        "\n   - Peso: " + dto.getWeight() +
                        "\n   - Quantidade: " + dto.getAmount() +
                        "\n   - Observações: " + dto.getWarnings()
        );
        mailSender.send(message);
    }

    public void warnClientAboutCollectDate(String companyEmail, String date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(companyEmail);
        message.setSubject("Alteração para a data de coleta");
        message.setText("A data para a coleta de seu pacote foi alterada para o dia " + date);
        mailSender.send(message);
    }
}
