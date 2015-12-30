# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/lukasz/android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.exceptions.**
-dontwarn io.realm.**

# javax
-dontwarn javax.**
-dontwarn android.support.design.widget.**

# retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# okhttp
-dontwarn okio.**

# android.design.library
-keep class android.support.v7.widget.** { *; }
-keep interface android.support.v7.widget.** { *; }
-dontwarn android.support.v7.**
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

## google plus login gms
-keep class com.google.android.gms.common.** { *; }

-dontoptimize
-dontobfuscate
-keep class com.squareup.okhttp.** { *; }

-dontoptimize
-dontobfuscate
-keep class com.squareup.picasso.OkHttpDownloader.** { *; }

-dontoptimize
-dontobfuscate
-keep interface com.squareup.picasso.OkHttpDownloader.** { *; }


-dontoptimize
-dontobfuscate
-dontwarn com.squareup.picasso.OkHttpDownloader.**

-dontoptimize
-dontobfuscate
-dontwarn com.trnql.zen.utlis.LocationUtils.**

-dontoptimize
-dontobfuscate
-keep class com.trnql.zen.utlis.LocationUtils.** { *; }

-dontoptimize
-dontobfuscate
-keep class com.trnql.zen.utlis.SoftHashMap.** { *; }

-dontwarn rx.internal.util.unsafe.**


-dontoptimize
-dontobfuscate
-keep interface com.google.android.gms.common.** { *; }

-dontoptimize
-dontobfuscate
-dontwarn com.google.android.gms.common.**


-dontoptimize
-dontobfuscate
-keep class com.google.android.gms.maps.** { *; }

-dontoptimize
-dontobfuscate
-keep interface com.google.android.gms.maps.** { *; }
#-keep class com.google.android.gms.plus.** { *; }
#-keep interface com.google.android.gms.plus.** { *; }
#-dontwarn com.google.android.gms.plus.**

# app module
-dontwarn pl.lukmarr.pokemontrainer.**

# butterknife
-dontwarn java.beans.ConstructorProperties
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-assumenosideeffects class com.trnql.zen.utlis.AndroidUtils {
    public static void log(...);
}