package yj.me.express.util.permission;

public interface PermissionResult {
    void onGranted();

    void onDenied();
}
