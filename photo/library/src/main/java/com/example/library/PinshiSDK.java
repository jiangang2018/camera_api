package com.example.library;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinshiSDK extends AppCompatActivity {
    private static final String[] ALL_PERMISSION = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.iv_complete)
    ImageView ivComplete;
    @BindView(R2.id.rv_content)
    RecyclerView rvContent;
    @BindView(R2.id.iv_add_row)
    ImageView ivAddRow;
    @BindView(R2.id.iv_add_col)
    ImageView ivAddCol;
    @BindView(R2.id.cl_root)
    ConstraintLayout clRoot;
    @BindView(R2.id.gl_left)
    Guideline glLeft;
    @BindView(R2.id.gl_right)
    Guideline glRight;

    GridAdapter mAdapter;   //内部datas从左往右,从上往下
    private GridLayoutManager mLayoutManager;
    private int mRow = 1, mCol = 1;
    private SparseArray<ArrayList<String>> mDataMap;
    private int mClickRowIndex, mClickColIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        checkPermisstion();
        setClickListener();

        mDataMap = new SparseArray<>();
        ArrayList<String> col1List = new ArrayList<>();
        col1List.add(null);
        mDataMap.put(0, col1List);

        mLayoutManager = new GridLayoutManager(this, 1);
        rvContent.setLayoutManager(mLayoutManager);
        mAdapter = new GridAdapter(R.layout.item_img, null);
        mAdapter.bindToRecyclerView(rvContent);
        mAdapter.setNewData(map2List());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mClickRowIndex = position / mCol;
            mClickColIndex = position % mCol;
            tackPhoto();
        });

    }

    private void tackPhoto() {
        PictureSelector.create(PinshiSDK.this).openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(false)// 是否裁剪 true or false
                .cropCompressQuality(50)
                .compress(true)// 是否压缩 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getFilesDir().getAbsolutePath())//压缩图片保存地址
                .minimumCompressSize(1000)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private ArrayList<String> map2List() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mCol; j++) {
                result.add(mDataMap.get(j).get(i));
            }
        }
        return result;
    }

    private void addRow() {
        if (mRow == 3) return;
        mRow++;
        switch (mRow) {
            case 2:
                mDataMap.get(0).add(null);
                mAdapter.setNewData(map2List());
                break;

            case 3:
                mDataMap.get(0).add(null);
                mAdapter.setNewData(map2List());
                ivAddRow.setVisibility(View.GONE);
                break;
        }
    }

    private void setClickListener() {
        ivBack.setOnClickListener(v -> finish());
        ivAddRow.setOnClickListener(v -> addRow());
        ivAddCol.setOnClickListener(v -> addCol());
        ivComplete.setOnClickListener(v -> onClickComplete());
    }

    private void addCol() {
        if (mCol == 3) return;
        mCol++;
        ivAddRow.setVisibility(View.GONE);
        switch (mCol) {
            case 2:
                mLayoutManager.setSpanCount(2);
                ArrayList<String> colList2 = new ArrayList<>();
                for (int i = 0, len = mDataMap.get(0).size(); i < len; i++) {
                    colList2.add(null);
                }
                mDataMap.put(1, colList2);
                mAdapter.setNewData(map2List());
                glLeft.setGuidelinePercent(0.2f);
                glRight.setGuidelinePercent(0.8f);
                clRoot.requestLayout();
                break;

            case 3:
                mLayoutManager.setSpanCount(3);
                ArrayList<String> colList3 = new ArrayList<>();
                for (int i = 0, len = mDataMap.get(0).size(); i < len; i++) {
                    colList3.add(null);
                }
                mDataMap.put(2, colList3);
                mAdapter.setNewData(map2List());
                glLeft.setGuidelinePercent(0.1f);
                glRight.setGuidelinePercent(0.9f);
                clRoot.requestLayout();
                ivAddCol.setVisibility(View.GONE);
                break;
        }
    }

    private void checkPermisstion() {
        ActivityCompat.requestPermissions(this, ALL_PERMISSION, 1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
                    if (list == null || list.isEmpty()) return;
                    String imgPath = list.get(0).getPath();

                    mDataMap.get(mClickColIndex).set(mClickRowIndex, imgPath);
                    mAdapter.setNewData(map2List());
            }
        }
    }

    /**
     *
     * @return 返回当前所有图片路径
     */
    public ArrayList<String> getPathList() {
        ArrayList<String> list = map2List();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }

        }
        return list;
    }

    private void onClickComplete(){
        getIntent().putExtra("data", map2List());
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
