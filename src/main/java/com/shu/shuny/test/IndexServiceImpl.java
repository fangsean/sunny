package com.shu.shuny.test;

import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Override
    public String index(String msg) {
        System.out.println("provider: index " + msg + "!");
        return "index " + msg + "!";
    }


}
