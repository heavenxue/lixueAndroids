package com.lixueandroid.domain;

import java.util.HashMap;

/**
 * Gatt的属性
 * @author lixue
 *
 */
public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";//心率测量
//    public static String HEART_RATE_MEASUREMENT = "d3e60004-8f36-40d6-b2d5-c5d9f5e81869";//心率测量
//    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";//客户端属性配置
    public static String CLIENT_CHARACTERISTIC_CONFIG = "d3e60000-8f36-40d6-b2d5-c5d9f5e81869";//客户端属性配置
    public static String CLIENT_CHARACTERISTIC_CONFIG2 = "d3e60004-8f36-40d6-b2d5-c5d9f5e81869";//客户端属性配置
 
    static {
        // Sample Services.
//        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
//        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
////        // Sample Characteristics.
//        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
//        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }
 
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}