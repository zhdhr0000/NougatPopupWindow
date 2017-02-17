package com.github.zhdhr0000.NougatPopupWindow;//rewrite this line with your package.

import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhangyinghao on 2017/1/9.
 * All code refer to Android 7.0(N) and Android 7.1(N_MR1) source code
 */

public class NougatPopupWindow extends PopupWindow {

    public NougatPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (Build.VERSION.SDK_INT != 24) {
            super.showAtLocation(parent, gravity, x, y);
            return;
        }
//        if (isShowing() || mContentView == null) {
//            return;
//        }
        Object obj = getParam("mContentView");
        View mContentView = (View) obj;
        if (isShowing() || mContentView == null) {
            return;
        }

        //TransitionManager.endTransitions(mDecorView);
        obj = getParam("mDecorView");
        ViewGroup mDecorView = (ViewGroup) obj;
        //RequireAPI M but if SDK_INT != N,super.showAtLocation and returned;
        TransitionManager.endTransitions(mDecorView);

//            detachFromAnchor();
        execMethod("detachFromAnchor", new Class[]{}, new Object[]{});

//            mIsShowing = true;
        setParam("mIsShowing", true);
//            mIsDropdown = false;
        setParam("mIsDropdown", false);

//        final WindowManager.LayoutParams p = createPopupLayoutParams(parent.getWindowToken());
        obj = execMethod("createPopupLayoutParams", new Class[]{IBinder.class}, new Object[]{parent.getWindowToken()});
        final WindowManager.LayoutParams p = (WindowManager.LayoutParams) obj;
        p.gravity = computeGravity(gravity);

//        preparePopup(p);
        execMethod("preparePopup",new Class[]{WindowManager.LayoutParams.class},new Object[]{p});

        // Only override the default if some gravity was specified.
        if (gravity != Gravity.NO_GRAVITY) {
            p.gravity = gravity;
        }

        p.x = x;
        p.y = y;

//        invokePopup(p);
        execMethod("invokePopup",new Class[]{WindowManager.LayoutParams.class},new Object[]{p});
    }

    private Object getParam(String paramName) {
        if (TextUtils.isEmpty(paramName)) {
            return null;
        }
        try {
            Field field = PopupWindow.class.getDeclaredField(paramName);
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setParam(String paramName, Object obj) {
        if (TextUtils.isEmpty(paramName)) {
            return;
        }
        try {
            Field field = PopupWindow.class.getDeclaredField(paramName);
            field.setAccessible(true);
            field.set(this, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object execMethod(String methodName, Class[] cls, Object[] args) {
        if (TextUtils.isEmpty(methodName)) {
            return null;
        }
        try {
            Method method = getMethod(PopupWindow.class, methodName, cls);
            method.setAccessible(true);
            return method.invoke(this, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method getMethod(Class clazz, String methodName,
                             final Class[] classes) throws NoSuchMethodException {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }


    private int computeGravity(int mGravity) {
//        int gravity = mGravity == Gravity.NO_GRAVITY ?  Gravity.START | Gravity.TOP : mGravity;
        setParam("mGravity", mGravity);
        int gravity = mGravity == Gravity.NO_GRAVITY ? Gravity.START | Gravity.TOP : mGravity;

//        if (mIsDropdown && (mClipToScreen || mClippingEnabled)) {
//            gravity |= Gravity.DISPLAY_CLIP_VERTICAL;
//        }
        Object obj = getParam("mIsDropdown");
        boolean mIsDropdown = (boolean) obj;
        obj = getParam("mClipToScreen");
        boolean mClipToScreen = (boolean) obj;
        obj = getParam("mClippingEnabled");
        boolean mClippingEnabled = (boolean) obj;
        if (mIsDropdown && (mClipToScreen || mClippingEnabled)) {
            gravity |= Gravity.DISPLAY_CLIP_VERTICAL;
        }
        return gravity;
    }
}
