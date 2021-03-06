package com.sherepenko.android.launchiteasy.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import com.sherepenko.android.launchiteasy.data.AppState
import timber.log.Timber

class AppStateLiveData(
    private val context: Context
) : MutableLiveData<Event<AppState>>() {

    companion object {
        private const val TAG = "AppState"
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_PACKAGE_ADDED -> {
                    Timber.tag(TAG).i("Newly installed package: ${intent.data}")
                    postValue(Event(AppState.ADDED))
                }
                Intent.ACTION_PACKAGE_CHANGED -> {
                    Timber.tag(TAG).i("Package state changed: ${intent.data}")
                    postValue(Event(AppState.UPDATED))
                }
                Intent.ACTION_PACKAGE_REMOVED -> {
                    Timber.tag(TAG).i("Package fully removed: ${intent.data}")
                    postValue(Event(AppState.REMOVED))
                }
                Intent.ACTION_PACKAGE_REPLACED -> {
                    Timber.tag(TAG).i("Package has been replaced: ${intent.data}")
                    postValue(Event(AppState.UPDATED))
                }
                else -> {
                    // ignore
                }
            }
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addAction(Intent.ACTION_PACKAGE_CHANGED)
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addAction(Intent.ACTION_PACKAGE_REPLACED)
        addDataScheme("package")
    }

    init {
        postValue(Event(AppState.UPDATED))
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(receiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiver)
    }
}
