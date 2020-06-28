package com.uxi.bambupay.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    protected var progressDialog: ProgressDialog? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel>
            viewModel(): Lazy<VM> = viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    protected open fun showProgressDialog(message: String?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage(message)
            progressDialog!!.show()
        }
    }

    protected open fun dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

}