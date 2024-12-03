package com.ruoyi.project.business.common.exception;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_31 _ 15:34__星期一
 * @package: com.ruoyi.project.business.common.exception
 * @JDK-Version : 1.8
 */
public class BaseValidException extends Exception {
    public BaseValidException() {
        super();
    }

    public BaseValidException(String message) {
        super(message);
    }

    public BaseValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseValidException(Throwable cause) {
        super(cause);
    }

    protected BaseValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
