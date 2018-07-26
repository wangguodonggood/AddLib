package com.topotek.module.android.menu;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.LinkedList;
//基类  主菜单布局和监听的实现
public class Menu extends LinearLayout {

    public static final int LIST_ITEM_TYPE_Folder = 0;
    public static final int LIST_ITEM_TYPE_Button = 1;
    public static final int LIST_ITEM_TYPE_RadioButton = 2;
    public static final int LIST_ITEM_TYPE_CheckBox = 3;
    public static final int LIST_ITEM_TYPE_TextView = 4;

    public static final int MOVE_TO_ITEM_TYPE_UP = 0;
    public static final int MOVE_TO_ITEM_TYPE_DOWN = 1;
    public static final int MOVE_TO_ITEM_TYPE_LEFT = 2;
    public static final int MOVE_TO_ITEM_TYPE_RIGHT = 3;

    private static final int menuBackgroundColor = Color.TRANSPARENT;
    private static final int listBackgroundColor = Color.parseColor("#44000000");
    private static final int listItemBackgroundColor = Color.parseColor("#66FFFFFF");
    private static final int selectColor = Color.RED;
    private static final int xNumber = 3;//>=3
    private static final int yNumber = 6;//>=1
    private static final int listMargin = 10;
    private static final int itemMargin = 10;
    private static final int selectMargin = 30;

    private float onTouchEventDownX = -1F;
    private float onTouchEventDownY = -1F;

    private int nowMenuX = 0;
    private MenuDataList nowMenuDataList;
    private int nowListLayoutY = 0;
    private int nowDataListIndex = 0;
    private int childListLayoutY;
    private int childDataListIndex;
    private LinkedList<MenuDataList> mLinkedList = new LinkedList<MenuDataList>();
    private LinkedList<Integer> listLayoutYLinkedList = new LinkedList<Integer>();
    private LinkedList<Integer> dataListIndexLinkedList = new LinkedList<Integer>();

    private Menu(Context context, MenuDataList menuDataList) {
        super(context);

        nowMenuDataList = menuDataList;

        initView();
    }

