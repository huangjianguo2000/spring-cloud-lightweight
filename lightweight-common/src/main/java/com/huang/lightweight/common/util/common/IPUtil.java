package com.huang.lightweight.common.util.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * 获取本机IP 地址
 */
public class IPUtil {


    public static String getLocalIp() {
        return getLocalIp4Address().iterator().next();
    }

    public static Set<String> getLocalIp4Address() {
        Set<String> result = new HashSet<>();
        List<Inet4Address> list = null;
        try {
            list = getLocalIp4AddressFromNetworkInterface();
            list.forEach(d -> {
                result.add(d.toString().replaceAll("/", ""));
            });
        } catch (Exception e) {
        }
        return result;
    }
 
    /*
     * 获取本机所有网卡信息   得到所有IPv4信息
     * @return Inet4Address>
     */
    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(8);
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces == null) {
            return addresses;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    addresses.add((Inet4Address) inetAddress);
                }
            }
        }
        return addresses;
    }
 
}