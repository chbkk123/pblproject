package nicarng.selcar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFile extends AsyncTask<String,String,String> {

    Context context;
    ProgressDialog mPorgressDialog;
    String fileName;

    HttpURLConnection conn = null;
    DataOutputStream dos = null;

    String lineEnd = "\r\n";
    String twoHyphens ="--";
    String boundary = "*****";

    int bytesRead,bytesAvailable,buffersize;
    byte[] buffer;
    int maxBufferSize = 2048;
    File sourceFile;
    int serverResponseCode;
    String TAG = "FileUpload";

    public UploadFile(Context context){
        this.context = context;
    }
    public void setPath(String uploadFilePath){
        this.fileName = uploadFilePath;
        this.sourceFile = new File(uploadFilePath);
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mPorgressDialog = new ProgressDialog(context);
        mPorgressDialog.setTitle("Loading...");
        mPorgressDialog.setMessage("Image uploading...");
        mPorgressDialog.setCanceledOnTouchOutside(false);
        mPorgressDialog.setIndeterminate(false);
        mPorgressDialog.show();
    }
    @Override
    protected String doInBackground(String... strings){
        if(!sourceFile.isFile()){
            Log.e(TAG,"sourceFile("+fileName+")is Not A File");
            return null;
        }else{
            String success = "Success";
            Log.i(TAG,"sourceFile("+fileName+")is A File");
            try{
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strings[0]);
                Log.i("strings[0]",strings[0]);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection","Keep-Alive");
                conn.setRequestProperty("ENCTYPE","multipart/form-data");
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
                conn.setRequestProperty("uploaded_file",fileName);
                Log.i(TAG,"fileName:"+fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens+boundary+lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"selpcar\""+lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("newImage");
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens+boundary+lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""+fileName+"\""+lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();

                buffersize=Math.min(bytesAvailable,maxBufferSize);
                buffer = new byte[buffersize];

                bytesRead = fileInputStream.read(buffer,0,buffersize);
                while(bytesRead > 0){
                    dos.write(buffer,0,buffersize);
                    bytesAvailable = fileInputStream.available();
                    buffersize=Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,buffersize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens+boundary+twoHyphens+lineEnd);

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                Log.i(TAG,"[UploadImageToServer] HTTP Resplonse is :"+serverResponseMessage+": "+serverResponseCode);
                if(serverResponseCode==200){}
                BufferedReader brd;

                brd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line;
                while((line=brd.readLine())!=null){
                    Log.i("Upload State",line);
                }
                fileInputStream.close();
                dos.flush();
                dos.close();
            }catch(Exception e){
                Log.e(TAG+"ERROR",e.toString());
            }
            return success;
        }
    }
    @Override
    protected void onPostExecute(String result){
        mPorgressDialog.dismiss();
        Toast.makeText(context,"업로드 완료",Toast.LENGTH_SHORT).show();
    }
}
