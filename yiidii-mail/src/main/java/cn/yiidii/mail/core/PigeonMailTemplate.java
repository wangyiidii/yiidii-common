package cn.yiidii.mail.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 邮件操作实现类
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class PigeonMailTemplate implements MailTemplate {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 昵称
     */
    @Value("${spring.mail.nickname}")
    private String nickname;


    @Override
    public void sendSimpleTextMail(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples) {
        doSendMail(subject, content, toWho, ccPeoples, bccPeoples, null);
    }

    @Override
    public void sendMail(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, String[] attachments) {
        doSendMail(subject, content, toWho, ccPeoples, bccPeoples, attachments);
    }


    @Override
    public void sendHtmlMail(String subject, String content, String[] toWho) {
        // 检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
        if (subject == null || toWho == null || toWho.length == 0 || content == null) {
            log.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            //设置邮件的基本信息
            handleBasicInfo(helper, subject, content, toWho, null, null);
            //发送邮件
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败: 主题->{}，异常->{}", subject, e);
        }
    }

    @Override
    public void sendTemplateHtmlMail(String subject, String content, String[] toWho, String templateFileName, Object model) {
        // 检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
        if (subject == null || toWho == null || toWho.length == 0 || content == null) {
            log.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }

        // html
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            // 设置邮件的基本信息
            handleBasicInfo(helper, subject, content, toWho, templateFileName, model);
            // 发送邮件
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发送邮件出错->{}", subject);
        }
    }

    private void doSendMail(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, String[] attachments) {
        if (subject == null || toWho == null || toWho.length == 0 || content == null) {
            log.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }

        // 附件处理，需要处理附件时，需要使用二进制信息，使用 MimeMessage 类来进行处理
        if (Objects.nonNull(attachments) && attachments.length > 0) {
            try {
                // 附件处理需要进行二进制传输
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                boolean continueProcess = handleBasicInfo(helper, subject, content, toWho, ccPeoples, bccPeoples, false);
                // 如果处理基本信息出现错误
                if (!continueProcess) {
                    log.error("邮件基本信息出错: 主题->{}", subject);
                    return;
                }
                // 处理附件
                handleAttachment(helper, subject, attachments);
                // 发送该邮件
                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.error("发送邮件失败: 主题->{}，异常->{}", subject, e);
            }
        } else {
            // 创建一个简单邮件信息对象
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // 设置邮件的基本信息
            handleBasicInfo(simpleMailMessage, subject, content, toWho, ccPeoples, bccPeoples);
            // 发送邮件
            mailSender.send(simpleMailMessage);
        }
    }

    public boolean handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, boolean isHtml) {
        try {
            mimeMessageHelper.setFrom(nickname + '<' + mailProperties.getUsername() + '>');
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, isHtml);
            mimeMessageHelper.setTo(toWho);
            if (ccPeoples != null) {
                mimeMessageHelper.setCc(ccPeoples);
            }

            if (bccPeoples != null) {
                mimeMessageHelper.setBcc(bccPeoples);
            }
            return true;
        } catch (MessagingException e) {
            log.error("邮件基本信息出错->{}, 异常->{}", subject, e);
        }
        return false;
    }

    public void handleBasicInfo(SimpleMailMessage simpleMailMessage, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples) {
        //设置发件人
        simpleMailMessage.setFrom(nickname + '<' + mailProperties.getUsername() + '>');
        //设置邮件的主题
        simpleMailMessage.setSubject(subject);
        //设置邮件的内容
        simpleMailMessage.setText(content);
        //设置邮件的收件人
        simpleMailMessage.setTo(toWho);
        //设置邮件的抄送人
        simpleMailMessage.setCc(ccPeoples);
        //设置邮件的密送人
        simpleMailMessage.setBcc(bccPeoples);
    }

    public void handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho, String templateName, Object model) {
        try {
            //设置发件人
            mimeMessageHelper.setFrom(nickname + '<' + mailProperties.getUsername() + '>');
            //设置邮件的主题
            mimeMessageHelper.setSubject(subject);
            //设置邮件的内容
            if (Objects.nonNull(templateName) && templateName.length() > 0 && Objects.nonNull(model)) {
                mimeMessageHelper.setText(Objects.requireNonNull(parseTemplateContent(templateName, model)), true);
            } else {
                mimeMessageHelper.setText(content, true);
            }
            //设置邮件的收件人
            mimeMessageHelper.setTo(toWho);
        } catch (MessagingException e) {
            log.error("html邮件基本信息出错->{}, 异常->{}", subject, e);
        }
    }

    public void handleAttachment(MimeMessageHelper mimeMessageHelper, String subject, String[] attachmentFilePaths) {
        //判断是否需要处理邮件的附件
        if (Objects.nonNull(attachmentFilePaths) && attachmentFilePaths.length > 0) {
            FileSystemResource resource;
            String fileName;
            //循环处理邮件的附件
            for (String attachmentFilePath : attachmentFilePaths) {
                //获取该路径所对应的文件资源对象
                resource = new FileSystemResource(new File(attachmentFilePath));
                // 判断该资源是否存在，当不存在时仅仅会打印一条警告日志，不会中断处理程序。
                // 也就是说在附件出现异常的情况下，邮件是可以正常发送的，所以请确定你发送的邮件附件在本机存在
                if (!resource.exists()) {
                    log.warn("邮件->{} 的附件->{} 不存在！", subject, attachmentFilePath);
                    continue;
                }
                // 获取资源的名称
                fileName = resource.getFilename();
                try {
                    // 添加附件
                    assert fileName != null;
                    mimeMessageHelper.addAttachment(fileName, resource);
                } catch (MessagingException e) {
                    log.error("邮件->{} 添加附件->{} 出现异常->{}", subject, attachmentFilePath, e.getMessage());
                }
            }
        }
    }

    private String parseTemplateContent(String templateName, Object model) {
        try {
            // 创建配置类
            Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            // 设置字符集
            configuration.setDefaultEncoding("utf-8");
            // 加载模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            // 模板静态化并返回
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            log.error("模板静态化异常", e);
            return null;
        }
    }
}
