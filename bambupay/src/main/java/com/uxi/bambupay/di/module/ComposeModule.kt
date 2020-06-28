package com.uxi.bambupay.di.module

import com.uxi.bambupay.view.activity.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ComposeModule {

    @ContributesAndroidInjector
    internal abstract fun contributeViewModelActivity(): BaseActivity

}