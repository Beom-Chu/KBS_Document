package sendMail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SendEmailUtil {
	protected final static Logger logger = LoggerFactory.getLogger(SendEmailUtil.class);		

//	public final static String SMTP_ADDR    = "relay.hyosung.com";  
//	public final static String SMTP_HOST    = "mail.smtp.host";  
	
	public final static String SMTP_ADDR    = System.getProperty("smtpAddr");	// smtp addr 
	public final static String SMTP_HOST    = System.getProperty("smtpHost");	// smtp host 

	public static  boolean sendMailWithoutFile(String senderEmail, String receiverEmail, String subject, String content) throws Exception
	{
		System.out.println("SMTP_ADDR1 => "+ SMTP_ADDR);
		System.out.println("SMTP_HOST1 => "+ SMTP_HOST);
		Properties prop = new Properties();

		prop.put(SMTP_HOST, SMTP_ADDR);
	    //prop.put("mail.smtp.starttls.enable", "false");
	   // properties.put("mail.smtp.user", user);
	   // properties.put("mail.smtp.auth", "true");
	
	    Session sess = javax.mail.Session.getInstance(prop, null);

	    MimeMessage msg = new MimeMessage(sess);
	    
	    
	    try {

	        // 메일 발송자 설정
	        //msg.setFrom( new InternetAddress(sender) );

          InternetAddress[] fromAddress = InternetAddress.parse(senderEmail);
          fromAddress[0].setPersonal(senderEmail, "euc-kr");
          msg.setFrom(fromAddress[0]);


	        // 메일 수신자 설정
          //msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
          InternetAddress[] toAddress = InternetAddress.parse(receiverEmail);
          toAddress[0].setPersonal(receiverEmail, "euc-kr");
          msg.setRecipients(Message.RecipientType.TO, toAddress);

	        // 메일 제목 설정
	        msg.setSubject(subject, "euc-kr");

	        // 메일 본문 내용
	        //msg.setText(content);
	        msg.setContent(content, "text/html; charset=euc-kr");

	        msg.setSentDate(new Date());


	        //메일을 전송한다.
	        Transport.send(msg);
	        return true;
	    }catch (RuntimeException e) {
	    	logger.error("caught Exception", e);
	    	return false;
		}catch(Exception e) {
			logger.error("caught Exception", e);
	        return false;
	    }
	}
	
	
}