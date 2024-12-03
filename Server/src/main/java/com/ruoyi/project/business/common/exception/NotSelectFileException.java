package com.ruoyi.project.business.common.exception;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_31 _ 15:16__星期一
 * @package: com.ruoyi.project.business.common.exception
 * @JDK-Version : 1.8
 */
public class NotSelectFileException extends BaseValidException {

    public NotSelectFileException() {
        super();
    }

    public NotSelectFileException(String message) {
        super(message);
    }

    public NotSelectFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSelectFileException(Throwable cause) {
        super(cause);
    }

    protected NotSelectFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
