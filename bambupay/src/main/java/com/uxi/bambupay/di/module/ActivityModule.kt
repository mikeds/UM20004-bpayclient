package com.uxi.bambupay.di.module

import com.uxi.bambupay.view.activity.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeCashOutActivity(): CashOutActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeTransactActivity(): TransactActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeTransactionDetailsActivity(): TransactionDetailsActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeTransactionHistoryActivity(): TransactionHistoryActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeVerificationActivity(): VerificationActivity

}