    public static Menu createMenu(Context context, MenuDataList menuDataList){
        return new Menu(context, menuDataList);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                onTouchEventDownX = event.getX();
                onTouchEventDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(onTouchEventDownX < 0 || onTouchEventDownY < 0)
                    break;
                float x = event.getX();
                float y = event.getY();
                float horizontal = x - onTouchEventDownX;
                float vertical = y - onTouchEventDownY;
                horizontal = horizontal * horizontal;
                vertical = vertical * vertical;
                int height = getHeight();
                int width = getWidth();
                int i = height > width ? height : width;
                if(horizontal + vertical < i / 3){
                    clickOk();
                }else if(horizontal > vertical){
                    if(x > onTouchEventDownX)
                        clickRight();
                    else
                        clickLeft();
                }else {
                    if(y > onTouchEventDownY)
                        clickDown();
                    else
                        clickUp();
                }
                onTouchEventDownX = -1;
                onTouchEventDownY = -1;
                break;
        }
        return true;
    }

    private static TextView createListItemView(Context context, MenuDataList menuDataList, int index){

        TextView listItemView = null;

        int itemType = menuDataList.getItemType(index);
        String itemText = menuDataList.getItemText(index);
        switch (itemType){
            case LIST_ITEM_TYPE_Folder:
                listItemView = new TextView(context);
                listItemView.setGravity(Gravity.CENTER_VERTICAL);
                listItemView.setPadding(25, 0, 0, 0);//100
                listItemView.setText("◤  " + itemText);
                break;
            case LIST_ITEM_TYPE_TextView:
                listItemView = new TextView(context);
                listItemView.setPadding(25, 0, 0, 0);
                listItemView.setText("◇  " + itemText);
                listItemView.setGravity(Gravity.CENTER_VERTICAL);
                break;
            case LIST_ITEM_TYPE_Button:
                listItemView = new TextView(context);
                listItemView.setPadding(25, 0, 0, 0);
                listItemView.setText("▷  " + itemText);
                listItemView.setGravity(Gravity.CENTER_VERTICAL);
                break;
            default:
                boolean selected = menuDataList.isSelected(index);
                switch (itemType){
                    case LIST_ITEM_TYPE_RadioButton:
                        RadioButton radioButton = new RadioButton(context);
                        listItemView = radioButton;
                        radioButton.setChecked(selected);
                        break;
                    case LIST_ITEM_TYPE_CheckBox:
                        CheckBox checkBox = new CheckBox(context);
                        listItemView = checkBox;
                        checkBox.setChecked(selected);
                        break;
                }
                listItemView.setText(itemText);
                break;
        }
        listItemView.setSingleLine();
        listItemView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        listItemView.setMarqueeRepeatLimit(1);
        listItemView.setTextSize(20);
        listItemView.setTextColor(Color.BLACK);
        listItemView.setBackgroundColor(listItemBackgroundColor);

        return listItemView;
    }

    private static void addListItemView(LinearLayout listLayout, TextView listItemView, boolean isAddToListLayoutHead){
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1F);
        layoutParams.setMargins(itemMargin, itemMargin, itemMargin, 0);
        if(isAddToListLayoutHead)
            listLayout.addView(listItemView, 0, layoutParams);
        else
            listLayout.addView(listItemView, layoutParams);
    }

    private static void formatListLayout(LinearLayout listLayout, MenuDataList menuDataList, int onViewY, int onDataIndex){
        listLayout.removeAllViews();
        Context context = listLayout.getContext();
        float weightSum = listLayout.getWeightSum();
        int listSize = menuDataList.getListSize();
        for(int i = 1; i <= weightSum && i <= listSize; i++){
            TextView listItemView = createListItemView(context, menuDataList, onDataIndex - onViewY + i - 1);
            addListItemView(listLayout, listItemView, false);
        }
    }

    private static LinearLayout createListLayout(Context context, MenuDataList menuDataList, float weightSum, int onViewY, int onDataIndex){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setWeightSum(weightSum);
        linearLayout.setPadding(0, 0, 0, itemMargin);
        linearLayout.setBackgroundColor(listBackgroundColor);
        formatListLayout(linearLayout, menuDataList, onViewY, onDataIndex);
        return linearLayout;
    }

    private static void addListLayout(LinearLayout menu, LinearLayout listLayout, boolean isAddToMenuHead){
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1F);
        layoutParams.setMargins(listMargin, listMargin, 0, listMargin);
        if(isAddToMenuHead)
            menu.addView(listLayout, 0, layoutParams);
        else
            menu.addView(listLayout, layoutParams);
    }

    private void selectItem(LinearLayout listLayout, int index){
        View listItemView = listLayout.getChildAt(index);
//        LayoutParams layoutParams = (LayoutParams) listItemView.getLayoutParams();
//        layoutParams.setMargins(selectMargin, selectMargin, selectMargin, selectMargin);
//        listItemView.setLayoutParams(layoutParams);
        listItemView.setBackgroundColor(selectColor);
        listItemView.setSelected(true);
    }

    private void cancelSelectItem(LinearLayout listLayout, int index){
        View listItemView = listLayout.getChildAt(index);
//        LayoutParams layoutParams = (LayoutParams) listItemView.getLayoutParams();
//        layoutParams.setMargins(itemMargin, itemMargin, itemMargin, 0);
//        listItemView.setLayoutParams(layoutParams);
        listItemView.setBackgroundColor(listItemBackgroundColor);
        listItemView.setSelected(false);
    }

    private void initView(){
        setOrientation(HORIZONTAL);
        setWeightSum(xNumber);
        setPadding(0, 0, listMargin, 0);
        setBackgroundColor(menuBackgroundColor);

        LinearLayout nowListLayout = createListLayout(getContext(), nowMenuDataList, yNumber, 0, 0);
        addListLayout(this, nowListLayout, false);
        selectItem(nowListLayout, 0);

        int itemType = nowMenuDataList.getItemType(0);
        if(itemType == LIST_ITEM_TYPE_Folder){
            MenuDataList childList = nowMenuDataList.getChildList(0);
            LinearLayout childListLayout = createListLayout(getContext(), childList, yNumber, 0, 0);
            addListLayout(this, childListLayout, false);
            childListLayoutY = 0;
            childDataListIndex = 0;
        }
    }

    public void clickUp(){

        int listSize = nowMenuDataList.getListSize();
        if(listSize <= 1)
            return;

        LinearLayout nowListLayout = (LinearLayout) getChildAt(nowMenuX);

        //当前选中的取消掉
        cancelSelectItem(nowListLayout, nowListLayoutY);

        //计算即将选中的y    轮转view
        if(nowDataListIndex == 0){
            if(listSize > yNumber){
                //洗牌list  view 到尾
                formatListLayout(nowListLayout, nowMenuDataList, yNumber - 1, listSize - 1);
            }
            nowListLayoutY = nowListLayout.getChildCount() - 1;
        }else {
            if(nowListLayoutY == 0){
                //轮转
                nowListLayout.removeViewAt(yNumber - 1);
                TextView listItemView = createListItemView(getContext(), nowMenuDataList, nowDataListIndex - 1);
                addListItemView(nowListLayout, listItemView, true);
            }else
                nowListLayoutY--;
        }

        //选中现在的view
        selectItem(nowListLayout, nowListLayoutY);

        nowDataListIndex = (nowDataListIndex + listSize - 1)%listSize;

        //初始化洗牌  子list  view
        int itemType = nowMenuDataList.getItemType(nowDataListIndex);
        if(itemType == LIST_ITEM_TYPE_Folder){//新选中文件夹
            MenuDataList childList = nowMenuDataList.getChildList(nowDataListIndex);
            if(nowMenuX + 1 < getChildCount())//之前是文件夹
                formatListLayout((LinearLayout) getChildAt(nowMenuX + 1), childList, 0, 0);
            else{
                if(nowMenuX == xNumber - 1){//现在在最后一列
                    //挤掉第一个
                    removeViewAt(0);
                    nowMenuX--;
                }
                LinearLayout listLayout = createListLayout(getContext(), childList, yNumber, 0, 0);
                addListLayout(this, listLayout, false);
            }
            childListLayoutY = 0;
            childDataListIndex = 0;
        }else {//新选中不是文件夹
            if(nowMenuX + 1 < getChildCount()){//之前是文件夹
                removeViewAt(nowMenuX + 1);

                int size = mLinkedList.size();
                if(nowMenuX < size){//前面有未显示
                    int linkedListIndex = size - nowMenuX - 1;
                    MenuDataList menuDataList = mLinkedList.get(linkedListIndex);
                    int listLayoutY = listLayoutYLinkedList.get(linkedListIndex);
                    int dataListIndex = dataListIndexLinkedList.get(linkedListIndex);
                    LinearLayout listLayout = createListLayout(getContext(), menuDataList, yNumber, listLayoutY, dataListIndex);
                    selectItem(listLayout, listLayoutY);
                    addListLayout(this, listLayout, true);
                    nowMenuX++;
                }
            }
        }

        nowMenuDataList.onMoveToItem(nowDataListIndex, MOVE_TO_ITEM_TYPE_UP);
    }

    public void clickDown(){

        int listSize = nowMenuDataList.getListSize();
        if(listSize <= 1)
            return;

        LinearLayout nowListLayout = (LinearLayout) getChildAt(nowMenuX);

        //当前选中的取消掉
        cancelSelectItem(nowListLayout, nowListLayoutY);

        //计算即将选中的y    轮转view
        if(nowDataListIndex == listSize - 1){
            if(listSize > yNumber){
                formatListLayout(nowListLayout, nowMenuDataList, 0, 0);
            }
            nowListLayoutY = 0;
        }else {
            if(nowListLayoutY == yNumber - 1){
                //轮转+1
                nowListLayout.removeViewAt(0);
                TextView listItemView = createListItemView(getContext(), nowMenuDataList, nowDataListIndex + 1);
                addListItemView(nowListLayout, listItemView, false);
            }else
                nowListLayoutY++;
        }

        //选中现在的view
        selectItem(nowListLayout, nowListLayoutY);

        nowDataListIndex = (nowDataListIndex + 1)%listSize;

        //初始化洗牌  子list  view
        int itemType = nowMenuDataList.getItemType(nowDataListIndex);
        if(itemType == LIST_ITEM_TYPE_Folder){//新选中文件夹
            MenuDataList childList = nowMenuDataList.getChildList(nowDataListIndex);
            if(nowMenuX + 1 < getChildCount())//之前是文件夹
                formatListLayout((LinearLayout) getChildAt(nowMenuX + 1), childList, 0, 0);
            else{
                if(nowMenuX == xNumber - 1){//现在在最后一列
                    //挤掉第一个
                    removeViewAt(0);
                    nowMenuX--;
                }
                LinearLayout listLayout = createListLayout(getContext(), childList, yNumber, 0, 0);
                addListLayout(this, listLayout, false);
            }
            childListLayoutY = 0;
            childDataListIndex = 0;
        }else {//新选中不是文件夹
            if(nowMenuX + 1 < getChildCount()){//之前是文件夹
                removeViewAt(nowMenuX + 1);

                int size = mLinkedList.size();
                if(nowMenuX < size){//前面有未显示
                    int linkedListIndex = size - nowMenuX - 1;
                    MenuDataList menuDataList = mLinkedList.get(linkedListIndex);
                    int listLayoutY = listLayoutYLinkedList.get(linkedListIndex);
                    int dataListIndex = dataListIndexLinkedList.get(linkedListIndex);
                    LinearLayout listLayout = createListLayout(getContext(), menuDataList, yNumber, listLayoutY, dataListIndex);
                    selectItem(listLayout, listLayoutY);
                    addListLayout(this, listLayout, true);
                    nowMenuX++;
                }
            }
        }

        nowMenuDataList.onMoveToItem(nowDataListIndex, MOVE_TO_ITEM_TYPE_DOWN);
    }

    public void clickLeft(){

        if(nowMenuX == 0)
            return;

        LinearLayout childListLayout = (LinearLayout) getChildAt(nowMenuX--);

        //当前选中的取消掉
        cancelSelectItem(childListLayout, nowListLayoutY);

        //弹栈
        nowMenuDataList = mLinkedList.removeLast();
        childListLayoutY = nowListLayoutY;
        nowListLayoutY = listLayoutYLinkedList.removeLast();
        childDataListIndex = nowDataListIndex;
        nowDataListIndex = dataListIndexLinkedList.removeLast();

        selectItem((LinearLayout) getChildAt(nowMenuX), nowListLayoutY);//选中新的

        if(nowMenuX + 1 + 1 < getChildCount()){
            //移除最后一个list  view
            removeViewAt(nowMenuX + 2);
        }

        int size = mLinkedList.size();
        if(nowMenuX < size && getChildCount() < xNumber){
            int linkedListIndex = size - nowMenuX - 1;
            MenuDataList menuDataList = mLinkedList.get(linkedListIndex);
            int listLayoutY = listLayoutYLinkedList.get(linkedListIndex);
            int dataListIndex = dataListIndexLinkedList.get(linkedListIndex);
            LinearLayout listLayout = createListLayout(getContext(), menuDataList, yNumber, listLayoutY, dataListIndex);
            listLayout.getChildAt(listLayoutY).setBackgroundColor(Color.parseColor("#AAFFFFFF"));//已进入文件夹颜色--------------------------
            addListLayout(this, listLayout, true);
            nowMenuX++;
        }

        nowMenuDataList.onMoveToItem(nowDataListIndex, MOVE_TO_ITEM_TYPE_LEFT);
    }

    public void clickRight(){

        int type = nowMenuDataList.getItemType(nowDataListIndex);
        if(type != LIST_ITEM_TYPE_Folder)
            return;

        View view = ((LinearLayout) getChildAt(nowMenuX)).getChildAt(nowListLayoutY);//---------
        view.setBackgroundColor(Color.parseColor("#AAFFFFFF"));//已进入文件夹颜色-----
        view.setSelected(false);//停止跑马灯---------------------------------------------------

        LinearLayout nowListLayout = (LinearLayout) getChildAt(++nowMenuX);
        //选中新的
        selectItem(nowListLayout, childListLayoutY);

        //压栈
        mLinkedList.add(nowMenuDataList);
        nowMenuDataList = nowMenuDataList.getChildList(nowDataListIndex);
        listLayoutYLinkedList.add(nowListLayoutY);
        nowListLayoutY = childListLayoutY;
        dataListIndexLinkedList.add(nowDataListIndex);
        nowDataListIndex = childDataListIndex;

        int itemType = nowMenuDataList.getItemType(nowDataListIndex);
        if(itemType == LIST_ITEM_TYPE_Folder){

            if(nowMenuX == xNumber - 1){
                //挤掉第一个
                removeViewAt(0);
                nowMenuX--;
            }
            //添加新的
            LinearLayout listLayout = createListLayout(getContext(), nowMenuDataList.getChildList(nowDataListIndex), yNumber, 0, 0);
            addListLayout(this, listLayout, false);

            childListLayoutY = 0;
            childDataListIndex = 0;
        }

        nowMenuDataList.onMoveToItem(nowDataListIndex, MOVE_TO_ITEM_TYPE_RIGHT);
    }

    public void clickOk(){

        boolean canOk = nowMenuDataList.canOk(nowDataListIndex);
        if(!canOk)
            return;

        LinearLayout nowListLayout = (LinearLayout) getChildAt(nowMenuX);

        int itemType = nowMenuDataList.getItemType(nowDataListIndex);
        switch (itemType){
            case LIST_ITEM_TYPE_Folder:
                final TextView folder = (TextView) nowListLayout.getChildAt(nowListLayoutY);
                folder.setSelected(false);
                folder.setBackgroundColor(listItemBackgroundColor);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        folder.setBackgroundColor(selectColor);
                    }
                }, 36);
                break;
            case LIST_ITEM_TYPE_TextView:
                final TextView textView = (TextView) nowListLayout.getChildAt(nowListLayoutY);
                textView.setSelected(false);
                final String text = nowMenuDataList.getItemText(nowDataListIndex);
                textView.setText("◆  " + text);
