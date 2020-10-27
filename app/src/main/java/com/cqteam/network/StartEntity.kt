package com.cqteam.network

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/8/14 16:06
 */
data class StartEntity(
    val device: DeviceEntity,
    val enterprise: EnterpriseEntity,
    val configs: ConfigsEntity,
    val start_use_notice: String = "",
    val userInfo: UserInfo?,
    val contact_mobile: String
)
data class UserList(
    val userList : List<UserInfo>?
)
data class SwitchUserEntity(
    val userInfo : UserInfo
)
data class UserInfo(
    val id : String = "",
    val nickname : String = "",
    val mobile : String = "",
    val api_token : String = "",
    val status : String = "",
    val birthday : String = "",
    val sex : String = "",
    val is_login : Boolean? = null
)
data class ConfigsEntity(
    val static_res_domain: String = "",
    val drawing_map: String?
)
data class EnterpriseEntity(
    val address: String,
    val district_text: String,
    val hotelbox_name: String,
    val id: Int,
    val name: String,
    val unique_key: String,
    val logo_image: String,
    val screen_off_time: Int,
    val ad_appear_time: Int
)
data class DeviceEntity(
    val auth_mobile: String,
    val device_no: String,
    val enterprise_title: String,
    val id: Int,
    val room_no: String,
    val room_phone: String,
    val upgrade_status: String
)