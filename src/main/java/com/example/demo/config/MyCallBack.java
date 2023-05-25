package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{

    //注入
    @Qualifier
    private RabbitTemplate rabbitTemplate;

    //
    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }



    /**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData     发消息时自己填写的
     * 消息相关数据
     * ack
     * 交换机是否收到消息
     * cause
     * 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id=correlationData!=null?correlationData.getId():"";
        if(ack){
            log.info("交换机已经收到 id 为:{}的消息",id);
        }else{
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,cause);
        }
    }


    //当消息无法路由的时候的回调方法
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String
            exchange, String routingKey) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因 :{}, 路 由 key:{}",new
                String(message.getBody()),exchange,replyText,routingKey);
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {

    }
}

