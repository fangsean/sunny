package com.shu.shuny.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImpl implements IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Override
    public String index(String msg) {
        logger.info("provider: index " + msg + "!");
        return "index " + msg + "!";
    }


}
