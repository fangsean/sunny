package com.shu.shuny.test;


import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        return "hello " + msg + "!";
    }


}
