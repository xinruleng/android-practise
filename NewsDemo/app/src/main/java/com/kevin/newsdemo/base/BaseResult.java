package com.kevin.newsdemo.base;

/**
 * Created by kevin on 2019/07/17 13:17.
 */
public class BaseResult<T> {
    ResultCode code;
    T t;
    Throwable throwable;

    private BaseResult(ResultCode code, T t) {
        this.code = code;
        this.t = t;
    }

    private BaseResult(ResultCode code, Throwable throwable) {
        this.code = code;
        this.throwable = throwable;
    }

    public static <T> BaseResult<T> succeed(T t) {
        return new BaseResult(ResultCode.OK, t);
    }

    public static BaseResult error(Throwable t) {
        return new BaseResult(ResultCode.ERROR, t);
    }

    public static BaseResult error(ResultCode code, Throwable t) {
        return new BaseResult(code, t);
    }

    public boolean isOK() {
        return code == ResultCode.OK;
    }

    public T getData() {
        return t;
    }

    public ResultCode getCode() {
        return code;
    }
}
