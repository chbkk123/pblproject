package nicarng.selcar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class IntroActivity extends Activity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        TedPermission.with(this).setPermissionListener(permissionListener)
                .setRationaleMessage("현재 위치 및 카메라, 저장소 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intent); //다음화면으로 넘어감
                    finish();
                }
            },1500); //1.5초 뒤에 Runner객체 실행하도록 함
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(IntroActivity.this, "권한 거부됨\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

}
