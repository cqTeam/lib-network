package com.cqteam.network

/**
 * Author： 洪亮
 * Time： 2020/3/30 - 10:24 AM
 * Email：281332545@qq.com
 * <p>
 * 描述：
 */
data class WeatherEntity(
        val forecast: Forecast,
        val now: Now
)

data class Forecast(
        val basic: ForecastBasic,
        val daily_forecast: List<DailyForecast>,
        val status: String,
        val update: ForecastUpdate
)

data class ForecastBasic(
        val admin_area: String,
        val cid: String,
        val cnty: String,
        val lat: String,
        val location: String,
        val lon: String,
        val parent_city: String,
        val tz: String
)

data class DailyForecast(
        val cond_code_d: String,
        val cond_code_n: String,
        val cond_txt_d: String,
        val cond_txt_n: String,
        val d_icon: String,
        val date: String,
        val hum: String,
        val mr: String,
        val ms: String,
        val n_icon: String,
        val pcpn: String,
        val pop: String,
        val pres: String,
        val sr: String,
        val ss: String,
        val tmp_max: String,
        val tmp_min: String,
        val uv_index: String,
        val vis: String,
        val wind_deg: String,
        val wind_dir: String,
        val wind_sc: String,
        val wind_spd: String
)

data class ForecastUpdate(
        val loc: String,
        val utc: String
)

data class Now(
        val basic: NowBasic,
        val now: NowDetail,
        val status: String,
        val update: NowUpdate
)

data class NowBasic(
        val admin_area: String,
        val cid: String,
        val cnty: String,
        val lat: String,
        val location: String,
        val lon: String,
        val parent_city: String,
        val tz: String
)

data class NowDetail(
        val cloud: String,
        val cond_code: String,
        val cond_txt: String,
        val fl: String,
        val hum: String,
        val icon: String,
        val pcpn: String,
        val pres: String,
        val tmp: String,
        val vis: String,
        val wind_deg: String,
        val wind_dir: String,
        val wind_sc: String,
        val wind_spd: String
)

data class NowUpdate(
        val loc: String,
        val utc: String
)