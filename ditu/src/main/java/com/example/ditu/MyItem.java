package com.example.ditu;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

import mapapi.clusterutil.clustering.ClusterItem;

//ClusterItem接口的实现类
public class MyItem implements ClusterItem {
    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    private final LatLng mPosition;

    public MyItem(LatLng position) {
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark);
    }
}
