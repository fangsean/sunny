package com.shu.shuny.test;


import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        System.out.println("provider: hello " + msg + "!");
        return "hello " + msg + "!";
    }


}
