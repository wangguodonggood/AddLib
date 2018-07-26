package com.topotek.topotekmodule.uiModule;

import android.view.Gravity;
import android.widget.FrameLayout;

import com.topotek.module.android.menu.Menu;

//实现Menu的工具类
public class MenuUtils {

    public static void menu(Menu menu, FrameLayout parentLayout) {
        if (menu.getParent() == null) {
            int height = parentLayout.getHeight();
            int menuHeight = height * 2 / 3;
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, menuHeight, Gravity.CENTER_VERTICAL);
            parentLayout.addView(menu, layoutParams);
        } else
            parentLayout.removeView(menu);
    }

    public static void menuUp(Menu menu) {
        if (menu.getParent() != null)
            menu.clickUp();
    }

    public static void menuDown(Menu menu) {
        if (menu.getParent() != null)
            menu.clickDown();
    }

    public static void menuLeft(Menu menu) {
        if (menu.getParent() != null)
            menu.clickLeft();
    }

    public static void menuRight(Menu menu) {
        if (menu.getParent() != null)
            menu.clickRight();
    }

    public static void menuOk(Menu menu) {
        if (menu.getParent() != null)
            menu.clickOk();
    }
}
