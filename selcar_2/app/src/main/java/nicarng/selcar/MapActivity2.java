package nicarng.selcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

public class MapActivity2 extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener,
        MapView.POIItemEventListener, MapView.MapViewEventListener{

    public static Activity _map2;
    private MapView mapView2;
    boolean locationTrack = true;

    MapPOIItem marker = new MapPOIItem();
    MapPOIItem marker2 = new MapPOIItem();
    MapPOIItem marker3 = new MapPOIItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        _map2 = MapActivity2.this;

        mapView2 = new MapView(this);
        mapView2.setDaumMapApiKey("1d25624ca7ce2d2404ff9471b43bfa6f");

        ViewGroup mapViewContainer2 =  findViewById(R.id.map_view);
        mapView2.setPOIItemEventListener(this);
        mapView2.setCurrentLocationEventListener(this);
        mapView2.setMapViewEventListener(this);

        Intent intent = getIntent();
        String realLocation = intent.getStringExtra("real_location");

        mapView2.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationTrack){
                    locationTrack = false;
                    mapView2.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                    Toast location = Toast.makeText(MapActivity2.this, "현재위치 추적을 시작합니다.", Toast.LENGTH_SHORT);
                    location.setGravity(Gravity.CENTER, 0, 0);
                    location.show();
                    Picasso.with(MapActivity2.this).load(R.drawable.locationtracking).into(fab);
                }else{
                    locationTrack = true;
                    Toast location = Toast.makeText(MapActivity2.this, "현재위치 추적을 종료합니다.", Toast.LENGTH_SHORT);
                    mapView2.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                    location.setGravity(Gravity.CENTER, 0, 0);
                    location.show();
                    Picasso.with(MapActivity2.this).load(R.drawable.location).into(fab);
                }
            }
        });
        mapViewContainer2.addView(mapView2);

        marker.setItemName("공학관 주차장");
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2978219, 126.8380179));
        marker.setTag(0);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        marker2.setItemName("학술정보관 주차장");
        marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2963441, 126.834405));
        marker2.setTag(1);
        marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        marker3.setItemName("인재원 주차장");
        marker3.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2932104,126.8365962));
        marker3.setTag(2);
        marker3.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker3.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView2.addPOIItem(marker);
        mapView2.addPOIItem(marker2);
        mapView2.addPOIItem(marker3);

        if(realLocation.equals("공학관 주차장")){
            mapView2.selectPOIItem(marker, true);
            mapView2.setMapCenterPoint(marker.getMapPoint(), true);
        }else if(realLocation.equals("학술정보관 주차장")){
            mapView2.selectPOIItem(marker2, true);
            mapView2.setMapCenterPoint(marker2.getMapPoint(), true);
        }else if(realLocation.equals("인재원 주차장")){
            mapView2.selectPOIItem(marker3, true);
            mapView2.setMapCenterPoint(marker3.getMapPoint(), true);
        }else {
            Toast.makeText(this, "해당 차량의 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
            mapView2.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
    }

    private void onFinishReverseGeoCoding(String result) {
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }


}
