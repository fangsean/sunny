package com.shu.shuny.test;


public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        return "hello " + msg + "!";
    }


}
