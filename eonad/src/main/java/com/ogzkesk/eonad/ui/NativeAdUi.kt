package com.ogzkesk.eonad.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ogzkesk.eonad.NativeAdTemplate
import com.ogzkesk.eonad.R

internal class NativeAdUi(private val nativeAd: NativeAd?) {

    private fun populateSmallNativeView(context: Context): View {

        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_small,
            null
        ) as FrameLayout

        return if(nativeAd != null){
            NativeAdView(context).apply {
                val adView = (context).layoutInflater.inflate(
                    R.layout.admob_native_small,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val body = adView.findViewById<TextView>(R.id.ad_body)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)

                headLine.text = nativeAd.headline
                body.text = nativeAd.body
                callToAction.text = nativeAd.callToAction
                nativeAd.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }

                headlineView = headLine
                bodyView = body
                iconView = icon
                callToActionView = callToAction

                removeAllViews()
                setNativeAd(nativeAd)
                addView(adView)
            }
        } else {
            loadingView
        }
    }

    private fun populateMediumNativeView(context: Context): View {

        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_medium,
            null
        ) as FrameLayout

        return if(nativeAd != null) {
            NativeAdView(context).apply {
                val adView = (context).layoutInflater.inflate(
                    R.layout.admob_native_medium,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
                val adImage = adView.findViewById<FrameLayout>(R.id.ad_view_container)
                    .findViewById<ImageView>(R.id.ad_image)

                headLine.text = nativeAd.headline
                advertiser.text = nativeAd.advertiser
                callToAction.text = nativeAd.callToAction
                nativeAd.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if (nativeAd.images.isNotEmpty()) {
                    nativeAd.images[0].drawable?.let { adImage.setImageDrawable(it) }
                }

                headlineView = headLine
                advertiserView = advertiser
                iconView = icon
                callToActionView = callToAction
                imageView = adImage

                removeAllViews()
                setNativeAd(nativeAd)
                addView(adView)
            }
        } else {
            loadingView
        }
    }

    private fun populateLargeNativeView(context: Context): View {
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_large,
            null
        ) as FrameLayout

        return if (nativeAd != null) {
            NativeAdView(context).apply {
                val adView = (context).layoutInflater.inflate(
                    R.layout.admob_native_large,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                val body = adView.findViewById<TextView>(R.id.ad_body)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
                val adImage = adView.findViewById<FrameLayout>(R.id.ad_view_container)
                    .findViewById<ImageView>(R.id.ad_image)

                headLine.text = nativeAd.headline
                advertiser.text = nativeAd.advertiser
                body.text = nativeAd.body
                callToAction.text = nativeAd.callToAction
                nativeAd.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if (nativeAd.images.isNotEmpty()) {
                    nativeAd.images[0].drawable?.let { adImage.setImageDrawable(it) }
                }

                headlineView = headLine
                advertiserView = advertiser
                bodyView = body
                iconView = icon
                imageView = adImage
                callToActionView = callToAction

                removeAllViews()
                addView(adView)
                setNativeAd(nativeAd)
            }
        } else {
            loadingView
        }
    }

    fun getNativeUi(context: Context, type: NativeAdTemplate): View {
        return when (type) {
            NativeAdTemplate.SMALL -> populateSmallNativeView(context).apply { show(this) }
            NativeAdTemplate.MEDIUM -> populateMediumNativeView(context).apply { show(this) }
            NativeAdTemplate.LARGE -> populateLargeNativeView(context).apply { show(this) }
        }
    }

    private fun show(view: View) {
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val adView = view.findViewById<FrameLayout>(R.id.ad_view_container)
        shimmer.isVisible = nativeAd == null
        adView.isVisible = nativeAd != null
    }
}