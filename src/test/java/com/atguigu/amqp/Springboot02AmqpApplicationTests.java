package com.atguigu.amqp;

import com.atguigu.amqp.bean.Book;
import com.sun.javafx.collections.MappingChange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02AmqpApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){

//        amqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
//        System.out.println("创建成功");

//        amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));

        //创建绑定规则
//        amqpAdmin.declareBinding(
//                new Binding(
//                        "amqpadmin.queue"
//                        ,Binding.DestinationType.QUEUE
//                        ,"amqpadmin.exchange"
//                        ,"amqp.haha"
//                        ,null));

//        amqpAdmin.deleteExchange("amqpadmin.exchange");
        amqpAdmin.deleteQueue("amqpadmin.queue");
    }

    /**
     * 1.单播(点对点)
     */
    @Test
    public void contextLoads() {
        //Message需要自己构造一个;定义消息体内容和消息头
        //rabbitTemplate.send(exchange,routeKey,message);

        //object默认当成消息体，只需要传入要发送的对象，自动序列化发送给rabbitmq
        //rabbitTemplate.convertAndSend(exchange,routeKey,object);

        Map<String,Object> map = new HashMap<>();
        map.put("msg","消息来了");
        map.put("data", Arrays.asList("HelloWorld",123,true));
        //对象被默认序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",new Book("红楼梦","曹雪芹"));
    }

    //接受数据，如何将数据自动的转为json发送出去
    @Test
    public void receive(){
        Object o = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println("o.getClass() = " + o.getClass());
        System.out.println("o = " + o);
    }

    /**
     * 广播(发布订阅型)
     */
    @Test
    public void text1(){
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("牛顿第一定律","科学家"));
    }


}
