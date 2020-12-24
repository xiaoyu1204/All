package com.example.ditu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import com.example.ditu.adapter.MapTypeAdapter;
import com.example.ditu.adapter.SearchItemAdapter;
import com.example.ditu.adapter.SuggestAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import mapapi.clusterutil.clustering.ClusterManager;
import mapapi.overlayutil.WalkingRouteOverlay;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ConstraintLayout mCly;
    private NavigationView mNv;
    private DrawerLayout mDl;

    private MapView mMapView;//地图控件

    private EditText input;//检索搜索框
    private Button btn_search;//检索按钮
    private RecyclerView mRlv;
    private EditText input_start;//路径起点
    private EditText input_end;//路径终点
    private Button btn_routePlan;//路径按钮

    //百度地图的数据操作
    BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDrawer();

        //当android系统小于5.0的时候直接定位显示，不用动态申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            initMap();//百度地图的数据操作
            //initMapType();//切换地图类型
            initPoi();//检索
            //初始化路径规划
            //initRoutePlan();
            //initPolymerization();//点聚合
            initNav();//侧滑+行政区

            // 获取传感器管理服务
            // mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        } else {
//            MainActivityPermissionsDispatcher.ApplySuccessWithCheck(this);
        }

    }

    private void initView() {
        input = (EditText) findViewById(R.id.et_input);
        btn_search = (Button) findViewById(R.id.btn_search);
        mRlv = (RecyclerView) findViewById(R.id.recyclerView);
        mCly = (ConstraintLayout) findViewById(R.id.mCly);
        mNv = (NavigationView) findViewById(R.id.mNv);
        mDl = (DrawerLayout) findViewById(R.id.mDl);
        //获取地图控件的引用
        mMapView = (MapView) findViewById(R.id.mMapView);
        btn_search.setOnClickListener(this);
        input_start = (EditText) findViewById(R.id.input_start);
        input_start.setOnClickListener(this);
        input_end = (EditText) findViewById(R.id.input_end);
        input_end.setOnClickListener(this);
        btn_routePlan = (Button) findViewById(R.id.btn_routePlan);
        btn_routePlan.setOnClickListener(this);
    }

    private void initDrawer() {
        mDl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //让主页面和菜单一起向右滑动
                mCly.setX(mNv.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_search:
                search();//点击搜索进行搜索
                break;
            case R.id.btn_routePlan:
                searchRoute();//点击起点和终点进行搜索
                break;
        }
    }

    /**
     * TODO*************************动态权限*************************
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //这句代码里的类和方法是没有的，它是通过对Activity或者Fragment@RuntimePermissions注解后
        // 并且进行Make project后才出现的
//        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功时
     */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void ApplySuccess() {
        initMap();//百度地图的数据操作
        //initMapType();//切换地图类型
        initPoi();//检索
        //initRoutePlan();//初始化路径规划
        //initPolymerization();//点聚合
        initNav();//侧滑+行政区
    }

    /**
     * 申请权限告诉用户原因时
     */
    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForMap(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开定位的权限", request);
    }

    //申请权限被拒绝时
    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapDenied() {
        Toast.makeText(MainActivity.this, "你拒绝了权限，该功能不可用", Toast.LENGTH_LONG).show();
    }

    /**
     * 申请权限被拒绝并勾选不再提醒时
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapNeverAskAgain() {
        AskForPermission();
    }

    /**
     * 告知用户具体需要权限的原因
     *
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("当前应用缺少定位权限,请去设置界面打开,打开之后按两次返回键可回到该应用哦");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    /**
     * TODO*************************初始化地图数据+定位************************
     */
    private void initMap() {
        baiduMap = mMapView.getMap();
        //定位初始化 声明LocationClient类
        locationClient = new LocationClient(this);
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);
        //设置为普通类型的地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，
        // 将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
        //baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        //开启交通图
        // baiduMap.setTrafficEnabled(true);
        //关闭缩放按钮
        mMapView.showZoomControls(false);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        initLocation();//初始化定位
        //开启地图定位图层
        locationClient.start();
    }

    /**
     * TODO*************************初始化地图数据+定位*************************
     */
    //百度地图定位的类
    LocationClient locationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;

    //初始化定位
    private void initLocation() {
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        // 打开Gps
        option.setOpenGps(true);
        // 设置坐标类型 MyApp里面默认的坐标类型
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(5000);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //设置locationClientOption
        locationClient.setLocOption(option);
    }

    //地图定位的监听
    // BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口听
    //实现定位监听 位置一旦有所改变就会调用这个方法
    //可以在这个方法里面获取到定位之后获取到的一系列数据
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }
            //获取定位结果
            bdLocation.getTime();    //获取定位时间
            bdLocation.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            bdLocation.getLocType();    //获取定位类型
            bdLocation.getRadius();    //获取定位精准度
            bdLocation.getAddrStr();    //获取地址信息
            bdLocation.getCountry();    //获取国家信息
            bdLocation.getCountryCode();    //获取国家码
            bdLocation.getCity();    //获取城市信息
            bdLocation.getCityCode();    //获取城市码
            bdLocation.getDistrict();    //获取区县信息
            bdLocation.getStreet();    //获取街道信息
            bdLocation.getStreetNumber();    //获取街道码
            bdLocation.getLocationDescribe();    //获取当前位置描述信息
            bdLocation.getPoiList();    //获取当前位置周边POI信息

            bdLocation.getBuildingID();    //室内精准定位下，获取楼宇ID
            bdLocation.getBuildingName();    //室内精准定位下，获取楼宇名称
            bdLocation.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //经纬度
            bdLocation.getLatitude();//获取纬度信息
            bdLocation.getLongitude();//获取经度信息

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false;
                //设置并显示中心点
                setPosition2Center(baiduMap, bdLocation, true);
            }
        }
    }
    private void setPosition2Center(BaiduMap baiduMap, BDLocation bdLocation, boolean b) {
        //设置中心点和添加marker
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        baiduMap.setMyLocationData(locData);

        if (b) {
            //获得经纬度
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();//进行定位
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }


    /**
     * TODO*************************检索*************************
     */
    SearchItemAdapter searchItemAdapter;
    List<PoiInfo> poiList;
    PoiSearch poiSearch;

    private void initPoi() {
        poiList = new ArrayList<>();//集合
        searchItemAdapter = new SearchItemAdapter(poiList, this);//创建适配器
        mRlv.setLayoutManager(new LinearLayoutManager(this));//布局管理器
        mRlv.setAdapter(searchItemAdapter);//绑定适配器
        mRlv.setVisibility(View.GONE);//隐藏视图

        //创建POI检索实例
        poiSearch = PoiSearch.newInstance();
        //设置检索监听器
        poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);

        searchItemAdapter.setMyClick(new SearchItemAdapter.MyClick() {
            @Override
            public void itemClick(int pos) {
                //点击条目进行定位
                PoiInfo poiInfo = poiList.get(pos);
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(poiInfo.location);//传经纬度

                //设置地图新中心点
                baiduMap.setMapStatus(status);

                //绘制圆  传经纬度
                //drawCircle(poiInfo.location.latitude, poiInfo.location.longitude);
                //绘制点标记
                //addMark(poiInfo.location.latitude,poiInfo.location.longitude);

                //文字覆盖物
                //latlng(poiInfo.location.latitude, poiInfo.location.longitude);

                //添加信息窗
                window(poiInfo.address, poiInfo.location.latitude, poiInfo.location.longitude);

                //调用点聚合
                addMark(poiInfo.location.latitude, poiInfo.location.longitude, poiInfo.getName());
            }
        });
    }

    //搜索 设置PoiCitySearchOption，发起检索请求
    private void search() {
        //获取输入框中要搜索的值
        String word = input.getText().toString();
        if (!TextUtils.isEmpty(word)) {//如果不为空
            // PoiCiySearchOption 设置检索属性
            PoiCitySearchOption option = new PoiCitySearchOption();
            option.city("北京");//city 检索城市  必填
            option.keyword(word);//keyword 检索内容关键字 必填
            poiSearch.searchInCity(option);//赋值
            mRlv.setVisibility(View.VISIBLE);//开启视图
        } else {
            poiList.clear();//把集合清空
            mRlv.setVisibility(View.GONE);//隐藏视图
        }

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isStart = false;
                    mRlv.setVisibility(View.VISIBLE);//显示集合
                }
            }
        });
    }

    //搜索的监听
    OnGetPoiSearchResultListener poiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            Log.i(TAG, "onGetPoiResult");
            poiList.clear();//把集合清空
            //如果 poiResult.getAllPoi不为空 并且长度不为0
            if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                //点聚合
                List<MyItem> list = new ArrayList<>();
                for (PoiInfo item:poiResult.getAllPoi()) {

                }
                poiList.addAll(poiResult.getAllPoi());//添加进入集合
                searchItemAdapter.notifyDataSetChanged();//刷新适配器
            } else {

            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            //废弃
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    /**
     * TODO*************************绘制圆*************************
     */
    //以当前的经纬度为圆心绘制一个圆
    private void drawCircle(double lat, double gt) {
        //设置圆心位置
        LatLng center = new LatLng(lat, gt);
        //构造CircleOptions对象 设置圆对象
        CircleOptions circleOptions = new CircleOptions().center(center)
                .radius(25)
                .fillColor(0x500000FF)//填充颜色 前两位是阿尔法值 00代表透明
                .stroke(new Stroke(1, 0x500000FF)); //设置边框的宽度和颜色
        baiduMap.clear();//清理之前的圆
        //在地图上添加显示圆
        baiduMap.addOverlay(circleOptions);
    }

    //以当前的经纬度创建一个点标记
    private void addMark(double lat, double gt) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, gt);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_mark);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    /**
     * TODO*************************文字覆盖物*************************
     */
    //文字覆盖物
    private void latlng(double lat, double gt) {
        LatLng llText = new LatLng(lat, gt);//获取当前的经纬度
        //构建TextOptions对象
        OverlayOptions mTextOptions = new TextOptions()
                .text("百度地图SDK")//文字内容
                .bgColor(0x50FFFF00) //背景色
                .fontSize(24) //字号
                .fontColor(0xFFFF00FF) //文字颜色
                .rotate(-30) //旋转角度
                .position(llText);

        //在地图上显示文字覆盖物
        Overlay mText = baiduMap.addOverlay(mTextOptions);
    }

    InfoWindow mInfoWindow;//弹窗覆盖物
    //添加信息窗
    private void window(final String address, double lat, double gt) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, gt);
        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.mipmap.icon_marka);
