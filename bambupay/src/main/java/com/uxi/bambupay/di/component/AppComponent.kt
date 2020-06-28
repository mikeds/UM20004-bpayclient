package com.uxi.bambupay.di.component

import android.app.Application
import com.uxi.bambupay.di.module.ActivityModule
import com.uxi.bambupay.di.module.AppModule
import com.uxi.bambupay.di.module.ComposeModule
import com.uxi.bambupay.di.module.RealmPersistenceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ComposeModule::class,
    ActivityModule::class,
    RealmPersistenceModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: DaggerApplication)

}