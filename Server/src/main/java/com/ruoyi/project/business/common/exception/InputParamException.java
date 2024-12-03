package com.ruoyi.project.business.common.exception;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_31 _ 15:34__星期一
 * @package: com.ruoyi.project.business.common.exception
 * @JDK-Version : 1.8
 */
public class InputParamException extends BaseValidException {

    public InputParamException() {
        super();
    }

    public InputParamException(String message) {
        super(message);
    }

    public InputParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputParamException(Throwable cause) {
        super(cause);
    }

    protected InputParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
