package com.adsmanager.unityAdsModule

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.RelativeLayout
import com.adsmanager.core.CallbackAds
import com.adsmanager.core.SizeBanner
import com.adsmanager.core.SizeNative
import com.adsmanager.core.iadsmanager.IAds
import com.adsmanager.core.iadsmanager.IInitialize
import com.adsmanager.core.rewards.IRewards
import com.adsmanager.core.rewards.RewardsItem
import com.unity3d.ads.*
import com.unity3d.ads.UnityAds.*
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.BannerView.IListener
import com.unity3d.services.banners.UnityBannerSize


class UnityAdsModule : IAds {


    override fun setTestDevices(activity: Activity, testDevices: List<String>) {

    }

    override fun initialize(context: Context, appId: String?, iInitialize: IInitialize?) {
        UnityAds.initialize(context, appId, false, object : IUnityAdsInitializationListener {
            override fun onInitializationComplete() {

            }

            override fun onInitializationFailed(
                error: UnityAds.UnityAdsInitializationError?,
                message: String?
            ) {

            }

        });
        iInitialize?.onInitializationComplete()
    }


    override fun loadGdpr(activity: Activity, childDirected: Boolean) {

    }

    override fun showBanner(
        activity: Activity,
        bannerView: RelativeLayout,
        sizeBanner: SizeBanner,
        adUnitId: String,
        callbackAds: CallbackAds?
    ) {
        val unityAdsBanner = BannerView(activity, adUnitId, UnityBannerSize(320, 50))
        // Set the listener for banner lifecycle events:
        // Set the listener for banner lifecycle events:
        unityAdsBanner.listener = bannerListener(callbackAds)
        // Request a banner ad:
        unityAdsBanner.load();
        // Associate the banner view object with the banner view:
        bannerView.removeAllViews()
        bannerView.addView(unityAdsBanner);
    }

    // Listener for banner events:
    private fun bannerListener (callbackAds: CallbackAds?) = object : IListener {
        override fun onBannerLoaded(bannerAdView: BannerView) {
            // Called when the banner is loaded.
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.placementId)
            callbackAds?.onAdLoaded()
        }

        override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
            callbackAds?.onAdFailedToLoad("Unity Ads failed to load banner for " + bannerAdView.placementId + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage)
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load banner for " + bannerAdView.placementId + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage
            )
            // Note that the BannerErrorInfo object can indicate a no fill (see API documentation).
        }

        override fun onBannerClick(bannerAdView: BannerView) {
            // Called when a banner is clicked.
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.placementId)
        }

        override fun onBannerLeftApplication(bannerAdView: BannerView) {
            // Called when the banner links out of the application.
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.placementId)
        }
    }

    private val loadInterstitialListener: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {

        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAdsLoadError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load ad for $placementId with error: [$error] $message"
            )
        }
    }

    private fun showInterstitialListener(callbackAds: CallbackAds?): IUnityAdsShowListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(
            placementId: String,
            error: UnityAdsShowError,
            message: String
        ) {
            callbackAds?.onAdFailedToLoad("Unity Ads failed to show ad for $placementId with error: [$error] $message")
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to show ad for $placementId with error: [$error] $message"
            )
        }

        override fun onUnityAdsShowStart(placementId: String) {
            callbackAds?.onAdLoaded()
            Log.v("UnityAdsExample", "onUnityAdsShowStart: $placementId")
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
        }

        override fun onUnityAdsShowComplete(
            placementId: String,
            state: UnityAdsShowCompletionState
        ) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: $placementId")
        }
    }

    override fun loadInterstitial(activity: Activity, adUnitId: String) {
        load(adUnitId, loadInterstitialListener);
    }

    override fun showInterstitial(activity: Activity, adUnitId: String, callbackAds: CallbackAds?) {
        show(
            activity,
            adUnitId,
            UnityAdsShowOptions(),
            showInterstitialListener(callbackAds)
        )
    }


    override fun showNativeAds(
        activity: Activity,
        nativeView: RelativeLayout,
        sizeNative: SizeNative,
        adUnitId: String,
        callbackAds: CallbackAds?
    ) {
        callbackAds?.onAdFailedToLoad("UnityAds, native not available")
    }

    private val loadRewardsListener: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {

        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAdsLoadError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load ad for $placementId with error: [$error] $message"
            )
        }
    }

    private fun showRewardsListener(callbackAds: CallbackAds?, iRewards: IRewards?): IUnityAdsShowListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(
            placementId: String,
            error: UnityAdsShowError,
            message: String
        ) {
            callbackAds?.onAdFailedToLoad("Unity Ads failed to show ad for $placementId with error: [$error] $message")
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to show ad for $placementId with error: [$error] $message"
            )
        }

        override fun onUnityAdsShowStart(placementId: String) {
            callbackAds?.onAdLoaded()
            Log.v("UnityAdsExample", "onUnityAdsShowStart: $placementId")
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
        }

        override fun onUnityAdsShowComplete(
            placementId: String,
            state: UnityAdsShowCompletionState
        ) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: $placementId")
            if (state == UnityAdsShowCompletionState.COMPLETED) {
                // Reward the user for watching the ad to completion
                iRewards?.onUserEarnedReward(RewardsItem(1, ""))
            } else {
                // Do not reward the user for skipping the ad
            }
        }
    }


    override fun loadRewards(activity: Activity, adUnitId: String) {
        load(adUnitId, loadRewardsListener);
    }

    override fun showRewards(
        activity: Activity,
        adUnitId: String,
        callbackAds: CallbackAds?,
        iRewards: IRewards?
    ) {
        show(
            activity,
            adUnitId,
            UnityAdsShowOptions(),
            showRewardsListener(callbackAds, iRewards)
        )
    }

}