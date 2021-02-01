package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.RequestRegister
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.Province
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
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

    fun loadRegister(map: HashMap<String, RequestBody>, file: File? /*request: MultipartBody*/) : Flowable<GenericApiResponse<User>> {
        if (file != null) {
            // Create a request body with file and media type
            val fileReqBody: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            // Create MultipartBody.Part using file request-body,file name and part name
            val body = MultipartBody.Part.createFormData("avatar_image", file?.name, fileReqBody)
            return webService.registerWithFile(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        return webService.register(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadProvinces() : Flowable<GenericApiResponse<List<Province>>> {
        return webService.getProvinces()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}