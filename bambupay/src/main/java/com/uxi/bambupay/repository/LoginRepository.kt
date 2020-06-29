package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.TokenResponse
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Singleton
class LoginRepository @Inject
constructor(
    private val userDao: UserDao,
    private val webService: WebService) {

    fun loadToken() : Flowable<TokenResponse> {
        val map = HashMap<String, String>()
        map["grant_type"] = "client_credentials"
        return webService.getToken(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadLogin(username: String, password: String) : Flowable<GenericApiResponse<User>> {
        return webService.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveUser(user: User) {
        userDao.copyOrUpdate(user)
    }

}