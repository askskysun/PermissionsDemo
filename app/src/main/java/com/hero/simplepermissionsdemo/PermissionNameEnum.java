package com.hero.simplepermissionsdemo;

import android.Manifest;

/**
 * <pre>
 * 权限名称对应枚举
 * </pre>
 * Author by sun, Email 1910713921@qq.com, Date on 2021/11/10.
 */

public enum PermissionNameEnum {
    /**
     * 麦克风
     */
    PERMISSION_RECORD_AUDIO(Manifest.permission.RECORD_AUDIO, "麦克风"),
    /**
     * 允许应用访问大概的位置
     */
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION, "定位"),
    /**
     * 允许应用访问精确的位置
     */
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, "定位"),
    /**
     * 应用程序访问有关网络的信息
     */
    ACCESS_NETWORK_STATE(Manifest.permission.ACCESS_NETWORK_STATE, "应用程序访问有关网络的信息"),
    /**
     * 访问有关Wi-Fi网络的信息
     */
    ACCESS_WIFI_STATE(Manifest.permission.ACCESS_WIFI_STATE, "访问有关Wi-Fi网络的信息"),
    /**
     * 应用收集电池统计信息
     */
    BATTERY_STATS(Manifest.permission.BATTERY_STATS, "应用收集电池统计信息"),
    /**
     * 应用连接到配对的蓝牙设备
     */
    BLUETOOTH(Manifest.permission.BLUETOOTH, "应用连接到配对的蓝牙设备"),
    /**
     * 启动电话通话
     */
    CALL_PHONE(Manifest.permission.CALL_PHONE, "启动电话通话"),
    /**
     * 应用程式拨打任何电话号码
     */
    CALL_PRIVILEGED(Manifest.permission.CALL_PRIVILEGED, "拨打电话"),
    /**
     * 摄像头
     */
    CAMERA(Manifest.permission.CAMERA, "摄像头"),
    /**
     * 捕获音频输出
     */
    CAPTURE_AUDIO_OUTPUT(Manifest.permission.CAPTURE_AUDIO_OUTPUT, "捕获音频输出"),
    /**
     * 更改是否启用应用程序组件
     */
    CHANGE_COMPONENT_ENABLED_STATE(Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE, "更改是否启用应用程序组件"),
    /**
     * 修改当前配置
     */
    CHANGE_CONFIGURATION(Manifest.permission.CHANGE_CONFIGURATION, "修改当前配置"),
    /**
     * 更改网络连接状态
     */
    CHANGE_NETWORK_STATE(Manifest.permission.CHANGE_NETWORK_STATE, "更改网络连接状态"),
    /**
     * 进入Wi-Fi多播模式
     */
    CHANGE_WIFI_MULTICAST_STATE(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE, "进入Wi-Fi多播模式"),
    /**
     * 更改Wi-Fi连接状态
     */
    CHANGE_WIFI_STATE(Manifest.permission.CHANGE_WIFI_STATE, "更改Wi-Fi连接状态"),
    /**
     * 清除设备上所有已安装应用程序的缓存
     */
    CLEAR_APP_CACHE(Manifest.permission.CLEAR_APP_CACHE, "清除设备上所有已安装应用程序的缓存"),
    /**
     * 删除缓存文件
     */
    DELETE_CACHE_FILES(Manifest.permission.DELETE_CACHE_FILES, "删除缓存文件"),
    /**
     * 删除软件包
     */
    DELETE_PACKAGES(Manifest.permission.DELETE_PACKAGES, "删除软件包"),
    /**
     * 在不安全的情况下禁用键盘锁
     */
    DISABLE_KEYGUARD(Manifest.permission.DISABLE_KEYGUARD, "在不安全的情况下禁用键盘锁"),
    /**
     * 安装软件包
     */
    INSTALL_PACKAGES(Manifest.permission.INSTALL_PACKAGES, "安装软件包"),

    /**
     * 读取用户的日历数据
     */
    READ_CALENDAR(Manifest.permission.READ_CALENDAR, "日历"),
    /**
     * 写入用户的日历数据
     */
    WRITE_CALENDAR(Manifest.permission.WRITE_CALENDAR, "日历"),
    /**
     * 发送短信
     */
    SEND_SMS(Manifest.permission.SEND_SMS, "发送短信"),
    /**
     * 从外部存储读取
     */
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, "存储"),
    /**
     * 写入外部存储
     */
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储"),
    /**
     * 读取用户的联系人数据
     */
    READ_CONTACTS(Manifest.permission.READ_CONTACTS, "联系人"),
    /**
     * 写入用户的联系人数据
     */
    WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS, "联系人"),
    /**
     * 访问电话状态
     */
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE, "访问电话状态"),
    /**
     * 写入（但不读取）用户的通话记录数据
     */
    WRITE_CALL_LOG(Manifest.permission.WRITE_CALL_LOG, "写入用户的通话记录数据"),
    /**
     * 接收短讯
     */
    RECEIVE_SMS(Manifest.permission.RECEIVE_SMS, "接收短讯"),
    /**
     * 读取使用者的通话记录
     */
    READ_CALL_LOG(Manifest.permission.READ_CALL_LOG, "读取使用者的通话记录");

    private String permission;
    private String descr;

    PermissionNameEnum(String permission, String descr) {
        this.permission = permission;
        this.descr = descr;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * 获取权限中文名
     *
     * @param permission
     * @return
     */
    public static String getPermissionName(String permission) {
        PermissionNameEnum[] values = PermissionNameEnum.values();
        for (PermissionNameEnum one : values) {
            if (one.getPermission().equals(permission)) {
                return one.getDescr();
            }
        }
        return "";
    }
}


