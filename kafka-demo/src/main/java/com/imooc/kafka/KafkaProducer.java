package com.imooc.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @GetMapping("/imooc/kafka/topic/send")
    public void send(@RequestParam String message) {
        template.send("imooc-kafka-topic", message);
    }

}
