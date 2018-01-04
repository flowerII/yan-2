package cn.qianshu.yan.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

    public void sendHtmlMail(String to, String subject, String content);

}
