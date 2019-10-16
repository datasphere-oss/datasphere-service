/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.core.common.utils;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.*;
import javax.activation.*;

public class EmailUtils {
	private String host = ""; // smtp server
    private String from = ""; // sender address
    private String to = ""; // Recipient's address
    private String affix = ""; // Attachment url
    private String affixName = ""; // affix Name
    private String user = ""; // username
    private String pwd = ""; // password
    private String subject = ""; // subject

    public EmailUtils(String host, String user, String pwd,String from) {
        this.host = host;
        this.user = user;
        this.pwd = pwd;
        this.from=from;
    }
    public void setAddress(String to, String subject) {
        this.from = from;
        this.to = to;
        this.subject = subject;
    }

    public void setAffix(String affix, String affixName) {
        this.affix = affix;
        this.affixName = affixName;
    }

    public void send(String text) {


        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(text, "text/html; charset=utf-8");
            multipart.addBodyPart(contentPart);
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(affix);
            messageBodyPart.setDataHandler(new DataHandler(source));
            // It is very important here that the following Base64 encoded conversions will ensure that your Chinese attachment title will not become garbled when sent.
            @SuppressWarnings("restriction")
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            messageBodyPart.setFileName("=?GBK?B?"
                    + enc.encode(affixName.getBytes()) + "?=");
            //multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(host, user, pwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
