package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.registration.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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
) : BaseRepository() {

    fun loadRegister(map: HashMap<String, RequestBody>, fileProfile: File?, fileFrontId: File?, fileBackId: File?) : Flowable<ResultWithMessage<Registration>> {
        val bodyFront = MultipartBody.Part.createFormData("id_front", fileFrontId!!.name, getFileBody(fileFrontId))
        val bodyBack = MultipartBody.Part.createFormData("id_back", fileBackId!!.name, getFileBody(fileBackId))

        if (fileProfile != null) {
            // Create a request body with file and media type
            val fileReqBody: RequestBody = fileProfile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            // Create MultipartBody.Part using file request-body,file name and part name
            val bodyProfile = MultipartBody.Part.createFormData("profile_picture", fileProfile.name, fileReqBody)
            return webService.registerWithFile(map, bodyProfile, bodyFront, bodyBack)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { res ->
                    when (val obj: Registration? = res.response) {
                        null -> ResultWithMessage.Error(false, "")
                        else -> ResultWithMessage.Success(obj, res.successMessage)
                    }
                }
                .onErrorReturn { errorHandler(it) }
        }

        return webService.register(map, bodyFront, bodyBack)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: Registration? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    private fun getFileBody(file: File) : RequestBody {
        return file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    }

    fun loadProvinces() : Flowable<GenericApiResponse<List<Province>>> {
        return webService.getProvinces()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadWorks() : Flowable<ResultWithMessage<List<Work>>> {
        return webService.getWorks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: List<Work>? = res.response) {
                    null -> ResultWithMessage.Error(false, res.errorMessage)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
    }

    fun loadIDTypes() : Flowable<ResultWithMessage<List<IDType>>> {
        return webService.getIdTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: List<IDType>? = res.response) {
                    null -> ResultWithMessage.Error(false, res.errorMessage)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
    }

    fun loadFunds() : Flowable<ResultWithMessage<List<Fund>>> {
        return webService.getFunds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: List<Fund>? = res.response) {
                    null -> ResultWithMessage.Error(false, res.errorMessage)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
    }

}