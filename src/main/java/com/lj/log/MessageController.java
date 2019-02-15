package com.lj.log;

import com.lj.log.util.LogUtil;
import com.lj.log.model.IbeLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Profile("stomp")
@RestController
public class MessageController {
    private String preSize;
    private final SimpMessagingTemplate template;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/message")
    public void message() {
        List<IbeLog> loglist = LogUtil.getLogList();
        template.convertAndSend("/subscribe/log", loglist);
    }

    @Scheduled(fixedDelay = 1000)
    public void pingLog() {
        if(StringUtils.isEmpty(preSize)){
            preSize=LogUtil.checkFile();
        }else{
            String thisSize = LogUtil.checkFile();
            if(!thisSize.equalsIgnoreCase(preSize)){
                List<IbeLog> loglist = LogUtil.getLogList();
                preSize = thisSize;
                template.convertAndSend("/subscribe/log", loglist);
            }
        }
    }
}
