/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SmtpTest {


	public SmtpTest() {
		
		
	}

	
	@Test
	public void testSendMail(){
		JavaMailSender sender = newMailSender();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(sender.createMimeMessage(), true);
			
			helper.setFrom("andang311@inkium.com");
			helper.setTo("andang72@naver.com");
			
			helper.setSubject("테스트");
			helper.setText("내용없음", "<strong>내용없음</strong>"); //("<strong>내용없음</strong>", true);
			
			sender.send(helper.getMimeMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		SimpleMailMessage message = new SimpleMailMessage();

		
		
		MimeMessage mm = sender.createMimeMessage();

		message.setFrom("andang311@inkium.com");
		message.setTo("andang72@naver.com");
		message.setSubject("테스트 메일");
		message.setText("내용없음");
		sender.send(message);
	*/
	
	}
	
	
	
	public static JavaMailSender newMailSender(){		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("222.122.52.4");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.put("mail.smtp.auth", "false");
		sender.setJavaMailProperties(javaMailProperties);
		sender.setDefaultEncoding("UTF-8");
		sender.setPort(25);
		sender.setProtocol("smtp");		
		return sender;
	} 
}
