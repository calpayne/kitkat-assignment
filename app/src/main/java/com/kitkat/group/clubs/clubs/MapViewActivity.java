package com.kitkat.group.clubs.clubs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kitkat.group.clubs.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MapViewActivity extends AppCompatActivity {

    private MapView map;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1Ijoia2l0a2F0MTkwNCIsImEiOiJjanVxeG9qdmwxMTY0NGRzamgzMWVldmJyIn0.kYiq4y3UvdrCg32zebOR6w");
        setContentView(R.layout.activity_map_view);

        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);


        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        if(getIntent().getStringExtra("locCoOrds") == null){

                            String[] coOrds =  getIntent().getStringExtra("locCoOrds").split(",");

                            LatLng location = new LatLng(Double.parseDouble(coOrds[0]),
                                                         Double.parseDouble(coOrds[1])
                                    );
                            System.out.println(location.toString());

                            //using depreciated methods as new handler API requires API 28
                            if(getIntent().getStringExtra("locName") == null)
                                mapboxMap.addMarker(new MarkerOptions().position(location).title(getIntent().getStringExtra("locName")));
                            else
                                mapboxMap.addMarker(new MarkerOptions().position(location));
                            mapboxMap.setCameraPosition(new CameraPosition.Builder().target(location).zoom(10).build());
                        }else
                            mapboxMap.addMarker(new MarkerOptions().position(new LatLng(50,0.1)).title(getIntent().getStringExtra("Null Location")));

                    }
                });
            }
        });
    }
    //Login: kitkat1904 Password: KitKatADS
}
