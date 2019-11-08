package nicarng.selcar;

import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.media.ExifInterface;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCaraddActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_TAKE_PHOTO = 2001;
    private static final int REQUEST_TAKE_ALBUM = 2002;
    private static final int REQUEST_IMAGE_CROP = 2003;

    private Uri mImageCaptureUri, AlbumUri;
    private String mImageName;
    private String currentPath;
    private ImageView iv_UserPhoto;

    public static TextView car_Name;
    public static TextView car_Type;

    private SpinnerAdapter adapter;
    private List<Spinner> spinnerList;

    boolean isAlbum = false;
    int id_view;

    private android.support.v7.app.AlertDialog dialog;

    UserSessionManager seesion;
    UserSessionManager_ID sessionId;

    RadioGroup parking, fuel, trans, CarType ;
    RadioButton type1,type2,type3,type4;
    RadioButton park1, park2, park3;
    RadioButton gas, diesel, lpg, elec, hybrid;
    RadioButton manual, automatic;
    public static android.widget.Spinner spinner;
    String Selecting;

    int selected_value_idx = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_caradd);

        seesion = new UserSessionManager(getApplicationContext());
        sessionId = new UserSessionManager_ID(getApplicationContext());

        spinner = findViewById(R.id.abspinner);
        final ArrayAdapter spinnerTitle = ArrayAdapter.createFromResource(this,R.array.SpinnerTtile,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerTitle);

        car_Type = findViewById(R.id.carType);
        car_Name = findViewById(R.id.carName);
        final EditText car_Number = findViewById(R.id.carNum);
        final EditText car_Year = findViewById(R.id.carYear);
        final EditText car_KmL = findViewById(R.id.carKmL);
        final EditText car_Km = findViewById(R.id.carKm);
        final EditText car_Color = findViewById(R.id.carColor);

        CarType = findViewById(R.id.CarType_RadioGroup);
            type1 = findViewById(R.id.LightCar);
            type1.setTag(1);
            type2 = findViewById(R.id.CompactCar);
            type2.setTag(2);
            type3 = findViewById(R.id.MidSizeCar);
            type3.setTag(3);
            type4 = findViewById(R.id.FullSizedCar);
            type4.setTag(4);

        parking = findViewById(R.id.parking_erica);
            park1 = findViewById(R.id.parking1);
            park2 = findViewById(R.id.parking2);
            park3 = findViewById(R.id.parking3);

        fuel = findViewById(R.id.fuel_type);
            gas = findViewById(R.id.gas);
            diesel = findViewById(R.id.diesel);
            lpg = findViewById(R.id.lpg);
            elec = findViewById(R.id.electric);
            hybrid = findViewById(R.id.hybrid);

        trans = findViewById(R.id.transmission);
            manual = findViewById(R.id.manual);
            automatic = findViewById(R.id.automatic);

        iv_UserPhoto = (ImageButton) this.findViewById(R.id.carPhoto);

        CarType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton CarTypeSelected = findViewById(CarType.getCheckedRadioButtonId());
                Selecting = CarTypeSelected.getText().toString();
            }
        });

        ImageButton searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                spinnerList = new ArrayList<Spinner>();
                adapter = new SpinnerAdapter(getApplicationContext(),spinnerList,UserCaraddActivity.this);
                class Register_my_car extends AsyncTask<Void, Void, String> {
                    String target;
                    protected  void onPreExecute(){
                        target = "https://wjddn944.cafe24.com/GetCarType.php?car_type=";
                    }
                    protected String doInBackground(Void... params){
                        try{
                            URL url = new URL(target+Selecting);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String temp;
                            StringBuilder stringBuilder = new StringBuilder();
                            while((temp = bufferedReader.readLine()) != null)
                            {
                                stringBuilder.append(temp +"\n");
                            }
                            bufferedReader.close();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            return  stringBuilder.toString().trim();

                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        return "error";
                    }
                    public void onPostExecute(String result){

                        try{
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            int count=0;
                            String car_name,car_type;
                            while (count < jsonArray.length()){
                                JSONObject object = jsonArray.getJSONObject(count);
                                car_name = object.getString("car_name");
                                car_type = object.getString("car_type");
                                Spinner dbspinner = new Spinner(car_name, car_type);


                                spinnerList.add(dbspinner);

                                count++;
                                spinner.setSelection(0);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                new Register_my_car().execute();
                spinner.setAdapter(adapter);
                spinner.performClick();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selected_value_idx = spinner.getSelectedItemPosition();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        });

        Button carraddbutton = findViewById(R.id.addCarFinish);
        carraddbutton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                RadioButton park = findViewById(parking.getCheckedRadioButtonId());
                RadioButton fuelType = findViewById(fuel.getCheckedRadioButtonId());
                RadioButton transType = findViewById(trans.getCheckedRadioButtonId());
                final String user_id = sessionId.getUserDetails_ID().get("id");
                final String car_image = mImageName;
                final String car_type = car_Type.getText().toString();
                final String car_name = car_Name.getText().toString();
                final String car_number = car_Number.getText().toString();
                final String car_km = car_Km.getText().toString();
                final String car_year = car_Year.getText().toString();
                final String car_kmL = car_KmL.getText().toString();
                final String car_color = car_Color.getText().toString();
                final String car_location = park.getText().toString();
                final String car_fuel = fuelType.getText().toString();
                final String car_trans = transType.getText().toString();

                if(user_id.equals("") ||car_type.equals("")||car_number.equals("")||car_km.equals("")||car_year.equals("")
                        ||car_kmL.equals("")||car_color.equals("")||car_location.equals("")||car_fuel.equals("")||car_trans.equals("")){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UserCaraddActivity.this);
                    dialog = builder.setMessage("No Empty Space Please")
                            .setNegativeButton("Again",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("IN_RESPONSE", response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserCaraddActivity.this);
                                AlertDialog ad = builder.setMessage("Success!!")
                                        .setPositiveButton("Ok", null)
                                        .create();
                                ad.show();
                                ad.dismiss();
                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserCaraddActivity.this);
                                builder.setMessage("Fail!!")
                                        .setNegativeButton("Again", null)
                                        .create()
                                        .show();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                UserCaraddReq userCaraddReq = new UserCaraddReq(user_id, car_image,car_type,car_name, car_number, car_km, car_year,
                        car_kmL, car_color, car_location, car_fuel, car_trans, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserCaraddActivity.this);
                queue.add(userCaraddReq);
            }
        });

    }
    // 7.0.0 이상 필요한 코드//
    public void doTakePhotoAction(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch (IOException ex){
                }
                if(photoFile != null){
                    mImageCaptureUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }


    private File createImageFile() throws IOException{
        File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "selcar");
        if(!dir.exists()){
            dir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageName = timeStamp + ".jpg";

        File storageDir = new File(dir, mImageName);
        currentPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void pickCamera(){
        Bitmap bitmap = BitmapFactory.decodeFile(currentPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if(exif != null){
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegree(exifOrientation);
        }else {
            exifDegree = 0;
        }
        iv_UserPhoto.setImageBitmap(rotate(bitmap, exifDegree));

    }

    private int exifOrientationToDegree(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public void doTakeAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    public void pickAlbum(String imagepath){
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(imagepath);
        }catch (IOException e){
            e.printStackTrace();
        }

        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegree(exifOrientation);
        Log.e("Uri : ",imagepath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
        iv_UserPhoto.setImageBitmap(rotate(bitmap,exifDegree));
    }

    //cropImage : 이미지를 크롭.
    public void cropImage(){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");


        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        cropIntent.setDataAndType(mImageCaptureUri,"image/*");
        cropIntent.putExtra("output",200); //crop한 이미지 y축 크기
        cropIntent.putExtra("aspectX",4); //crop박스의 x축 비율
        cropIntent.putExtra("aspectY",3); //crop박스의 y축 비율
        cropIntent.putExtra("scale",true);

        if(!isAlbum){
            cropIntent.putExtra("output", mImageCaptureUri);
        }else if(isAlbum){
            cropIntent.putExtra("output", AlbumUri);
        }

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(cropIntent,0);
        grantUriPermission(list.get(0).activityInfo.packageName, mImageCaptureUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent i = new Intent(cropIntent);
        ResolveInfo res = list.get(0);
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        grantUriPermission(res.activityInfo.packageName, mImageCaptureUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(i,REQUEST_IMAGE_CROP);
    }


    //갤러리 동기화 : 새로고침 등에서 일어날 수 있는 버그 방지.
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("onActivityResult","Call OK" +requestCode +"and" +resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case REQUEST_TAKE_PHOTO: {
                isAlbum = false;
                cropImage();
                break;
            }
            case REQUEST_TAKE_ALBUM: {
                isAlbum = true;
                File albumFile = null;
                try{
                    albumFile = createImageFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if(albumFile !=null){
                    AlbumUri = Uri.fromFile(albumFile);
                }
                mImageCaptureUri = data.getData();
                cropImage();
                break;
            }
            case REQUEST_IMAGE_CROP: {
                galleryAddPic();
                uploadFile(currentPath);
                if(isAlbum){
                    pickAlbum(currentPath);
                }else {
                    pickCamera();
                }
                break;
            }
            default:
                break;
        }
    }
    public void uploadFile(String filePath){
        String url = "http://wjddn944.cafe24.com/imageupload.php";
        try{
            UploadFile uploadFile = new UploadFile(UserCaraddActivity.this);
            uploadFile.setPath(filePath);
            uploadFile.execute(url);
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        id_view = v.getId();
        if(v.getId() == R.id.addCarFinish){
            Toast.makeText(getApplicationContext(),"등록 완료" , Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.carPhoto){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this).setTitle("이미지 선택")
                    .setPositiveButton("앨범", albumListener)
                    .setNeutralButton("취소", cancelListener)
                    .setNegativeButton("사진", cameraListener).show();
        }

    }


}

