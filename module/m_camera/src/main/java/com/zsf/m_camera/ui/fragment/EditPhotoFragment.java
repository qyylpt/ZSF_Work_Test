package com.zsf.m_camera.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nanchen.compresshelper.CompressHelper;
import com.zsf.m_camera.R;
import com.zsf.m_camera.ZLog;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.ui.BaseFragment;
import com.zsf.m_camera.ui.activity.CameraActivity;
import com.zsf.m_camera.ui.view.CustomPaintView;
import com.zsf.utils.ZsfLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : zsf
 * @date : 2021/1/26 9:47 AM
 * @desc :
 */
public class EditPhotoFragment extends BaseFragment implements View.OnClickListener, CustomPaintView.OnUndoListener {

    private final String TAG = "AQCJ_EditPhotoFragment";

    private ImageView photoPreview;
    private CustomPaintView addTagView;
    private TextView undoView, photoCancel, photoSelect;
    private View progressView;
    private Bitmap bitmap;
    private String tempPath;
    private String filePath;
    private String time;
    private String fPath;

    private double longitude = 4.9E-324D;
    private double latitude = 4.9E-324D;
    private float radius = 0.0F;

    private final String EDIT_UNDO = "undo";
    private final String EDIT_DRAW = "draw";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        CameraFragment cameraFragment = (CameraFragment) getTargetFragment();
        initView(view);
        if (cameraFragment != null) {
            bitmap = cameraFragment.getBitmapPreview();
            if (bitmap != null) {
                addWatermark(bitmap);
                photoPreview.setImageBitmap(bitmap);
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            tempPath = bundle.getString("path");
            time = bundle.getString("time");
            int length = tempPath.length();
            String[] paths = tempPath.split("/");
            int pathsSize = paths.length;
            fPath = tempPath.substring(0, length - (paths[pathsSize - 1]).length());
            filePath = fPath + time + ".jpeg";
            ZLog.d(TAG, "filePath = " + filePath);
            longitude = bundle.getDouble("longitude");
            latitude = bundle.getDouble("latitude");
            radius = bundle.getFloat("radius");
            ZsfLog.d(EditPhotoFragment.class, "path = " + tempPath + "; time = " + time + "; longitude = " + longitude + "; latitude = " + latitude + "; radius = " + radius);
        }
        return view;
    }



