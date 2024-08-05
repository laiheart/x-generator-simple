package com.ection.platform.drivermanager.utils;

import com.ection.platform.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lsx
 * @date 2024-07-10
 */
public class ThrowUtils {

    public static void throwIf(boolean expression, RuntimeException exception) {
        if (expression) {
            throw exception;
        }
    }

    public static void throwIf(boolean expression, String errMsg) {
        throwIf(expression, new BusinessException(errMsg));
    }

    public static void throwIfNull(Object obj, String errMsg) {
        throwIf(obj == null, errMsg);
    }

    public static void throwIfBlank(String str, String errMsg) {
        throwIf(StringUtils.isBlank(str), errMsg);
    }
}