//                button.setBackgroundColor(Color.YELLOW);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        button.setBackgroundColor(selectColor);
                        textView.setText("◇  " + text);//●○◎☑☒■□◆◇
                    }
                }, 128);
                break;
            case LIST_ITEM_TYPE_Button:
                final TextView button = (TextView) nowListLayout.getChildAt(nowListLayoutY);
                button.setSelected(false);
                final String itemText = nowMenuDataList.getItemText(nowDataListIndex);
                button.setText("▶  " + itemText);
//                button.setBackgroundColor(Color.YELLOW);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        button.setBackgroundColor(selectColor);
                        button.setText("▷  " + itemText);//●○◎☑☒■□◆◇
                    }
                }, 128);
                break;
            case LIST_ITEM_TYPE_RadioButton:
                int listSize = nowMenuDataList.getListSize();
                for(int i = 1; i <= yNumber && i <= listSize; i++){
                    int dataListindex = nowDataListIndex - nowListLayoutY + i - 1;
                    int type = nowMenuDataList.getItemType(dataListindex);
                    if(type == LIST_ITEM_TYPE_RadioButton){
                        boolean isSelected = nowMenuDataList.isSelected(dataListindex);
                        if(isSelected){
                            RadioButton radioButton = (RadioButton) nowListLayout.getChildAt(i - 1);
                            radioButton.setChecked(false);
                            break;
                        }
                    }
                }
                RadioButton radioButton = (RadioButton) nowListLayout.getChildAt(nowListLayoutY);
                radioButton.setChecked(true);
                break;
            case LIST_ITEM_TYPE_CheckBox:
                CheckBox checkBox = (CheckBox) nowListLayout.getChildAt(nowListLayoutY);
                boolean checked = checkBox.isChecked();
                checkBox.setChecked(!checked);
                break;
        }

        nowMenuDataList.ok(nowDataListIndex);
    }

    public interface MenuDataList{

        int getListSize();

        int getItemType(int index);

        boolean isSelected(int index);

        String getItemText(int index);

        MenuDataList getChildList(int index);

        boolean canOk(int index);

        void ok(int index);

        void onMoveToItem(int index, int moveToItemType);
    }
}
