package com.uxi.bambupay.di.module

import com.uxi.bambupay.view.gallery.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class MainActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

}