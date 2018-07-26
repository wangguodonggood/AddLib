package com.topotek.module.android.menu;
//基类  快速添加菜单数据列表
public abstract class SimpleMenuDataList implements Menu.MenuDataList {


    protected static final int Interface_Type_getItemType = 0;
    protected static final int Interface_Type_isSelected = 1;
    protected static final int Interface_Type_getItemText = 2;
    protected static final int Interface_Type_getChildList = 3;
    protected static final int Interface_Type_canOk = 4;

    //主要关注的是以下几个列表项
    protected static final int Interface_Type_ok = 5;
    //向 上、下、左、右 移动到响应item上的点击事件
    protected static final int Interface_Type_onMoveToItem_UP = 6;
    protected static final int Interface_Type_onMoveToItem_DOWN = 7;
    protected static final int Interface_Type_onMoveToItem_LEFT = 8;
    protected static final int Interface_Type_onMoveToItem_RIGHT = 9;

    //列表的类型 button textview radiobutton...
    protected int itemType;
    //多选 或者 单选
    protected boolean isSelected;
    //列表上的文本
    protected String itemText;

    //可以继续添加文件夹
    protected Menu.MenuDataList childList;

    //是否可以点击
    protected boolean canOk;

    @Override
    public int getItemType(int index) {
        integrationInterface(index, Interface_Type_getItemType);
        return itemType;
    }

    @Override
    public boolean isSelected(int index) {
        integrationInterface(index, Interface_Type_isSelected);
        return isSelected;
    }

    @Override
    public String getItemText(int index) {
        integrationInterface(index, Interface_Type_getItemText);
        return itemText;
    }

    @Override
    public Menu.MenuDataList getChildList(int index) {
        integrationInterface(index, Interface_Type_getChildList);
        return childList;
    }

    @Override
    public boolean canOk(int index) {
        integrationInterface(index, Interface_Type_canOk);
        return canOk;
    }

    @Override
    public void ok(int index) {
        integrationInterface(index, Interface_Type_ok);
    }

    @Override
    public void onMoveToItem(int index, int moveToItemType) {
        switch (moveToItemType){
            case Menu.MOVE_TO_ITEM_TYPE_UP:
                integrationInterface(index, Interface_Type_onMoveToItem_UP);
                break;
            case Menu.MOVE_TO_ITEM_TYPE_DOWN:
                integrationInterface(index, Interface_Type_onMoveToItem_DOWN);
                break;
            case Menu.MOVE_TO_ITEM_TYPE_LEFT:
                integrationInterface(index, Interface_Type_onMoveToItem_LEFT);
                break;
            case Menu.MOVE_TO_ITEM_TYPE_RIGHT:
                integrationInterface(index, Interface_Type_onMoveToItem_RIGHT);
                break;
        }
    }

    public abstract void integrationInterface(int index, int interfaceType);
}
