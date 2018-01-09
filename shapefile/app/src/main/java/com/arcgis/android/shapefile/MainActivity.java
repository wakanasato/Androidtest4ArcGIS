package com.arcgis.android.shapefile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * test for Release
 * version：100.2.0
 * createDate：2018/01/09
 * wrote by wakakanasato
 *
 * get attribute info from shapefile
 *
 * */
public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private ArcGISMap mArcGISMap;
    public String TAG = "★★★esrij_test★★★";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.mapView);
        mArcGISMap = new ArcGISMap(Basemap.createTopographic());
        mMapView.setMap(mArcGISMap);

        requestWritePermission();
    }

    /**
     * Request read permission on the device.
     */
    private void requestWritePermission() {
        // define permission to request
        String[] reqPermission = new String[] { Manifest.permission.READ_EXTERNAL_STORAGE };
        int requestCode = 2;
        // For API level 23+ request permission at runtime
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                reqPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            readShapefile();
        } else {
            // request permission
            ActivityCompat.requestPermissions(MainActivity.this, reqPermission, requestCode);
        }
    }

    /**
     * read shapefile
     *
     * */
    private void readShapefile(){

        // shapefile directry
        String shapfile  = Environment.getExternalStorageDirectory().getPath() +  "/ArcGIS/samples/shapefile/sjis/sjis.shp";
        File file = new File(shapfile);
        Log.d(TAG, "file exist:"+ file.exists());

        // create shapefiletable
        ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(shapfile);
        FeatureLayer featureLayer = new FeatureLayer(shapefileFeatureTable);
        // representation
        mArcGISMap.getOperationalLayers().add(featureLayer);

        // get attributes
        QueryParameters parameters = new QueryParameters();
        parameters.setWhereClause("1=1");
        final ListenableFuture<FeatureQueryResult> queryFeature = shapefileFeatureTable.queryFeaturesAsync(parameters);
        queryFeature.addDoneListener(new Runnable() {
            @Override
            public void run() {
                FeatureQueryResult result = null;
                try {
                    result = queryFeature.get();
                    // check there are some results
                    Iterator<Feature> ite = result.iterator();
                    while(ite.hasNext()){
                        Feature  feature = ite.next();
                        Map<String,Object> attmap = feature.getAttributes();
                        for (String key : attmap.keySet()) {
                            // TODO Japanese fields is garbed and if japanese field name,Japanese & English data are null
                            Log.d(TAG,key + " => " + attmap.get(key));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

