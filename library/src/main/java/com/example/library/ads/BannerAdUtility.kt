package com.example.library.ads;

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
