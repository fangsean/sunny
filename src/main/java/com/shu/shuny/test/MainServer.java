package com.shu.shuny.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainServer {


    public static void main(String[] args) throws Exception {

        //发布服务
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("sunny-server.xml");
        System.out.println(" 服务发布完成");
    }
}
