package com.kevin.newsdemo.base;

import java.util.Objects;

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

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BaseResult))
            return false;
        BaseResult<?> that = (BaseResult<?>) o;
        return code == that.code &&
          Objects.equals(t, that.t) &&
          Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, t, throwable);
    }
}
