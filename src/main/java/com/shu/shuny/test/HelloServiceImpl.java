package com.shu.shuny.test;


public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String somebody) {
        System.out.println("sayHello");
        return "hello " + somebody + "!";
    }


}
