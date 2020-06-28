package com.uxi.bambupay.di.module

import com.uxi.bambupay.db.UserDao
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Module
class RealmPersistenceModule {

    @Provides
    @Singleton
    fun getDatabase(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    @Singleton
    fun provideUserDao(realm: Realm): UserDao {
        return UserDao(realm)
    }

}