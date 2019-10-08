package com.shu.shuny.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainServer {
    private static final Logger logger = LoggerFactory.getLogger(MainServer.class);



    public static void main(String[] args) throws Exception {

        //发布服务
         new ClassPathXmlApplicationContext("sunny-server.xml");
        logger.info(" 服务发布完成");
    }
}
