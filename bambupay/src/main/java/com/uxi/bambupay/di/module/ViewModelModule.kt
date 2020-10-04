package com.uxi.bambupay.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uxi.bambupay.di.factory.ViewModelFactory
import com.uxi.bambupay.di.factory.ViewModelKey
import com.uxi.bambupay.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(loginViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserTokenViewModel::class)
    internal abstract fun bindUserTokenViewModel(userTokenViewModel: UserTokenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel::class)
    internal abstract fun bindTransactionViewModel(transactionViewModel: TransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CashOutViewModel::class)
    internal abstract fun bindCashOutViewModel(cashOutViewModel: CashOutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CashInViewModel::class)
    internal abstract fun bindCashInViewModel(cashInViewModel: CashInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyViewModel::class)
    internal abstract fun bindVerifyViewModel(verifyViewModel: VerifyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InstaPayViewModel::class)
    internal abstract fun bindInstaPayViewModel(instaPayViewModel: InstaPayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QRCodeViewModel::class)
    internal abstract fun bindQRCodeViewModel(qrCodeViewModel: QRCodeViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}