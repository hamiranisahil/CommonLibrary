package com.example.library.ads;

import com.example.common.util.MyLog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

public class BannerAdUtility {
    companion object {
        var BANNER_UNIT_ID = ""
    }

    fun bannerAdLoad(testDeviceId: String, adView: AdView) {
        val adRequestBuilder = AdRequest.Builder()
        if (testDeviceId != null)
            adRequestBuilder.addTestDevice(testDeviceId).build()

        val adRequest = adRequestBuilder.build()
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                when (p0) {
                    AdRequest.ERROR_CODE_INTERNAL_ERROR -> {
                        MyLog().printLog(
                            "AdRequest",
                            "onAdFailedToLoad: code: ($p0 - ERROR_CODE_INTERNAL_ERROR) msg: Something happened internally; for instance, an invalid response was received from the ad server."
                        )

                    }
                    AdRequest.ERROR_CODE_INVALID_REQUEST -> {
                        MyLog().printLog(
                            "AdRequest",
                            "onAdFailedToLoad: code: ($p0 - ERROR_CODE_INTERNAL_ERROR) msg: The ad request was invalid; for instance, the ad unit ID was incorrect."
                        )

                    }
                    AdRequest.ERROR_CODE_NETWORK_ERROR -> {
                        MyLog().printLog(
                            "AdRequest",
                            "onAdFailedToLoad: code: ($p0 - ERROR_CODE_INTERNAL_ERROR) msg: The ad request was unsuccessful due to network connectivity."
                        )

                    }
                    AdRequest.ERROR_CODE_NO_FILL -> {
                        MyLog().printLog(
                            "AdRequest",
                            "onAdFailedToLoad: code: ($p0 - ERROR_CODE_INTERNAL_ERROR) msg: The ad request was successful, but no ad was returned due to lack of ad inventory."
                        )
                    }
                }
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }
        }
        adView.loadAd(adRequest)
    }
}