    private void initView(View view) {
        photoPreview = view.findViewById(R.id.m_camera_ImageView_photo_preview);
        addTagView = view.findViewById(R.id.m_camera_CustomPaintView_tag);
        addTagView.setUndoListener(this);
        progressView = view.findViewById(R.id.m_camera_ProgressBar_save);
        photoCancel = view.findViewById(R.id.m_camera_TextView_edit_close);
        photoCancel.setOnClickListener(this);
        photoSelect = view.findViewById(R.id.m_camera_TextView_save);
        photoSelect.setOnClickListener(this);
        undoView = view.findViewById(R.id.m_camera_TextView_undo);
        undoView.setOnClickListener(this);
        undoView.setTag(EDIT_DRAW);
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_camera_TextView_edit_close) {
            addTagView.cancel();
            back();
        } else if (id == R.id.m_camera_TextView_save) {
            if (bitmap != null) {
                if (progressView.getVisibility() == View.VISIBLE || bitmap.isRecycled()) {
                    return;
                }
                progressView.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Matrix touchMatrix = photoPreview.getImageMatrix();
                            addTagView.save(bitmap, touchMatrix);
                            write(Bitmap2Bytes(bitmap));
                            new CompressHelper.Builder(getContext())
                                    .setMaxWidth(720)  // 默认最大宽度为720
                                    .setMaxHeight(960) // 默认最大高度为960
                                    .setQuality(80)    // 默认压缩质量为80
                                    .setFileName(time) // 设置你需要修改的文件名
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                                    .setDestinationDirectoryPath(fPath)
                                    .build()
                                    .compressToFile(new File(tempPath));
                            File file = new File(tempPath);
                            if (file.exists()) {
                                file.delete();
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressView.setVisibility(View.GONE);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("path", filePath);
                                    bundle.putString("time", time);
                                    bundle.putDouble("longitude", longitude);
                                    bundle.putDouble("latitude", latitude);
                                    bundle.putFloat("radius", radius);
                                    FragmentStack.addFragment(getActivity().getSupportFragmentManager(), EditPhotoFragment.this, SubmitPhotoFragment.class, 101, ((CameraActivity)getActivity()).getFragmentContainer(), bundle);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else if (id == R.id.m_camera_TextView_undo) {
            String tag = (String) v.getTag();
            if (TextUtils.isEmpty(tag)) {
                return;
            }
            if (EDIT_UNDO.equals(tag) && addTagView != null && addTagView.isUndoEnable()) {
                addTagView.undo();
            }
            if (EDIT_DRAW.equals(tag)) {
                addTagView.setVisibility(View.VISIBLE);
                undoView.setBackground(getResources().getDrawable(R.drawable.ic_redo_normal));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void OnUndo(boolean isUndo) {
        if (undoView != null) {
            undoView.setBackground(getResources().getDrawable(isUndo ? R.drawable.ic_redo_active : R.drawable.ic_draw));
            undoView.setTag(isUndo ? EDIT_UNDO : EDIT_DRAW);
            addTagView.setVisibility(isUndo ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        tempPath = null;
        filePath = null;
        longitude = 4.9E-324D;
        latitude = 4.9E-324D;
        radius = 0.0F;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // 写到sdcard中
    public void write(byte[] bs) throws IOException {
        if (bs == null) {
            return;
        }
        File file = new File(tempPath);
        if (!file.exists()) {
            if (file.getParentFile().mkdirs()) {
                file.createNewFile();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bs);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void addWatermark(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        // TODO: 2021/2/23 内容动态加载 
        String watermark = "这里是水印！！！！";
        if (TextUtils.isEmpty(watermark)) {
            ZsfLog.d(EditPhotoFragment.class, "watermark is empty!");
            return;
        }
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float scaledDensity = displayMetrics.scaledDensity;
        // TODO: 2021/2/23 字体动态加载
        paint.setTextSize(16 * scaledDensity);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.parseColor("#E6E6E6"));
        // TODO: 2021/2/23 透明度动态加载
        paint.setAlpha(100);
        canvas.setDrawFilter(filter);
        int w = (int) paint.measureText(watermark) + 30;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Path path = new Path();

        // TODO: 2021/2/23 是否全屏
        boolean isFullScreen = true;

        // TODO: 2021/2/23 角度动态调节

        int intervalX = width / 5;
        int intervalY = height / 5;
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 20; j++) {
            builder.append(watermark);
        }
        // 左上
        for (int i = 1; i <= 5; i++) {
            if (!isFullScreen && i != 2) {
                continue;
            }
            path.moveTo(0, intervalY * i);
            path.lineTo(intervalX * i,0);
            double z = Math.hypot(intervalX * i, intervalY * i);
            int length = paint.breakText(builder.toString().toCharArray(), 0, builder.length(), (int)z, null);
            canvas.drawTextOnPath(builder.toString().toCharArray(),0, length + 1, path, 0, 0, paint);
            path.reset();
        }
        // 右下
        for (int i = 1; i <= 4; i++) {
            if (!isFullScreen && i != 2) {
                continue;
            }
            path.moveTo(width - intervalX * i, height);
            path.lineTo(width,height - intervalY * i);
            double z = Math.hypot(intervalX * i, intervalY * i);
            int length = paint.breakText(builder.toString().toCharArray(), 0, builder.length(), (int)z, null);
            canvas.drawTextOnPath(builder.toString().toCharArray(),0, length + 1, path, 0, 0, paint);
            path.reset();
        }
    }

    @Override
    public void back() {
        File tempFilePath = new File(tempPath);
        if (tempFilePath.exists()) {
            tempFilePath.delete();
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        getActivity().getSupportFragmentManager().popBackStack(MainFragment.class.getSimpleName(), 0);
        MainFragment mainFragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName());
        if (mainFragment != null) {
            mainFragment.refreshStyle();
        }
    }
}
