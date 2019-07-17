package com.kevin.newsdemo.base;

/**
 * Created by kevin on 2019/07/17 10:41.
 */
public interface CallBack<T> {

    void onSuccess(T t);

    void onFailed(ResultCode code, Throwable throwable);
}
