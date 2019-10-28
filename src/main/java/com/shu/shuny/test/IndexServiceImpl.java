package com.shu.shuny.test;

import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Override
    public String index(String msg) {
        return "index " + msg + "!";
    }


}
