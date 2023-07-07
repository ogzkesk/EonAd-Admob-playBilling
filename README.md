# EonAd - Admob & Play Billing

[![](https://jitpack.io/v/ogzkesk/EonAd.svg)](https://jitpack.io/#ogzkesk/EonAd)

A library simplifies and makes more effectiveness Google Admob Ads & Google Play Billing



## Gradle Setup

* Step 1. Add the JitPack repository to your settings.gradle

```gradle
allprojects {
  repositories {
      ...
      maven { url 'https://jitpack.io' }
  }
}
```

* Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.github.ogzkesk:EonAd:<version>'
}
```




## ADMOB

* Describe your Admob App-ID in manifest.xml

```xml
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="{your_app_id}" />
```


  
### Setup

* Initialize in Application.kt

```kotlin
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        EonAd.getInstance().init(this)
    }
}
```



### Resume Ads

* You can just set Resume Ads ( App Open Ads ) in Application onCreate

```kotlin
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        val eonAd = EonAd.getInstance()
        eonAd.init(this)
        eonAd.setResumeAd("ad_unit_id")
    }
}
```

* Disable for a certain time use disableResumeAd() will be disable ads until you enabled again.

```kotlin
    EonAd.getInstance().disableResumeAds()
    // also
    EonAd.getInstance().enableResumeAds()
```

* Disable for one-time event use disableResumeAdsOnClickEvent() will be disable ads only once.

```kotlin
    EonAd.getInstance().disableResumeAdsOnClickEvent()
```



### Interstitial Ads

```kotlin
    EonAd.getInstance().loadInterstitialAd(context,"ad_unit_id")
```

*  If you want listener, use EonAdCallback to get ad state loading, closed, loaded etc.

```kotlin
    EonAd.getInstance().loadInterstitialAd(context,"ad_unit_id",object: EonAdCallback{
        override fun onInterstitialAdLoaded(ad: EonInterstitialAd) {
            super.onInterstitialAdLoaded(ad)
            
        }
    })
```



* Use loadInterstitialAdWithInterval() to disable Interstitial Ads for a given time
* This disables reloading of ads for a certain period of time.
* E.g. in this code Interstitial Ad won't show before 30 second done :

```kotlin
    EonAd.getInstance().loadInterstitialAdWithInterval(context,"ad_unit_id",30_000)
```

<img src="[https://github.com/ogzkesk/EonAd/assets/100413782/a5c539a3-b4ef-4b83-a884-93c99493aa9d]" width="270" align="left">
<img src="[https://github.com/ogzkesk/EonAd/assets/100413782/a5c539a3-b4ef-4b83-a884-93c99493aa9d]" width="270" align="right">



### Rewarded Ads

* Same usage as Interstitial Ads

```kotlin
    EonAd.getInstance().loadRewardedAd(context,"ad_unit_id")
```

* You need to use Callback to get the time the user has earned reward.

```kotlin
    EonAd.getInstance().loadRewardedAd(context,"ad_unit_id",object : EonAdCallback{
        override fun onRewardedAdLoaded(ad: EonRewardedAd) {
            super.onRewardedAdLoaded(ad)
        }

        override fun onRewardEarned(rewardItem: EonRewardedAdItem) {
            super.onRewardEarned(rewardItem)
            // Handle Rewarded item 
        }
    })
```



### Native Ads


```kotlin
    EonAd.getInstance().loadNativeAd(context,"ad_unit_id"){ nativeAd ->
        // populate nativeAd to layout that you created for ad.
    }
```

Note : Use AdCallback for to get other states of ad. ( loading etc. )



#### Native Ad Templates

* Also you can just use ready templates and add your own view easily .
* Note : All Templates Using Shimmer Loading Effect so you dont need to care about loading state.



##### Usage For Xml

```kotlin
    EonAd.getInstance().loadNativeAdTemplate(
        context = context,
        adUnitId = "ad_unit_id",
        type = NativeAdTemplate.LARGE, // you can set SMALL, MEDIUM, MEDIUM_2, LARGE templates.
        onFailedToLoad = {
            // handle if failed
        },
        onNativeAdLoaded = { view ->
            // create a frame layout in your xml and populate in it.
            binding.myView.removeAllViews()
            binding.myView.addView(view)
        }
    )
