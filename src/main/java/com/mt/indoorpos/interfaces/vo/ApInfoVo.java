package com.mt.indoorpos.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: chenyh08
 * @date: 2021-08-05
 * @vision: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApInfoVo {

    private String ssid;

    private String bssid;

    private Double rssi;
}
