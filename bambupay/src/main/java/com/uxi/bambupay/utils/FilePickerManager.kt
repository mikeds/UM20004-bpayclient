package com.uxi.bambupay.utils

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uxi.bambupay.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 9/21/20.
 * hunterxer31@gmail.com
 */
class FilePickerManager @Inject constructor() {

    enum class Type {
        CAMERA, GALLERY, DOCUMENT
    }

    sealed class Result {
        data class Image(val file: File) : Result()
        data class Document(val originalFilename: String, val file: File) : Result()
        object Cancelled : Result()
    }

    fun pickFile(activity: FragmentActivity, type: Type): Single<Result> =
        RxPermissions(activity)
            .request(*getPermissions(type).toTypedArray())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { granted ->
                if (!granted) onPermissionNotGranted(activity, type)
            }
            .filter { it }
            .flatMap {
                when (type) {
                    Type.CAMERA -> RxPaparazzo.single(activity)
                        .useInternalStorage()
                        .usingCamera()
                    Type.GALLERY -> RxPaparazzo.single(activity)
                        .useInternalStorage()
                        .usingGallery()
                    Type.DOCUMENT -> RxPaparazzo.single(activity)
                        .useDocumentPicker()
                        .usingFiles()
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .map {
                when (it.resultCode()) {
                    Activity.RESULT_OK -> when (type) {
                        Type.CAMERA,
                        Type.GALLERY -> Result.Image(it.data().file)
                        Type.DOCUMENT -> Result.Document(it.data().filename, it.data().file)
                    }
                    else -> Result.Cancelled
                }
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .singleOrError()

    private fun getPermissions(type: Type): List<String> = when (type) {
        Type.CAMERA -> listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        Type.GALLERY -> listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        Type.DOCUMENT -> listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun onPermissionNotGranted(activity: Activity, type: Type) {
        val builder = AlertDialog.Builder(activity)
        val title = when (type) {
            Type.CAMERA -> activity.getString(R.string.me_permission_camera_title)
            Type.GALLERY -> activity.getString(R.string.me_permission_gallery_title)
            Type.DOCUMENT -> activity.getString(R.string.me_permission_document_title)
        }
        val msg = when (type) {
            Type.CAMERA -> activity.getString(R.string.me_permission_camera_msg)
            Type.GALLERY -> activity.getString(R.string.me_permission_gallery_msg)
            Type.DOCUMENT -> activity.getString(R.string.me_permission_document_msg)
        }
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(activity.getString(R.string.me_permission_btn)) { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

}