//        //构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
//        //在地图上添加Marker，并显示
//        baiduMap.addOverlay(option);
        //构造Icon列表
        // 初始化bitmap 信息，不用时及时 recycle
        BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);
        BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.mipmap.icon_markb);
        BitmapDescriptor bdC = BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark);

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        //构造MarkerOptions对象
        MarkerOptions ooD = new MarkerOptions()
                .position(point)
                .icons(giflist)
                .zIndex(0)
                .period(20);//定义刷新的帧间隔

        //在地图上展示包含帧动画的Marker
        Overlay mMarkerD = (Marker) (baiduMap.addOverlay(ooD));


        //第一种方法 使用View构造InfoWindow
        //用来构造InfoWindow的Button
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        button.setText(address);

        //构造InfoWindow
        //point 描述的位置点
        //-100 InfoWindow相对于point在y轴的偏移量
        mInfoWindow = new InfoWindow(button, new LatLng(lat, gt), -100);

        //使InfoWindow生效
        baiduMap.showInfoWindow(mInfoWindow);

        //第二种使用BitmpDescriptor构造InfoWindow
        //用来构造InfoWindow
//        BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.info);
//
//        //响应点击的OnInfoWindowClickListener
//        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick() {
//                Toast.makeText(MapActivity.this, address, Toast.LENGTH_LONG).show();
//            }
//        };
        //构造InfoWindow
        //point 描述的位置点
        //-100 InfoWindow相对于point在y轴的偏移量
