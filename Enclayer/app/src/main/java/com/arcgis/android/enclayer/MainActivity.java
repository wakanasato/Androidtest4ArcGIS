package com.arcgis.android.enclayer;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;

import com.esri.arcgisruntime.hydrography.EncCell;
import com.esri.arcgisruntime.hydrography.EncDataset;
import com.esri.arcgisruntime.hydrography.EncEnvironmentSettings;
import com.esri.arcgisruntime.hydrography.EncExchangeSet;
import com.esri.arcgisruntime.layers.EncLayer;
import com.esri.arcgisruntime.layers.WmsLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * EnvLayer
 * ver：100.2.0
 * */
public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private ArcGISMap mArcGISMap;

    public String TAG = "★★★esrij_test★★★";
    public String encFilePath = "/ArcGIS/samples/enc/S57/S57/ENC_ROOT";
    public String sencFilePath = "/ArcGIS/samples/enc/senc/";
    public String encdata  = Environment.getExternalStorageDirectory().getPath() +  encFilePath;
    public String sencdata  = Environment.getExternalStorageDirectory().getPath() +  sencFilePath;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MapView
        mMapView = findViewById(R.id.mapView);
        mArcGISMap = new ArcGISMap(Basemap.createTopographic());

        // set data path
        EncEnvironmentSettings.setResourcePath(encdata);
        EncEnvironmentSettings.setSencDataPath(sencdata);
        File encfolder = new File(encdata);
        File sencfolder = new File(sencdata);
        Log.d(TAG, "ENC folder:" + encfolder.exists());
        Log.d(TAG, "SENC folder:" + sencfolder.exists());

        EncCell encCell = new EncCell(encdata + "/7T4BSMP1.000");

        final EncLayer encLayer = new EncLayer(encCell);
        encLayer.loadAsync();

        encLayer.addDoneLoadingListener(new Runnable() {
            public void run() {
                if (encLayer.getLoadStatus() == LoadStatus.LOADED) {
                    // ENC layer has loaded
                }else{
                    Log.d(TAG, "failed loadstatus:" + encLayer.getLoadStatus());

                }
            }
        });
        mArcGISMap.getOperationalLayers().add(encLayer);
        mMapView.setMap(mArcGISMap);

    }
}