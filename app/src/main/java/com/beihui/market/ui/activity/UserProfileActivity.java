package com.beihui.market.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.FileProviderHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerUserProfileComponent;
import com.beihui.market.injection.module.UserProfileModule;
import com.beihui.market.ui.contract.UserProfileContract;
import com.beihui.market.ui.presenter.UserProfilePresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UserProfileActivity extends BaseComponentActivity implements UserProfileContract.View, View.OnClickListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    ImageView avatarIv;
    @BindView(R.id.nick_name)
    TextView nickNameTv;
    @BindView(R.id.phone)
    TextView phoneTv;
    @BindView(R.id.profession)
    TextView professionTv;

    @Inject
    UserProfilePresenter presenter;

    private Dialog avatarSelector;

    private String avatarFilePath;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerUserProfileComponent.builder()
                .appComponent(appComponent)
                .userProfileModule(new UserProfileModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap avatar = null;
            String path = null;
            if (requestCode == 1) {
                //相机
                path = avatarFilePath;
            } else if (requestCode == 2) {
                //相册
                path = getRealPathFromURI(data.getData());
            }
            if (path != null) {
                avatar = ImageUtils.getFixedBitmap(path, 512);
            }
            if (avatar != null) {
                presenter.updateAvatar(avatar);
            } else {
                ToastUtils.showShort(this, "头像解析错误", null);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.avatar_item, R.id.nick_name_item, R.id.profession_item})
    void OnItemsClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar_item:
                showAvatarSelector();
                break;
            case R.id.nick_name_item:
                Intent toEditName = new Intent(this, EditNickNameActivity.class);
                startActivity(toEditName);
                break;
            case R.id.profession_item:
                Intent toEditJob = new Intent(this, EditJobGroupActivity.class);
                startActivity(toEditJob);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (avatarSelector != null) {
            avatarSelector.dismiss();
        }
        switch (v.getId()) {
            case R.id.from_camera:
                UserProfileActivityPermissionsDispatcher.openCameraWithCheck(this);
                break;
            case R.id.from_album:
                UserProfileActivityPermissionsDispatcher.openAlbumWithCheck(this);
                break;
        }
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        Intent toCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String dirPath = FileProviderHelper.getTempDirPath(this);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            LogUtils.i(this.getClass().getSimpleName(), "result of mkdirs " + result);
        }
        String filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
        File avatarFile = new File(filePath);
        if (!avatarFile.exists()) {
            try {
                boolean result = avatarFile.createNewFile();
                LogUtils.i(this.getClass().getSimpleName(), "create new avatar file result " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (avatarFile.exists()) {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    uri = FileProvider.getUriForFile(this, FileProviderHelper.getFileProvider(this), avatarFile);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                toCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(avatarFile);
            }
            avatarFilePath = avatarFile.getPath();
            if (uri != null) {
                toCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(toCamera, 1);
            } else {
                ToastUtils.showShort(this, "创建头像文件失败", null);
            }
        } else {
            ToastUtils.showShort(this, "创建头像文件失败", null);
        }
    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbum() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, 2);
    }

    String getRealPathFromURI(Uri uri) {
        if (uri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    String path = cursor.getString(idx);
                    cursor.close();
                    return path;
                }
            } else {
                return uri.getPath();
            }
        }
        return null;
    }

    private void showAvatarSelector() {
        avatarSelector = new Dialog(this, R.style.AvatarSelectorStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar_selector, null);
        view.findViewById(R.id.from_camera).setOnClickListener(this);
        view.findViewById(R.id.from_album).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        avatarSelector.setContentView(view);
        avatarSelector.setCanceledOnTouchOutside(true);
        Window window = avatarSelector.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
        }
        avatarSelector.show();
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showProfile(UserHelper.Profile profile) {
        if (profile.getHeadPortrait() != null) {
            Glide.with(this)
                    .load(profile.getHeadPortrait())
                    .asBitmap()
                    .into(avatarIv);
        }
        if (profile.getUserName() != null) {
            nickNameTv.setText(profile.getUserName());
        }
        if (profile.getAccount() != null) {
            phoneTv.setText(CommonUtils.phone2Username(profile.getAccount()));
        }
        if (profile.getProfession() != null) {
            professionTv.setText(profile.getProfession());
        }
    }

    @Override
    public void showAvatarUpdateSuccess(String avatar) {
        dismissProgress();
        if (avatar != null) {
            Glide.with(this)
                    .load(avatar)
                    .asBitmap()
                    .into(avatarIv);
        }
    }
}