//        mInfoWindow = new InfoWindow(mBitmap, new LatLng(lat,gt), -30, listener);

        //使InfoWindow生效
//        baiduMap.showInfoWindow(mInfoWindow);
    }


    /**
     * TODO*************************路径规划*************************
     */
    RoutePlanSearch routePlanSearch;
    private PlanNode startNode, endNode; //开始和结束的坐标点

    SuggestionSearch suggestionSearch; //地点检索的类
    SuggestAdapter suggestAdapter; //路径规划搜索出来的列表
    List<SuggestionResult.SuggestionInfo> suggestList; //地点检索的结果
    boolean isStart = true; //当前处理的是否是起点
    LatLng startLatLng; //起点的经纬度

    //初始化路径规划
    private void initRoutePlan() {
        //创建路线规划地点检索实例
        suggestionSearch = SuggestionSearch.newInstance();
        //地点检索的结果集合
        suggestList = new ArrayList<>();
        suggestAdapter = new SuggestAdapter(suggestList, this);
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mRlv.setAdapter(suggestAdapter);
        //设置监听地点检索
        suggestionSearch.setOnGetSuggestionResultListener(suggestionResultListener);

        //监听起点输入框的光标
        input_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isStart = true;
                    mRlv.setVisibility(View.VISIBLE);//显示集合
                }
            }
        });
        //监听起点输入框的变化
        input_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //起点输入改变以后获取对应的起点数据
                SuggestionSearchOption option = new SuggestionSearchOption();
                option.city("北京");
                option.citylimit(true);
                option.keyword(s.toString());
                suggestionSearch.requestSuggestion(option);
            }
        });
        //监听终点输入框的光标
        input_end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isStart = false;
                    mRlv.setVisibility(View.VISIBLE);//显示集合
                }
            }
        });
        //监听终点输入框的输入
        input_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //获取终点框对应的数据
                SuggestionSearchOption option = new SuggestionSearchOption();
                option.city("北京");
                option.citylimit(true);
                option.keyword(s.toString());
                suggestionSearch.requestSuggestion(option);
            }
        });
        //创建路线规划检索实例
        routePlanSearch = RoutePlanSearch.newInstance();
        //设置路线规划检索监听器
        routePlanSearch.setOnGetRoutePlanResultListener(routePlanResultListener);
        suggestAdapter.setMyClick(new SuggestAdapter.MyClick() {
            @Override
            public void itemClick(int pos) {
                SuggestionResult.SuggestionInfo suggestionInfo = suggestList.get(pos);

                if (isStart) {//当前处理的是否是起点 是
                    input_start.setText(suggestionInfo.getKey());//则给开始的地方赋值
                    startLatLng = suggestionInfo.getPt();//存储开始的地方
                } else {
                    input_end.setText(suggestionInfo.getKey());//不是，则给终点赋值
                }
                mRlv.setVisibility(View.GONE);//隐藏
            }
        });
    }

    //创建地点检索结果监听器
    OnGetSuggestionResultListener suggestionResultListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult.getAllSuggestions() != null && suggestionResult.getAllSuggestions().size() > 0) {
                //地点检索结果
                suggestList.clear();//清空集合
                suggestList.addAll(suggestionResult.getAllSuggestions());//再次赋值
                suggestAdapter.notifyDataSetChanged();
            } else {
                //地点检索结果
                suggestList.clear();
                suggestList.addAll(suggestionResult.getAllSuggestions());
                suggestAdapter.notifyDataSetChanged();
            }
        }
    };

    //创建路线规划检索结果监听器
    OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            //创建WalkingRouteOverlay实例
            //创建一个路径规划的类
            WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(baiduMap);
            //判断当前查找出来的路线
            if (walkingRouteResult.getRouteLines() != null && walkingRouteResult.getRouteLines().size() > 0) {
                //获取路径规划数据,(以返回的第一条数据为例)
                //为WalkingRouteOverlay实例设置路径数据
                walkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(0));
                //在地图上绘制WalkingRouteOverlay
                walkingRouteOverlay.addToMap();
                //当前的定位移动到开始点并放大地图  放大16倍
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(startLatLng, 16);
                baiduMap.setMapStatus(status);
            } else {
                Toast.makeText(MainActivity.this, "未搜索到相关路径", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private void searchRoute() {
        String startName, endName;
        startName = input_start.getText().toString();//获得起点
        endName = input_end.getText().toString();//获得终点
        if (TextUtils.isEmpty(startName) || TextUtils.isEmpty(endName)) {//判断输入框是否为框
            Toast.makeText(this, "请输入正确起点和终点", Toast.LENGTH_SHORT).show();
        } else {//都不为空
            //准备起终点信息
            startNode = PlanNode.withCityNameAndPlaceName("北京", startName);//开始和结束的地址
            endNode = PlanNode.withCityNameAndPlaceName("北京", endName);
            WalkingRoutePlanOption option = new WalkingRoutePlanOption();
            option.from(startNode);//发起检索
            option.to(endNode);
            //搜索路径
            routePlanSearch.walkingSearch(option);
        }
    }

    /**
     * TODO*************************检索行政区边界*************************
     */

    DistrictSearch mDistrictSearch;

    private void initNav() {
        View headerView = mNv.getHeaderView(0);
        final EditText input = headerView.findViewById(R.id.et_nav_head_input);
        Button start = headerView.findViewById(R.id.btn_nav_head_start);
        start.setOnClickListener(new View.OnClickListener() {//监听
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s = input.getText().toString();//获得输入框中的值

                        initDistrict(s);
                    }
                });
            }
        });
    }

    private void initDistrict(String s) {
        //创建行政区边界数据检索实例
        mDistrictSearch = DistrictSearch.newInstance();
        //设置行政区边界数据检索监听器
        mDistrictSearch.setOnDistrictSearchListener(listener);
        //设置DistrictSearchOption，发起检索
        mDistrictSearch.searchDistrict(new DistrictSearchOption()//请求行政区数据
                .cityName("北京市")//检索城市名称
                .districtName(s));//检索的区县名称

    }

    //创建行政区边界数据检索监听器
    OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {
        @Override
        public void onGetDistrictResult(DistrictResult districtResult) {
            //对检索所得行政区划边界数据进行处理
            if (null != districtResult) {
                districtResult.getCenterPt();//获取行政区中心坐标点
                districtResult.getCityName();//获取行政区域名称
                baiduMap.clear();//清理数据
                //获取边界坐标点，并展示
                if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {//没有错误的时候
                    List<List<LatLng>> polyLines = districtResult.getPolylines();//获取行政区域边界坐标点
                    if (polyLines == null) {//如果等于空就结束
                        return;
                    }
                    //边界就是坐标点的集合，在地图上画出来就是多边形图层。有的行政区可能有多个区域，所以会有多个点集合。
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (List<LatLng> polyline : polyLines) {
                        //绘制折线
                        OverlayOptions mOverlayOptions = new PolylineOptions()
                                .width(10)//折线宽度
                                .points(polyline)//	折线坐标点列表
                                .dottedLine(true) //设置折线显示为虚线
                                .color(0x5000FF88);//折线颜色
                        //.visible(true);//折线是否可见
                        //在地图上绘制折线
                        baiduMap.addOverlay(mOverlayOptions);

                        //绘制折线
                        OverlayOptions ooPolygon = new PolygonOptions()
                                .points(polyline)
                                .stroke(new Stroke(5, 0xAA00FF88)).fillColor(0xAAFFFF00);
                        baiduMap.addOverlay(ooPolygon);

                        for (LatLng latLng : polyline) {
                            builder.include(latLng);
                        }
                    }
                    baiduMap.setMapStatus(MapStatusUpdateFactory
                            .newLatLngBounds(builder.build()));
                }

            }
        }
    };

    /**
     * TODO*************************聚合管理*************************
     */
    //地图类型切换
    private void initMapType() {
        List<String> listMapType = new ArrayList<>();
        listMapType.add("俯视图");
        listMapType.add("卫星图");
        listMapType.add("聚合");
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mRlv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        MapTypeAdapter mapTypeAdapter = new MapTypeAdapter(listMapType, this);
        mRlv.setAdapter(mapTypeAdapter);
        mapTypeAdapter.notifyDataSetChanged();

        mapTypeAdapter.setMyClick(new MapTypeAdapter.MyClick() {
            @Override
            public void itemClick(int pos) {
                if (pos == 0) baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                if (pos == 1) baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                if (pos == 2) initPolymerization(); //实现聚合定点
            }
        });
    }
    ClusterManager<MyItem> mClusterManager;

    private void initPolymerization() {
//        mMapStatus = new MapStatus.Builder()
//                .target(new LatLng(39.914935, 116.403119)).zoom(8).build();
//
//        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));

        //初始化点聚合管理类
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<>(this, baiduMap);
        }

        //清空点聚合 地标标志
        // mClusterManager.addItems(new ArrayList <>());
        mClusterManager.cluster(); //函数，更新数据。

        // 添加Marker点
        if (poiList.size() > 0) {
            List<MyItem> items = new ArrayList<>();//存放经纬度
            for (int i = 0; i < poiList.size(); i++) {
                PoiInfo poiInfo = poiList.get(i);
                //获取集合
                LatLng llA = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
                items.add(new MyItem(llA));
                //传入方法里面
                addMark(poiInfo.location.latitude, poiInfo.location.longitude, poiInfo.getName());
            }
            mClusterManager.addItems(items);
            mClusterManager.cluster(); //函数，更新数据。
            // 设置地图监听，当地图状态发生改变时，进行点聚合运算
            baiduMap.setOnMapStatusChangeListener(mClusterManager);
            // 设置maker点击时的响应
//            baiduMap.setOnMarkerClickListener(mClusterManager);
//
//            mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
//                @Override
//                public boolean onClusterClick(Cluster<MyItem> cluster) {
//                    Toast.makeText(MapActivity.this, "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
//            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
//                @Override
//                public boolean onClusterItemClick(MyItem item) {
//                    Toast.makeText(MapActivity.this, "点击单个Item", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
        }
    }

    //单个点
    public void addMark(final double lat, final double gt, String name) {
        //定义Maker坐标点 //设置圆心位置
        LatLng point = new LatLng(lat, gt);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_mark);
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //定义动画
        option.animateType(MarkerOptions.MarkerAnimateType.jump);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);

        /*添加信息窗（弹窗覆盖物InfoWindow）*/

        //用来构造InfoWindow的Button  getApplicationContext()
        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.popup);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前的定位移动到开始点并放大地图
                LatLng start = new LatLng(lat, gt);
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(start, 18);
                baiduMap.setMapStatus(status);
            }
        });

        //构造InfoWindow
        //point 描述的位置点
        //-100 InfoWindow相对于point在y轴的偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, point, -100);

        //使InfoWindow生效
        baiduMap.showInfoWindow(mInfoWindow);
    }

    /**
     * TODO*************************指南针-传感器管理器*************************
     */
    //指南针 传感器管理器
//     SensorManager mSensorManager;
//    // 记录指南针图片转过的角度
//    float currentDegree = -100f;
//    // 获取触发event的传感器类型 领图片方向旋转
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        int sensorType = event.sensor.getType();
//        if (sensorType == Sensor.TYPE_ORIENTATION) {
//            // 获取绕Z轴转过的角度
//            float degree = event.values[0];
//            // 创建旋转动画（反向转过degree度）
//            RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            // 设置动画的持续时间
//            ra.setDuration(200);
//            // 运行动画
//            ivZhinan.startAnimation(ra);
//            currentDegree = -degree;
//        }
//    }
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }

    /**
     * TODO*************************地图的生命周期*************************
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        // 为系统的方向传感器注册监听器
        // mSensorManager.registerListener((SensorEventListener) this,
        // mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
        // SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        // 取消注册 系统的方向传感器
        // mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消注册 系统的方向传感器
        // mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        locationClient.unRegisterLocationListener(myListener);
        locationClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        //释放检索实例
        poiSearch.destroy();
        //释放步行检索实例
        routePlanSearch.destroy();
        //放行政区边界数据检索实例
        mDistrictSearch.destroy();
    }

}
