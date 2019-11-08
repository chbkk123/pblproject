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


public class MapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener,
        MapView.POIItemEventListener, MapView.MapViewEventListener{

    public static Activity _map;
    ViewGroup mapViewContainer;
    private MapView mapView;
    boolean locationTrack = true, checking = false;
    UserSessionManager session;

    MapPOIItem marker = new MapPOIItem();
    MapPOIItem marker2 = new MapPOIItem();
    MapPOIItem marker3 = new MapPOIItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        _map = MapActivity.this;

        session = new UserSessionManager(getApplicationContext());

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("13176533dbf8387e421472657011ef8e");

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);

        final FloatingActionButton fab = findViewById(R.id.fab);

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.297083, 126.8362114), 2, true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationTrack){
                    locationTrack = false;
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                    Toast location = Toast.makeText(MapActivity.this, "현재위치 추적을 시작합니다.", Toast.LENGTH_SHORT);
                    location.setGravity(Gravity.CENTER, 0, 0);
                    location.show();
                    Picasso.with(MapActivity.this).load(R.drawable.locationtracking).into(fab);
                }else{
                    locationTrack = true;
                    Toast location = Toast.makeText(MapActivity.this, "현재위치 추적을 종료합니다.", Toast.LENGTH_SHORT);
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                    location.setGravity(Gravity.CENTER, 0, 0);
                    location.show();
                    Picasso.with(MapActivity.this).load(R.drawable.location).into(fab);
                }
            }
        });

        mapViewContainer.addView(mapView);

        marker.setItemName("공학관 주차장");
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2978219, 126.8380179));
        marker.setTag(0);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.getMapPoint();

        marker2.setItemName("학술정보관 주차장");
        marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2963441, 126.834405));
        marker2.setTag(1);
        marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        marker2.getMapPoint();

        marker3.setItemName("인재원 주차장");
        marker3.setMapPoint(MapPoint.mapPointWithGeoCoord(37.2932104,126.8365962));
        marker3.setTag(2);
        marker3.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker3.getMapPoint();

        mapView.addPOIItem(marker);
        mapView.addPOIItem(marker2);
        mapView.addPOIItem(marker3);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(checking){
            mapViewContainer.removeAllViews();
            mapView = new MapView(this);
            mapView.setPOIItemEventListener(this);
            mapView.setCurrentLocationEventListener(this);
            mapView.setMapViewEventListener(this);

            mapViewContainer.addView(mapView);

            mapView.addPOIItem(marker);
            mapView.addPOIItem(marker2);
            mapView.addPOIItem(marker3);
        }else {
            checking = true;
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
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
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
            final int tagnum = mapPOIItem.getTag();
            Intent intent = new Intent(this, IndvActivity.class);
            intent.putExtra("tagnum", tagnum);
            startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }
}

