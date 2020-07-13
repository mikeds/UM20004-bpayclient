package com.uxi.bambupay

import android.content.Context
import com.facebook.stetho.Stetho
import com.microblink.MicroblinkSDK
import com.microblink.intent.IntentDataTransferMode
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import com.uxi.bambupay.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm
import io.realm.RealmConfiguration

@Suppress("unused")
class BambuPayApplication : DaggerApplication() {

    //Instantiate Dagger
    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent.inject(this)

        initMicroblink()
        initRealm()
        initStetho()

//        ClassicsFooter.REFRESH_FOOTER_PULLUP = "Pull up to load more"//this.resources.getString(R.string.refresh_footer_pull_up)
//        ClassicsFooter.REFRESH_FOOTER_RELEASE = "Release"//this.resources.getString(R.string.refresh_footer_release)
//        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "Refreshing..."//this.resources.getString(R.string.refresh_footer_refreshing)
//        ClassicsFooter.REFRESH_FOOTER_LOADING = "Loading..."//this.resources.getString(R.string.refresh_footer_loading)
//        ClassicsFooter.REFRESH_FOOTER_FINISH = ""
//        ClassicsFooter.REFRESH_FOOTER_FAILED = "Unable to load"//this.resources.getString(R.string.refresh_footer_failed)
//        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = ""
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

    private fun initMicroblink() {
        // Micro-Blink ID
        MicroblinkSDK.setLicenseKey(getString(R.string.demo_microblink_key), this)
        // use optimised way for transferring RecognizerBundle between activities, while ensuring
        // data does not get lost when Android restarts the scanning activity
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED)
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(
                        RealmInspectorModulesProvider.builder(this)
                            .withDeleteIfMigrationNeeded(true) //if there is any changes in database schema then rebuild bd.
                            .withMetaTables() //extract table meta data
                            .withLimit(10000) //by default limit of data id 250, but you can increase with this
                            .build()
                    )
                    .build())
        }
    }

    @Synchronized
    fun getInstance(): BambuPayApplication? {
        return instance
    }

    companion object {
        lateinit var instance: BambuPayApplication
            private set

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)
                MaterialHeader(context).setColorSchemeColors(R.color.black) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}