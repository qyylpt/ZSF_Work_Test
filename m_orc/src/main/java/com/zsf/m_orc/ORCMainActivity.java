package com.zsf.m_orc;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.zsf.global.GlobalData;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
@Route(path = "/m_orc/ORCMainActivity")
public class ORCMainActivity extends BaseActivity {
    private ImageView imageViewOrc;
    private Button buttonNext;
    private TextView textViewOrc;
    private int[] imageFile = new int[]{R.drawable.five,R.drawable.six, R.drawable.three,R.drawable.one,};
    private int currentId = 0;
    private Bitmap bitmap;


    /**
     * TessBaseAPI初始化用到的第一个参数，是个目录。
     */
    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private static final String tessdata = DATAPATH + File.separator + "tessdata";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private static final String DEFAULT_LANGUAGE = "chi_sim";
    /**
     * assets中的文件名
     */
    private static final String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
    /**
     * 保存到SD卡中的完整文件名
     */
    private static final String LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;

    /**
     * 权限请求值
     */
    private static final int PERMISSION_REQUEST_CODE=0;
    private TessBaseAPI tessBaseAPI;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_orcmain);
        imageViewOrc = findViewById(R.id.m_orc_image);
        imageViewOrc.setImageBitmap(BitmapFactory.decodeResource(GlobalData.getContext().getResources(), imageFile[0]));
        buttonNext = findViewById(R.id.m_orc_button);
        buttonNext.setOnClickListener(this);
        textViewOrc = findViewById(R.id.m_orc_text);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        //Android6.0之前安装时就能复制，6.0之后要先请求权限，所以6.0以上的这个方法无用。
        copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
    }

    @Override
    public void initData(Activity activity) {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.setDebug(true);
        tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE, tessBaseAPI.OEM_TESSERACT_ONLY);
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_ONLY);
        mDialog = new ProgressDialog(ORCMainActivity.this);
        mDialog.setMessage("图片处理中......");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        imageToText(BitmapFactory.decodeResource(GlobalData.getContext().getResources(), imageFile[0]));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.m_orc_button){
            mDialog.show();
            currentId = (currentId + 1)%4;
            bitmap = ((BitmapDrawable)imageViewOrc.getDrawable()).getBitmap();
            bitmap.recycle();
            bitmap = BitmapFactory.decodeResource(GlobalData.getContext().getResources(), imageFile[currentId]);
            imageViewOrc.setImageBitmap(bitmap);
            imageToText(bitmap);
        }
    }

    private void imageToText(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long setImageStart = System.currentTimeMillis();
                ZsfLog.d(ORCMainActivity.class, "run: kaishi setImage : " + setImageStart);
                tessBaseAPI.setImage(bitmap);
                long setImageEnd = System.currentTimeMillis();
                ZsfLog.d(ORCMainActivity.class, "run: kaishi setImage : " + setImageEnd);
                ZsfLog.d(ORCMainActivity.class, "run: setImage result : " + (setImageEnd - setImageStart));

                long getImageTextStart = System.currentTimeMillis();
                ZsfLog.d(ORCMainActivity.class, "run: getText : " + getImageTextStart);
                final String text = tessBaseAPI.getUTF8Text();
                long getImageTextEnd = System.currentTimeMillis();
                ZsfLog.d(ORCMainActivity.class, "run: getText " + getImageTextEnd);
                ZsfLog.d(ORCMainActivity.class, "run: getText result : " + (getImageTextEnd - getImageTextStart));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewOrc.setText(text);
                        mDialog.dismiss();
                    }
                });
                tessBaseAPI.clear();

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tessBaseAPI.end();
    }

    /**
     * 将assets中的识别库复制到SD卡中
     * @param path  要存放在SD卡中的 完整的文件名。这里是"/storage/emulated/0//tessdata/chi_sim.traineddata"
     * @param name  assets中的文件名 这里是 "chi_sim.traineddata"
     */
    public void copyToSD(String path, String name) {
        ZsfLog.d(ORCMainActivity.class, "copyToSD: "+path);
        ZsfLog.d(ORCMainActivity.class, "copyToSD: "+name);

        //如果存在就删掉
        File f = new File(path);
        if (f.exists()){
            f.delete();
        }
        if (!f.exists()){
            File p = new File(f.getParent());
            if (!p.exists()){
                p.mkdirs();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is=null;
        OutputStream os=null;
        try {
            is = this.getAssets().open(name);
            File file = new File(path);
            os = new FileOutputStream(file);
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 请求到权限后在这里复制识别库
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ZsfLog.d(ORCMainActivity.class, "onRequestPermissionsResult: "+grantResults[0]);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ZsfLog.d(ORCMainActivity.class, "onRequestPermissionsResult: copy");
                    copyToSD(LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                }
                break;
            default:
                break;
        }
    }

}
