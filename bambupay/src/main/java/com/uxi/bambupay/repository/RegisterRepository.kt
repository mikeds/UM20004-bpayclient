package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.RequestRegister
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
class RegisterRepository @Inject
constructor(
    private val userDao: UserDao,
    private val webService: WebService
) {

    fun loadRegister(request: RequestRegister) : Flowable<GenericApiResponse<User>> {
        return webService.register(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}