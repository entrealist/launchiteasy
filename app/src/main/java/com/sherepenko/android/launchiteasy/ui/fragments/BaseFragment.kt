package com.sherepenko.android.launchiteasy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.core.KoinComponent
import timber.log.Timber

abstract class BaseFragment(
    @LayoutRes private val contentLayoutRes: Int
) : Fragment(), KoinComponent {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(contentLayoutRes, container, false)

    override fun onResume() {
        super.onResume()
        setCurrentScreen()
    }

    private fun setCurrentScreen() {
        Timber.d("Current fragment: ${javaClass.simpleName}")
        FirebaseAnalytics.getInstance(requireActivity())
            .logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to javaClass.simpleName,
                    FirebaseAnalytics.Param.SCREEN_CLASS to javaClass.canonicalName
                )
            )
    }
}
