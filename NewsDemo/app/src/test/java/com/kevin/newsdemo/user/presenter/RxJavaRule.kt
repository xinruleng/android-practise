package com.kevin.newsdemo.user.presenter

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Created by kevin on 2019/07/21 18:27.
 */
class RxJavaRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

                base.evaluate()
            }
        }
    }
}
