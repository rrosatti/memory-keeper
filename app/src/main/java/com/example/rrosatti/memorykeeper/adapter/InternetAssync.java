package com.example.rrosatti.memorykeeper.adapter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rrosatti.memorykeeper.model.Email;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by root on 26/10/17.
 */

public class InternetAssync extends AsyncTask<Email, Void, Void> {

    private Session session;

    public InternetAssync(Session session) {
        this.session = session;
    }

    @Override
    protected Void doInBackground(Email... args) {
        Email email = args[0];
        try {
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress());
            Address[] toUser = InternetAddress.parse(email.getToUser());
            message.setRecipients(javax.mail.Message.RecipientType.TO, toUser);
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            Transport.send(message);
            Log.e("ok","Email sent sucess");

        } catch (MessagingException ex) {
            Log.e("ERROR", "Error preparing email" + ex.getMessage());
            return null;
        }
        return null;
    }

}
