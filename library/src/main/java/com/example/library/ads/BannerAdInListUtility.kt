package com.example.library.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class BannerAdInListUtility {

    val ITEMS_PER_AD = 0

    fun addBannerAd(
        context: Context,
        listData: ArrayList<Any>,
        totalItemsAfterAd: Int,
        bannerAdUnitId: String,
        bannerSize: AdSize,
        loadAds: Boolean
    ) {
        var i = 0;
        while (i <= listData.size) {
            val adView = AdView(context)
            adView.adSize = bannerSize
            adView.adUnitId = bannerAdUnitId
            listData.add(i, adView)
            i += totalItemsAfterAd
        }
        if (loadAds)
            loadBannerAd(context, 0, listData)
    }

    fun loadBannerAd(context: Context, listData: ArrayList<Any>) {
        loadBannerAd(context, 0, listData)
    }

    private fun loadBannerAd(context: Context, index: Int, listData: ArrayList<Any>) {
        if (index >= listData.size)
            return

        val item = listData[index]

        if (!(item is AdView)) {
            throw ClassCastException("Expected item at index ${index} to be a banner ad")
        }

        val adView: AdView = item
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
                Log.e("AD Failed", "Failed to Load ad: " + p0)
                loadBannerAd(context, index + ITEMS_PER_AD, listData)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                loadBannerAd(context, index + ITEMS_PER_AD, listData)
            }
        }
        (context as Activity).runOnUiThread { adView.loadAd(AdRequest.Builder().build()) }
    }

}