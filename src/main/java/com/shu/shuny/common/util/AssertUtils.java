package com.shu.shuny.common.util;

import com.shu.shuny.common.exception.BizException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 10:43
 */
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class AssertUtils {
    public static void checkNull(Object o) {
        if (o == null) {
            throw new BizException("源数据为空");
        }

    }
}
