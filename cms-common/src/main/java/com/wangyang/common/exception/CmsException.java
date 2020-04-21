package com.wangyang.common.exception;



public abstract class CmsException extends RuntimeException {
    /**
     * Error errorData.
     */
    private Object errorData;

    public CmsException() {
    }

    public CmsException(String message) {
        super(message);
    }

    public CmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CmsException(Throwable cause) {
        super(cause);
    }

    public CmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Object getErrorData() {
        return errorData;
    }

    public CmsException setErrorData(Object errorData) {
        this.errorData = errorData;
        return this;
    }

}