```



##### Usage For Jetpack Compose


* Give nativeAdView that you get from loadNativeAdTemplate() function.
* Note : Do not run loadNativeAdTemplate() in @Composable functions to avoid multiple loads.

```kotlin
    EonNativeAdView(
        nativeAdView = nativeAdView,
        darkModeSupport = true
    )
```



### Banner Ads


* Similar native ad templates just load banner ads and populate view to your xml layout.
* You have to use Callback if you need ad's other states.


##### Usage For Xml


```kotlin
    val bannerAdView = EonAd.getInstance().loadBannerAd(context,"ad_unit_id",BannerAdSize.BANNER) // select different banner types that admob provides
    binding.myView.removeAllViews()
    binding.myView.addView(bannerAdView)
```


##### Usage For Jetpack Compose


* Note : Do not run loadBannerAd() in @Composable functions to avoid multiple loads.
  
```kotlin
    EonBannerView(view = bannerView)
```



## Play Billing


### Setup


* Describe permissions in your Manifest.xml

```xml
    <uses-permission android:name="com.android.vending.BILLING"/>
    <uses-permission android:name="android.permission.INTERNET"/>
```


* Initialize in Application.kt

```kotlin
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        val products = listOf(
            PurchaseItem("your_sub_id_1",PurchaseItem.TYPE_SUBSCRIPTION),
            PurchaseItem("your_product_id_1",PurchaseItem.TYPE_PRODUCT),
        )
        Iap.getInstance().init(this,products).connect()
    }
}
```


* NOTE : Billing will connect in 1-2 second. If you want to do something on at app starting you need to be sure CONNECTED .
* You can use with listener if you need to :

```kotlin
    Iap.getInstance().init(this,products).connect { connected, disconnected ->
        if(connected){
            // do something..
        }
    }
}
```

* If you can't, You can connect later on anywhere in app. Just .init(application) enough in Application() class.
* But if you dont connect after you cannot use billing library.

* E.g. if you want to check subscription at app start :
  
```kotlin
    val iap = Iap.getInstance()
    iap.connect{ connected, disconnected ->
        if(connected){
            iap.checkSubscription { isAvailable -> 
                if(isAvailable){
                    // user has subscription
                }
            }
        }
    }
```



#### Products & Subscriptions


* For fetching products & subscription that you described :

```kotlin
    val iap = Iap.getInstance()
    val subs = iap.getSubscriptions()
    val products = iap.getInAppProducts()
```



#### Subscribe & Purchase


* listen() suspend function that needs coroutine scope 
* Use subscribe() for start subscription billing :

```kotlin
    someCoroutineScope.launch() {
        iap.subscribe(activity,"subscription_id")
            .listen { result ->
                if(result.purchases.isNotEmpty()){
                    // user subscribed successfully
                }                
            }
        
    }
```


* listen() suspend function that needs coroutine scope
* Use purchase() for start inapp product billing :

```kotlin
    someCoroutineScope.launch() {
        iap.purchase(activity,"product_id")
            .listen { result ->
                if(result.purchases.isNotEmpty()){
                    // user bought the product successfully
                }                
            }
        
    }
```



#### Consume


* If you used disableAutoConsume() you need to consume() item later on otherwise user can't buy that inapp product again.

```kotlin
    Iap.getInstance().disableAutoConsume()
```


* In order for the user to purchase the product again :

```kotlin
    Iap.getInstance().consume(PurchaseIap){ error -> 
        if(error == null){
            // check error
        }
    }
```

Note : PurchaseIap you gonna get from listen() function when user buy any inapp product 



#### State


* Check connected state ( this is not a listener )

```kotlin
    Iap.getInstance().isConnected()
```


* If you won't use billing after a while use :
* Note : If released billing won't work until you .connect() again.

```kotlin
    Iap.getInstance().release()
```


