package com.ruoyi.project.business.common.exception;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-16 13:53 - 星期三
 * @package: com.ruoyi.project.business.common.exception
 * @JDK-Version : 1.8
 */
public class InputListOrderIsNotSupport extends BaseValidException {
    public InputListOrderIsNotSupport() {
        super();
    }

    public InputListOrderIsNotSupport(String message) {
        super(message);
    }

    public InputListOrderIsNotSupport(String message, Throwable cause) {
        super(message, cause);
    }

    public InputListOrderIsNotSupport(Throwable cause) {
        super(cause);
    }

    protected InputListOrderIsNotSupport(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
