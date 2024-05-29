package ru.itis.reststub.services;

public interface EmailSenderService {

    boolean sendConfirmCodeEmail(String email, String code);

}
