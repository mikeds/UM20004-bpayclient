package com.uxi.bambupay.repository

import com.uxi.bambupay.db.UserDao
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class SettingsRepository @Inject constructor(private val userDao: UserDao)  {

    fun resetDb() {
        userDao.deleteAll()
    }

}