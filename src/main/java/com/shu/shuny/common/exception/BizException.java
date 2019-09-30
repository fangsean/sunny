package com.shu.shuny.common.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/9
 */
@Getter
@Setter
public class BizException extends RuntimeException {


    public BizException(Throwable e) {
        super(e);
    }

    public BizException(String errorMsg) {
        super(errorMsg);
    }
}
