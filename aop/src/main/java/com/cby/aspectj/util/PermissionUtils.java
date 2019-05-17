package com.cby.aspectj.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import com.cby.aspectj.Aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class PermissionUtils {

    private static final List<String> PERMISSIONS = getPermissions();
    private static final String TAG = PermissionFragment.class.getSimpleName();

    private static PermissionUtils sInstance;
    private PermissionFragment mPermissionsFragment;

    public OnRationaleListener mOnRationaleListener;
    private SimpleCallback mSimpleCallback;
    private FullCallback mFullCallback;
    private Set<String> mPermissions;
    /**
     * 需要申请的权限
     */
    public List<String> mPermissionsRequest;
    /**
     * 授权的权限
     */
    private List<String> mPermissionsGranted;
    /**
     * 拒绝的权限
     */
    private List<String> mPermissionsDenied;
    /**
     * 永久拒绝的权限
     */
    private List<String> mPermissionsDeniedForever;

    /**
     * 获取应用权限
     *
     * @return 清单文件中的权限列表
     */
    public static List<String> getPermissions() {
        return getPermissions(Aop.getContext().getPackageName());
    }

    /**
     * 获取应用权限
     *
     * @param packageName 包名
     * @return 清单文件中的权限列表
     */
    public static List<String> getPermissions(final String packageName) {
        PackageManager pm = Aop.getContext().getPackageManager();
        try {
            return Arrays.asList(
                    pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                            .requestedPermissions
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 判断权限是否被授予
     *
     * @param permissions 权限
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGranted(final String... permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(Aop.getContext(), permission);
    }

    /**
     * 打开应用具体设置
     */
    public static void openAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + Aop.getContext().getPackageName()));
        Aop.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    private PermissionUtils(final String... permissions) {
        mPermissions = new LinkedHashSet<>();
        // 遍历动态请求的权限集
        for (String permission : permissions) {
            // 遍历该权限是否属于需要动态申请的权限组
            for (String aPermission : PermissionConsts.getPermissions(permission)) {
                // 判断清单文件中的权限列表是否包含该权限，存在，则添加到集合中
                if (PERMISSIONS.contains(aPermission)) {
                    mPermissions.add(aPermission);
                }
            }
        }
        sInstance = this;
    }


    public PermissionUtils(@NonNull final FragmentActivity activity) {
//        mPermissionsFragment = getLazySingleton(activity.getSupportFragmentManager());
    }

    public PermissionUtils(@NonNull final Fragment fragment) {
//        mPermissionsFragment = getLazySingleton(fragment.getChildFragmentManager());
    }

    @NonNull
//    private Lazy<PermissionFragment> getLazySingleton(@NonNull final FragmentManager fragmentManager) {
//        return new Lazy<PermissionFragment>() {
//
//            private PermissionFragment fragment;
//
//            @Override
//            public synchronized PermissionFragment get() {
//                if (fragment == null) {
//                    fragment = getPermissionsFragment(fragmentManager);
//                }
//                return fragment;
//            }
//        };
//    }
//
//    private PermissionFragment getPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
//        PermissionFragment mPermissionsFragment = findPermissionsFragment(fragmentManager);
//        boolean isNewInstance = mPermissionsFragment == null;
//        if (isNewInstance) {
//            mPermissionsFragment = new PermissionFragment();
//            fragmentManager
//                    .beginTransaction()
//                    .add(mPermissionsFragment, TAG)
//                    .commitNow();
//        }
//        return mPermissionsFragment;
//    }
//
//    private PermissionFragment findPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
//        return (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
//    }


    /**
     * 设置请求权限
     *
     * @param permissions 要请求的权限
     * @return {@link PermissionUtils}
     */
    public static PermissionUtils permission(@PermissionConsts.Permissions() final String... permissions) {
        return new PermissionUtils(permissions);
    }

    /**
     * 设置拒绝权限后再次请求的回调接口
     *
     * @param listener 拒绝权限后再次请求的回调接口
     * @return {@link PermissionUtils}
     */
    PermissionUtils rationale(final OnRationaleListener listener) {
        mOnRationaleListener = listener;
        return this;
    }

    /**
     * 设置回调
     *
     * @param callback 简单回调接口
     * @return {@link PermissionUtils}
     */
    public PermissionUtils callback(final SimpleCallback callback) {
        mSimpleCallback = callback;
        return this;
    }

    /**
     * 设置回调
     *
     * @param callback 完整回调接口
     * @return {@link PermissionUtils}
     */
    public PermissionUtils callback(final FullCallback callback) {
        mFullCallback = callback;
        return this;
    }


    public void request(@NonNull final FragmentActivity activity) {
        request(activity.getSupportFragmentManager());
    }

    public void request(@NonNull final Fragment fragment) {
        request(fragment.getChildFragmentManager());
    }

    /**
     * 开始请求
     */
    public void request(FragmentManager fragmentManager) {
        mPermissionsGranted = new ArrayList<>();
        mPermissionsRequest = new ArrayList<>();
        // 判断 SDK 版本 【23（Android 6.0 系统）】 ==> 小于 23：不需要动态权限申请；大于 23：要动态权限申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionsGranted.addAll(mPermissions);
            requestCallback();
        } else {
            // 遍历权限列表，检测是否已授权 ==> 已授权：添加到已授权权限列表；未授权：添加到需要申请的权限列表
            for (String permission : mPermissions) {
                if (isGranted(permission)) {
                    mPermissionsGranted.add(permission);
                } else {
                    mPermissionsRequest.add(permission);
                }
            }
            // 判断需要申请的权限列表是否为空，如果不为空，前往申请权限
            if (mPermissionsRequest.isEmpty()) {
                requestCallback();
            } else {
                startPermissionFragment(fragmentManager);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startPermissionFragment(FragmentManager fragmentManager) {
        // 初始化拒绝、永久拒绝的权限集合
        mPermissionsDenied = new ArrayList<>();
        mPermissionsDeniedForever = new ArrayList<>();
        if (sInstance.mPermissionsRequest != null) {
            int size = sInstance.mPermissionsRequest.size();
            // TODO: 2019/5/17 请求权限
            if (mPermissionsFragment == null) {
                mPermissionsFragment = new PermissionFragment();
                fragmentManager
                        .beginTransaction()
                        .add(mPermissionsFragment, TAG)
                        .commitNow();
            }
            mPermissionsFragment.requestPermissions(sInstance.mPermissionsRequest.toArray(new String[size]));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean rationale(final Activity activity) {
        boolean isRationale = false;
        if (mOnRationaleListener != null) {
            for (String permission : mPermissionsRequest) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    getPermissionsStatus(activity);
                    mOnRationaleListener.rationale(again -> {
                        if (again) {
//                            startPermissionActivity();
                        } else {
                            requestCallback();
                        }
                    });
                    isRationale = true;
                    break;
                }
            }
            mOnRationaleListener = null;
        }
        return isRationale;
    }

    private void getPermissionsStatus(final Activity activity) {
        for (String permission : mPermissionsRequest) {
            if (isGranted(permission)) {
                mPermissionsGranted.add(permission);
            } else {
                mPermissionsDenied.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    private void requestCallback() {
        if (mSimpleCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mSimpleCallback.onGranted();
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mSimpleCallback.onDenied();
                }
            }
            mSimpleCallback = null;
        }
        if (mFullCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mFullCallback.onGranted(mPermissionsGranted);
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mFullCallback.onDenied(mPermissionsDeniedForever, mPermissionsDenied);
                }
            }
            mFullCallback = null;
        }
        mOnRationaleListener = null;
    }

    private void onRequestPermissionsResult(final Activity activity) {
        getPermissionsStatus(activity);
        requestCallback();
    }

    public interface OnRationaleListener {

        void rationale(ShouldRequest shouldRequest);

        interface ShouldRequest {
            /**
             * 是否需要重新请求权限
             *
             * @param again
             */
            void again(boolean again);
        }
    }

    /**
     * 简单的权限申请回调
     */
    public interface SimpleCallback {
        /**
         * 权限申请成功
         */
        void onGranted();

        /**
         * 权限申请被拒绝
         */
        void onDenied();
    }

    public interface FullCallback {
        void onGranted(List<String> permissionsGranted);

        void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied);
    }

    /**
     * 权限申请被拒绝的监听
     */
    public interface OnPermissionDeniedListener {
        /**
         * 权限申请被拒绝
         */
        void onDenied(List<String> permissionsDenied);
    }

    /**
     * 打开APP的通知权限设置界面
     *
     * @param activity
     */
    private static void openAppNotificationSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", activity.getPackageName());
        intent.putExtra("app_uid", activity.getApplicationInfo().uid);
        activity.startActivity(intent);
    }

    /**
     * @Desc 请求权限的 Fragment
     * @Author cby
     * @Time 2019/5/17 10:39
     */
    public static class PermissionFragment extends Fragment {
        private static final int PERMISSIONS_REQUEST_CODE = 42;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @TargetApi(Build.VERSION_CODES.M)
        void requestPermissions(@NonNull String[] permissions) {
            if (sInstance.mPermissionsRequest != null) {
                requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode != PERMISSIONS_REQUEST_CODE) return;

            // 判断是否显示请求权限的理由
            final FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity == null) {
                throw new IllegalStateException("This fragment must be attached to an activity.");
            }
            sInstance.onRequestPermissionsResult(fragmentActivity);
        }
    }
}