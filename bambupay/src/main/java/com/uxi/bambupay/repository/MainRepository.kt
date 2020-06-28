package com.uxi.bambupay.repository

import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.User
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/29/20.
 * hunterxer31@gmail.com
 */
class MainRepository @Inject constructor(private val userDao: UserDao) {

    fun loadCurrentUser(): User? {
        return userDao.getCurrentUser()
    }

}