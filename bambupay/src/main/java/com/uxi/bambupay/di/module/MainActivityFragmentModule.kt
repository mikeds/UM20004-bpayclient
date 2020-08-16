package com.uxi.bambupay.di.module

import com.uxi.bambupay.view.fragment.AboutFragment
import com.uxi.bambupay.view.fragment.FAQFragment
import com.uxi.bambupay.view.fragment.HomeFragment
import com.uxi.bambupay.view.fragment.SettingsFragment
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

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeAboutFragment(): AboutFragment

    @ContributesAndroidInjector
    abstract fun contributeFAQFragment(): FAQFragment

}