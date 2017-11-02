package com.example.rrosatti.memorykeeper.adapter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rrosatti.memorykeeper.model.Email;

import java.io.File;
import java.io.IOException;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by root on 26/10/17.
 */

public class InternetAsync extends AsyncTask<Email, Void, Void> {

    private Session session;

    public InternetAsync(Session session) {
        this.session = session;
    }

    @Override
    protected Void doInBackground(Email... args) {
        Email email = args[0];
        try {
            MimeMessage message = new MimeMessage(session);
            MimeBodyPart mbp1 = new MimeBodyPart();
            MimeBodyPart mbp2 = new MimeBodyPart();

            message.setFrom(new InternetAddress());
            Address[] toUser = InternetAddress.parse(email.getToUser());
            message.setRecipients(javax.mail.Message.RecipientType.TO, toUser);
            message.setSubject(email.getSubject());

            mbp1.setText(email.getText());

            //mbp2.setDataHandler(new DataHandler(email.getQrCodePath(), "image/jpg"));
            File file = new File(email.getQrCodePath());
            mbp2.attachFile(file);
            mbp2.setFileName("qrcode.jpg");
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            message.setContent(mp);
            Transport.send(message);

            Log.e("ok","Email sent successfully");

        } catch (MessagingException | IOException ex) {
            Log.e("ERROR", "Error while trying to send email: " + ex.getMessage());
            return null;
        }
        return null;
    }

}
