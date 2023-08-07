package com.huang.lightweight.common.exception;

import com.huang.lightweight.common.common.Constants;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.util.common.StringUtils;

/**
 * @Author lightweight
 * @Date 2023/5/24 14:22
 */
public class LightweightException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3913902031489277776L;

    private ErrorCode errCode;

    private String errMsg;

    private Throwable causeThrowable;

    public LightweightException() {
    }

    public LightweightException(final ErrorCode errCode, final String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public LightweightException(final ErrorCode errCode, final Throwable throwable) {
        super(throwable);
        this.errCode = errCode;
        this.setCauseThrowable(throwable);
    }

    public LightweightException(final ErrorCode errCode, final String errMsg, final Throwable throwable) {
        super(errMsg, throwable);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.setCauseThrowable(throwable);
    }

    public ErrorCode getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        if (!StringUtils.isBlank(this.errMsg)) {
            return this.errMsg;
        }
        if (this.causeThrowable != null) {
            return this.causeThrowable.getMessage();
        }
        return Constants.NULL;
    }

    public void setErrCode(final ErrorCode errCode) {
        this.errCode = errCode;
    }

    public void setErrMsg(final String errMsg) {
        this.errMsg = errMsg;
    }

    public void setCauseThrowable(final Throwable throwable) {
        this.causeThrowable = this.getCauseThrowable(throwable);
    }

    private Throwable getCauseThrowable(final Throwable t) {
        if (t.getCause() == null) {
            return t;
        }
        return this.getCauseThrowable(t.getCause());
    }

    @Override
    public String toString() {
        return "ErrCode:" + getErrCode() + ", ErrMsg:" + getErrMsg();
    }

}
