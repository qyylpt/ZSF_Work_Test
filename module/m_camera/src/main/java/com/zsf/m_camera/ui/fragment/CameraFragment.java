package com.zsf.m_camera.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.zsf.m_camera.R;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.manager.IMediaTelecontroller;
import com.zsf.m_camera.manager.MediaRecorderController;
import com.zsf.m_camera.manager.ChronometerManager;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.BaseFragment;
import com.zsf.m_camera.utils.FileUtils;
import com.zsf.m_camera.manager.HandlerThreadManager;
import com.zsf.utils.ZsfLog;

import java.io.ByteArrayInputStream;
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
public class CameraFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, ChronometerManager.RecordListener, Runnable {

    public static final int PHOTO_TYPE = 0;
    public static final int VIDEO_TYPE = 1;

    private SeekBar seekBar;
    private CameraView cameraView;
    private TextView takePhoto, takeVideo, videoControl, takeCancel, cameraFlash, cameraSwitch;
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

    private String path;

    private int currentFlash = 0;

    private boolean isPhoto = false;

    private int zoom = -1;

    private int lastTime;

    private double longitude = 8.8D;
    private double latitude = 8.8D;
    private float radius = 8.8F;

    private boolean isPreview = false;

    private Bitmap bitmapPreview;

    private Handler backHandler = new Handler(HandlerThreadManager.getLooper());

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (cameraView != null) {
                try {
                    cameraView.setFlash(FLASH_OPTIONS[currentFlash]);
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
        // TODO: 2021/1/24 视频 or 照片判断
        isPhoto = true;
        initView(view, isPhoto);
        return view;
            
    }

    private void initView(View view, boolean isPhoto) {
        seekBar = view.findViewById(R.id.focus_zoom);
        seekBar.setOnSeekBarChangeListener(this);
        cameraView = view.findViewById(R.id.m_camera_CameraView_camera);
        cameraView.setAdjustViewBounds(true);
        cameraView.addCallback(new CameraCallback());
        takePhoto = view.findViewById(R.id.m_camera_TextView_take_photo);
        takeVideo = view.findViewById(R.id.m_camera_TextView_take_video);
        videoControl = view.findViewById(R.id.m_camera_TextView_video_control);
        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setVisibility(isPhoto ? View.GONE : View.VISIBLE);
        takeCancel = view.findViewById(R.id.m_camera_TextView_close);
        cameraFlash = view.findViewById(R.id.m_camera_TextView_flash);
        cameraSwitch = view.findViewById(R.id.m_camera_TextView_switch);
        takeCancel.setOnClickListener(this);
        cameraFlash.setOnClickListener(this);
        cameraSwitch.setOnClickListener(this);
        chronometer.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        takeVideo.setOnClickListener(this);
        videoControl.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && !isPhoto) {
            takePhoto.setVisibility(View.VISIBLE);
            takePhoto.setBackgroundResource(R.drawable.ic_video_button);
            takeVideo.setVisibility(View.INVISIBLE);
            videoControl.setVisibility(View.INVISIBLE);
        }
        takePhoto.setBackgroundResource(R.drawable.ic_camera_button);
        takePhoto.setVisibility(isPhoto ? View.VISIBLE : View.GONE);
        takeVideo.setVisibility(isPhoto ? View.INVISIBLE : View.VISIBLE);
        videoControl.setVisibility(isPhoto ? View.INVISIBLE : View.VISIBLE);

        if (!isPhoto) {
            telecontroller = new MediaRecorderController(new ChronometerManager(chronometer, 11, this));
            // TODO: 2021/1/24 替换全局上下文
            videoControl.setBackground(getContext().getResources().getDrawable(telecontroller.isResume() ? R.drawable.ic_video_pause : R.drawable.ic_video_default, getContext().getTheme()));
            takeVideo.setBackground(getContext().getResources().getDrawable(telecontroller.isStart() ? R.drawable.ic_video_stop : R.drawable.ic_video_button, getContext().getTheme()));
            cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[1], getContext().getTheme()));
        } else {
            cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[0], getContext().getTheme()));
        }
        timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        isPreview = false;
        uiHandler.sendEmptyMessage(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isPhoto) {
            currentFlash = 0;
        } else {
            currentFlash = 1;
        }
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
        if (id == R.id.m_camera_TextView_take_video){
            if (telecontroller.isStart() && !telecontroller.stopEnable()) {
                ZsfLog.d(CameraFragment.class, "录像 return");
                return;
            }
            if (telecontroller.isStart()) {
                telecontroller.stop();
                // TODO: 2021/1/24 预览影片
                ZsfLog.d(CameraFragment.class, "预览影片");
                cameraSwitch.setVisibility(View.VISIBLE);
            } else {
                timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                path = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "mp4");
                // TODO: 2021/1/24 添加至数据存储
                ZsfLog.d(CameraFragment.class, "开始录像");
                cameraSwitch.setVisibility(View.GONE);
                telecontroller.startRecord(cameraView, path);
            }
            videoControl.setBackground(getContext().getResources().getDrawable(telecontroller.isResume() ? R.drawable.ic_video_pause : R.drawable.ic_video_default));
            takeVideo.setBackground(getContext().getResources().getDrawable(telecontroller.isStart() ? R.drawable.ic_video_stop : R.drawable.ic_video_button));
        } else if (id == R.id.m_camera_TextView_switch) {
            int facing = cameraView.getFacing();
            cameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
        } else if (id == R.id.m_camera_TextView_flash) {
            currentFlash = (currentFlash + 1) % FLASH_OPTIONS.length;
            if (!isPhoto && currentFlash == 0) {
                currentFlash = 1;
            }
            cameraFlash.setBackground(getContext().getResources().getDrawable(FLASH_ICONS[currentFlash]));
            cameraView.setFlash(FLASH_OPTIONS[currentFlash]);
        } else if (id == R.id.m_camera_TextView_video_control) {
            if (!telecontroller.isStart() || !telecontroller.stopEnable()) {
                return;
            }
            if (telecontroller.isResume()) {
                telecontroller.pause();
            } else {
                telecontroller.resume();
            }
            videoControl.setBackground(getContext().getResources().getDrawable(telecontroller.isResume() ? R.drawable.ic_video_pause : R.drawable.ic_video_resume));
        } else if (id == R.id.m_camera_TextView_take_photo) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && !isPhoto) {
                if (telecontroller.isStart() && !telecontroller.stopEnable()) {
                    return;
                }
                if (telecontroller.isStart()) {
                    telecontroller.stop();
                    // TODO: 2021/1/25  预览
                    ZsfLog.d(CameraFragment.class, "24 以下 photo 1");
                    cameraSwitch.setVisibility(View.VISIBLE);
                } else {
                    timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                    path = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "mp4");
                    // TODO: 2021/1/25 添加至数据存储
                    ZsfLog.d(CameraFragment.class, "24 以下 photo 2");
                    cameraSwitch.setVisibility(View.GONE);
                    telecontroller.startRecord(cameraView, path);
                }
                takePhoto.setBackground(getContext().getResources().getDrawable(telecontroller.isStart() ? R.drawable.ic_video_stop : R.drawable.ic_video_button));
            } else {
                timeTemp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                path = FileUtils.getMediaFilePath(timeTemp.replace("_", ""), "", "jpg");
                // TODO: 2021/1/25 添加至数据存储
                ZsfLog.d(CameraFragment.class, "24 以上 拍照完成");
                cameraView.takePicture();
            }
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
        videoControl.setBackground(getContext().getResources().getDrawable(telecontroller.isResume() ? R.drawable.ic_video_pause : R.drawable.ic_video_default));
        takePhoto.setBackground(getContext().getResources().getDrawable(telecontroller.isStart() ? R.drawable.ic_video_stop : R.drawable.ic_video_button));
    }

    @Override
    public void run() {
        if (telecontroller.isStart()) {
            telecontroller.stop();
        }
    }

    @Override
    public boolean isNeedAddStack() {
        return false;
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
            isPreview = true;
            if (data == null || data.length == 0) {
                return;
            }
            bitmapPreview = byteToBitmap(data);
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            bundle.putString("time", timeTemp);
            bundle.putDouble("longitude", longitude);
            bundle.putDouble("latitude", latitude);
            bundle.putFloat("radius", radius);
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
    }

    private Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
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
            Bitmap copy = bitmapPreview.copy(Bitmap.Config.ARGB_8888, true);
            if (!bitmapPreview.isRecycled()) {
                bitmapPreview.recycle();
                bitmapPreview = null;
            }
            return copy;
        }
        return null;
    }
}
