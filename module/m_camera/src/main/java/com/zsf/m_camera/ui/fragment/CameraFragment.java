package com.zsf.m_camera.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.cameraview.CameraView;
import com.hw.videoprocessor.VideoProcessor;
import com.hw.videoprocessor.util.VideoProgressListener;
import com.zsf.m_camera.R;
import com.zsf.m_camera.ZLog;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.manager.IMediaTelecontroller;
import com.zsf.m_camera.manager.MediaRecorderController;
import com.zsf.m_camera.manager.ChronometerManager;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.BaseFragment;
import com.zsf.m_camera.ui.view.RecordButton;
import com.zsf.m_camera.utils.FileUtils;
import com.zsf.m_camera.manager.HandlerThreadManager;
import com.zsf.utils.ZsfLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : zsf
 * @date : 2021/1/23 3:08 PM
 * @desc :
 */
public class CameraFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, ChronometerManager.RecordListener, Runnable, RecordButton.RecordButtonListener {

    private final String TAG = "AQCJ_CameraFragment";

    private SeekBar seekBar;
    private CameraView cameraView;
    private TextView takeCancel, cameraFlash, cameraSwitch;
    private Chronometer chronometer;

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_camera_flash,
            R.drawable.ic_camera_flash_disable,
            R.drawable.ic_camera_flash_enable,
    };
    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_TORCH,
    };

    private IMediaTelecontroller telecontroller;

    private String timeTemp;

    private String tempPath;

    private int zoom = -1;

    private int lastTime;

    private double longitude = 8.8D;
    private double latitude = 8.8D;
    private float radius = 8.8F;

    private boolean currentFlash = false;

    private Bitmap bitmapPreview;

    private Handler backHandler = new Handler(HandlerThreadManager.getLooper());

    private RecordButton mRecordButton;

    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (cameraView != null) {
                try {
                    cameraView.setFlash(FLASH_OPTIONS[1]);
                    cameraView.start();
                }
                catch (Exception e){
                    cameraView.stop();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "相机打开失败！", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2021/1/23 初始化定位功能
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Bundle bundle = getArguments();
        initView(view);
        return view;
            
    }

    private void initView(View view) {
        seekBar = view.findViewById(R.id.focus_zoom);
        seekBar.setOnSeekBarChangeListener(this);
        cameraView = view.findViewById(R.id.m_camera_CameraView_camera);
        cameraView.setAdjustViewBounds(true);
        cameraView.addCallback(new CameraCallback());
        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setVisibility(View.VISIBLE);
        takeCancel = view.findViewById(R.id.m_camera_TextView_close);
        cameraFlash = view.findViewById(R.id.m_camera_TextView_flash);
        cameraSwitch = view.findViewById(R.id.m_camera_TextView_switch);
        takeCancel.setOnClickListener(this);
        cameraFlash.setOnClickListener(this);
        cameraSwitch.setOnClickListener(this);
        chronometer.setOnClickListener(this);

        cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[1], getContext().getTheme()));
        telecontroller = new MediaRecorderController(new ChronometerManager(chronometer, 11, this));
        timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

        mRecordButton = view.findViewById(R.id.m_camera_RecordButton_collector);
        mRecordButton.setRecordButtonListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHandler.sendEmptyMessage(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (telecontroller != null && telecontroller.isStart()) {
            backHandler.postDelayed(this, lastTime < 3 ? (3 - lastTime) * 1000 : 0);
        }
        cameraView.stop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (cameraView == null) {
            return;
        }
        if (!cameraView.isCameraOpened()){
            return;
        }
        if (id == R.id.m_camera_TextView_switch) {
            int facing = cameraView.getFacing();
            cameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
        } else if (id == R.id.m_camera_TextView_flash) {
            cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[currentFlash ? 1 : 2]));
            cameraView.setFlash(FLASH_OPTIONS[currentFlash ? 1 : 2]);
            currentFlash = !currentFlash;
        } else if (id == R.id.m_camera_TextView_close) {
            // TODO: 2021/1/25 退出当前页面
            getActivity().finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Camera camera = cameraView.getCamera();
        try {
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                if (zoom < 0) {
                    zoom = parameters.getZoom();
                }
                parameters.setZoom(zoom + progress);
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZsfLog.d(CameraFragment.class,"focus zoom error : %s" + e.getMessage());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartRecord() {

    }

    @Override
    public void onStopRecord(int time) {
        lastTime = time;
        if (telecontroller.isStart()) {
            telecontroller.stop();
            // TODO: 2021/1/25 预览数据
        }
    }

    @Override
    public void run() {
        if (telecontroller.isStart()) {
            telecontroller.stop();
        }
    }

    @Override
    public boolean isNeedAddStack() {
        return true;
    }

    @Override
    public void refreshStyle() {
        switchStyle(true);
    }

    @Override
    public void onClick() {
        Log.e(TAG, "onClick: ");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            if (telecontroller.isStart() && !telecontroller.stopEnable()) {
                return;
            }
            if (telecontroller.isStart()) {
                telecontroller.stop();
                ZsfLog.d(CameraFragment.class, "24 以下 photo 1");
                cameraSwitch.setVisibility(View.VISIBLE);
            } else {
                timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                tempPath = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "mp4");
                ZsfLog.d(CameraFragment.class, "24 以下 photo 2");
                cameraSwitch.setVisibility(View.GONE);
                telecontroller.startRecord(cameraView, tempPath);
            }
        } else {
            timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            tempPath = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "jpg");
            ZsfLog.d(CameraFragment.class, "24 以上 拍照完成");
            cameraView.takePicture();
        }
    }

    @Override
    public void onLongClick() {
        Log.e(TAG, "onLongClick: ");
        timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        tempPath = FileUtils.getMediaFilePath("temp_" + timeTemp.replace("_", ""), "", "mp4");
        // TODO: 2021/1/24 添加至数据存储
        ZsfLog.d(CameraFragment.class, "开始录像");
        cameraSwitch.setVisibility(View.GONE);
        telecontroller.startRecord(cameraView, tempPath);
    }

    @Override
    public void onLongClickFinish(int result) {
        Log.e(TAG, "onLongClickFinish: "+result);
        switch (result) {
            case RecordButton.NORMAL:
                Log.e(TAG, "长按结束: ");
                telecontroller.stop();
                previewVideo();
                ZsfLog.d(CameraFragment.class, "预览影片");
                cameraSwitch.setVisibility(View.VISIBLE);
                break;
            case RecordButton.RECORD_SHORT:Log.e(TAG, "录制时间过短: ");
                break;
            default:
        }
    }

    class CameraCallback extends CameraView.Callback {
        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);

        }


        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            if (data == null || data.length == 0) {
                return;
            }
            currentFlash = !currentFlash;
            cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[1]));
            cameraView.setFlash(FLASH_OPTIONS[1]);
            bitmapPreview = byteToBitmap(data);
            Bundle bundle = new Bundle();
            bundle.putString("path", tempPath);
            bundle.putString("time", timeTemp);
            bundle.putDouble("longitude", longitude);
            bundle.putDouble("latitude", latitude);
            bundle.putFloat("radius", radius);
            bundle.putInt("type", 0);
            FragmentStack.addFragment(getActivity().getSupportFragmentManager(), CameraFragment.this, EditPhotoFragment.class, 100,((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);
        }
    }

    @Override
    public void onDestroyView() {
        if (cameraView != null) {
            cameraView.stop();
        }
        if (telecontroller != null) {
            telecontroller.stop();
        }
        // TODO: 2021/1/25 百度定位反注册

        longitude = 4.9E-324D;
        latitude = 4.9E-324D;
        radius = 0.0F;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHandler.removeCallbacksAndMessages(null);
    }

    private Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getBitmapPreview() {
        if (bitmapPreview != null && !bitmapPreview.isRecycled()) {
            Bitmap copy = bitmapPreview.copy(Bitmap.Config.RGB_565, true);
            if (!bitmapPreview.isRecycled()) {
                bitmapPreview.recycle();
                bitmapPreview = null;
            }
            ZLog.d(TAG, "copy = " + copy.getByteCount()/1024);
            return copy;
        }
        return null;
    }

    private void previewVideo() {
        currentFlash = !currentFlash;
        cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[1]));
        cameraView.setFlash(FLASH_OPTIONS[1]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(tempPath);
                    int originWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int originHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                    Log.e(TAG,"originWidth="+originWidth+" originHeight=="+originHeight+" bitrate=="+bitrate);
                    final String path = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "mp4");
                    VideoProcessor.processor(getContext())
                            .input(tempPath)
                            .bitrate(bitrate / 50)
                            .output(path)
                            .progressListener(new VideoProgressListener() {
                                @Override
                                public void onProgress(float progress) {
                                    int intProgress = (int) (progress * 100);
                                    if (intProgress==100){
                                        ZLog.d(TAG, "压缩完成!");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("path", path);
                                                bundle.putString("time", timeTemp);
                                                bundle.putDouble("longitude", longitude);
                                                bundle.putDouble("latitude", latitude);
                                                bundle.putFloat("radius", radius);
                                                bundle.putInt("type", 1);
                                                FragmentStack.addFragment(getActivity().getSupportFragmentManager(), CameraFragment.this, SubmitVideoFragment.class, 100,((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);
                                            }
                                        });

                                    }
                                }
                            })
                            .process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
