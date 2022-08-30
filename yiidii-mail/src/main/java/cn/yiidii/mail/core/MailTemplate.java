package cn.yiidii.mail.core;

/**
 * 邮件操作接口
 *
 * @author ed w
 * @since 1.0
 */
public interface MailTemplate {

    /**
     * 发送简单邮件
     *
     * @param subject    主题
     * @param content    内容
     * @param toWho      需要发送的人
     * @param ccPeoples  需要抄送的人
     * @param bccPeoples 需要密送的人
     */
    void sendSimpleTextMail(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples);


    /**
     * 发送附件邮件
     *
     * @param subject     主题
     * @param content     内容
     * @param toWho       需要发送的人
     * @param ccPeoples   需要抄送的人
     * @param bccPeoples  需要密送的人
     * @param attachments 需要附带的附件
     */
    void sendMail(String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples, String[] attachments);


    /**
     * 发送Html
     *
     * @param subject 主题
     * @param content 内容
     * @param toWho   需要发送的人
     */
    void sendHtmlMail(String subject, String content, String[] toWho);

    /**
     * 发送模板邮件（理论上应该为html邮件）
     *
     * @param subject          主题
     * @param content          内容
     * @param templateFileName 模板名称
     * @param toWho            需要发送的人
     * @param model            模板
     */
    void sendTemplateHtmlMail(String subject, String content, String[] toWho, String templateFileName, Object model);
}
