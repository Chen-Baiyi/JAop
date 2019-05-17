//package com.cby.aspectj.util;
//
//
//import android.annotation.TargetApi;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.lang.annotation.Target;
//
///**
// * @Desc 描述
// * @Author cby
// * @Time 2019/5/17 10:39
// */
//public class PermissionFragment extends Fragment {
//    private static final int PERMISSIONS_REQUEST_CODE = 42;
//    private PermissionUtils mPermissionUtils;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    void requestPermissions(PermissionUtils mPermissionUtils, @NonNull String[] permissions) {
//        this.mPermissionUtils = mPermissionUtils;
//        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
//
//        // 判断是否显示请求权限的理由
//        final FragmentActivity fragmentActivity = getActivity();
//        if (fragmentActivity == null) {
//            throw new IllegalStateException("This fragment must be attached to an activity.");
//        }
//        if (mPermissionUtils.rationale(fragmentActivity)) {
//            // TODO: 2019/5/17
//            return;
//        }
//        if (mPermissionUtils.mPermissionsRequest != null) {
//            int size = mPermissionUtils.mPermissionsRequest.size();
//            requestPermissions(mPermissionUtils.mPermissionsRequest.toArray(new String[size]), 1);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    boolean isGranted(String permission) {
//        final FragmentActivity fragmentActivity = getActivity();
//        if (fragmentActivity == null) {
//            throw new IllegalStateException("This fragment must be attached to an activity.");
//        }
//        return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    boolean isRevoked(String permission) {
//        final FragmentActivity fragmentActivity = getActivity();
//        if (fragmentActivity == null) {
//            throw new IllegalStateException("This fragment must be attached to an activity.");
//        }
//        return fragmentActivity.getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
//    }
//}
