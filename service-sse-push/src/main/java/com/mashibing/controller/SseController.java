package com.mashibing.controller;

import com.mashibing.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SseController {
    public static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    /**
     * 建立链接
     * @param userId
     * @param identity
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId, @RequestParam String identity) {
        log.info("用户ID:" + userId + ",身份类型" + identity);
        String mapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitterMap.put(mapKey, sseEmitter);
        return sseEmitter;
    }

    /**
     * 发送消息
     * @param userId 用户ID
     * @param identity 身份类型
     * @param content
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam Long userId, @RequestParam String identity, @RequestParam String content) {
        log.info("用户ID"+userId+",身份"+identity);
        String mapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        try {
            if (sseEmitterMap.containsKey(mapKey)) {
                sseEmitterMap.get(mapKey).send(content);
            } else {
                return "推送失败";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "给用户: " + mapKey + "，发送了消息:" + content;
    }

    /**
     * 关闭链接
     * @param userId
     * @param identity
     * @return
     */
    @GetMapping("/close")
    public String close(@RequestParam Long userId, @RequestParam String identity) {
        String mapKey = SsePrefixUtils.generatorSseKey(userId, identity);
        if (sseEmitterMap.containsKey(mapKey)) {
            sseEmitterMap.remove(mapKey);
        }
        return "关闭链接";
    }
}
