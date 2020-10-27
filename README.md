# lib-network
基于Kotlin利用Coroutines+Okhttp3+Retrofit的网络框架
## 技术点
* Coroutines
* Okhttp3
* Retrofit
## 集成
### last-version:
[![](https://jitpack.io/v/cqTeam/lib-network.svg)](https://jitpack.io/#cqTeam/lib-network)
* Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
    	...
        maven { url 'https://jitpack.io' }
    }
}
```
* Add the dependency to module build.gradle:
```
dependencies {
    implementation 'com.github.cqTeam:lib-network:[last-version]'
}
```
* Add the networkSecurityConfig to module AndroidManifest
```
<application
        ...
        android:networkSecurityConfig="@xml/network_http">
```
* Add the Permissions to module AndroidManifest

```
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
## 使用
* Init in your Application 

```
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetWorkManager.init(this,"http://www.***.com/")
    }
}
```
* [使用详情](https://github.com/cqTeam/lib-network/wiki/%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)

### END



