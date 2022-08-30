package cn.yiidii.mail.config;

import cn.yiidii.mail.core.MailTemplate;
import cn.yiidii.mail.core.PigeonMailTemplate;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

/**
 * mail自动配置
 *
 * @author ed w
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

    @PostConstruct
    public void init() {
        System.err.println("MailAutoConfiguration");
    }
    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Bean
    public MailTemplate mailTemplate() {
        System.err.println("MailTemplate");
        return new PigeonMailTemplate(mailSender, mailProperties, freeMarkerConfigurer);
    }
}
