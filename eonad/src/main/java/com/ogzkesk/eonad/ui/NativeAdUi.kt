package com.ogzkesk.eonad.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ogzkesk.eonad.EonNativeAd
import com.ogzkesk.eonad.NativeAdTemplateType
import com.ogzkesk.eonad.R
import org.xmlpull.v1.XmlPullParser

internal class NativeAdUi(private val nativeAd: NativeAd?) {

    private fun populateSmallNativeView(context: Context): View {
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_small,
            null
        ) as FrameLayout

        return NativeAdView(context).apply {
            nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
                    R.layout.admob_native_small,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val body = adView.findViewById<TextView>(R.id.ad_body)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)

                headLine.text = ad.headline
                body.text = ad.body
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }

                headlineView = headLine
                bodyView = body
                iconView = icon
                callToActionView = callToAction

                removeAllViews()
                setNativeAd(ad)
                addView(adView)
            } ?: addView(loadingView)
        }
    }

    private fun populateMediumNativeView(context: Context): View {
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_medium,
            null
        ) as FrameLayout

        return NativeAdView(context).apply {
            nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
                    R.layout.admob_native_medium,
                    null
                ) as FrameLayout

                val headLine = adView.findViewById<TextView>(R.id.ad_headline)
                val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                val icon = adView.findViewById<ImageView>(R.id.ad_icon)
                val callToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
                val adImage = adView.findViewById<FrameLayout>(R.id.ad_view_container)
                    .findViewById<ImageView>(R.id.ad_image)

                headLine.text = ad.headline
                advertiser.text = ad.advertiser
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if (ad.images.isNotEmpty()) {
                    println("Eon --- Images Not emty size :: -- > ${ad.images.size}")
                    ad.images[0].drawable?.let {
                        adImage.setImageDrawable(it)
                        println("Eon ---- Image drawable --- > $it")
                    }
                    ad.images[0].uri?.let {
                        println("Eon --- Image Uri :: ${it.toString()}")
                    }
                }

                headlineView = headLine
                advertiserView = advertiser
                iconView = icon
                callToActionView = callToAction
                imageView = adImage

                removeAllViews()
                setNativeAd(ad)
                addView(adView)
            } ?: addView(loadingView)
        }
    }

    private fun populateLargeNativeView(context: Context): View {
        val loadingView = (context as Activity).layoutInflater.inflate(
            R.layout.admob_native_large,
            null
        ) as FrameLayout

        return NativeAdView(context).apply {
            nativeAd?.let { ad ->

                val adView = (context as Activity).layoutInflater.inflate(
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

                headLine.text = ad.headline
                advertiser.text = ad.advertiser
                body.text = ad.body
                callToAction.text = ad.callToAction
                ad.icon?.drawable?.let { draw -> icon.setImageDrawable(draw) }
                if (ad.images.isNotEmpty()) {
                    ad.images[0].drawable?.let { adImage.setImageDrawable(it) }
                }

                headlineView = headLine
                advertiserView = advertiser
                bodyView = body
                iconView = icon
                imageView = adImage
                callToActionView = callToAction

                removeAllViews()
                addView(adView)
                setNativeAd(ad)
            } ?: addView(loadingView)
        }
    }

    fun getNativeUi(context: Context, type: NativeAdTemplateType): View {
        return when (type) {
            NativeAdTemplateType.SMALL -> populateSmallNativeView(context).apply { show(this) }
            NativeAdTemplateType.MEDIUM -> populateMediumNativeView(context).apply { show(this) }
            NativeAdTemplateType.LARGE -> populateLargeNativeView(context).apply { show(this) }
        }
    }

    private fun show(view: View) {
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val adView = view.findViewById<FrameLayout>(R.id.ad_view_container)
        shimmer.isVisible = nativeAd == null
        adView.isVisible = nativeAd != null
    }
}