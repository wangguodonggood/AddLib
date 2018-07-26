package com.topotek.topotekmodule.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.debug.LogManager;
import com.topotek.module.android.menu.Menu;
import com.topotek.module.android.menu.SimpleMenuDataList;
import com.topotek.module.android.system.Setting;
import com.topotek.module.project.version.Version;
import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;
import com.topotek.topotekmodule.commandExecuter.CommandExecuter;
import com.topotek.topotekmodule.module.toast.ToastUtils;

import java.util.Date;
//菜单选择框  增加子选择框  可改功能
public class MenuModule {
    public static Menu createMenuModule(final Context context){
        //最外层的菜单列表项
        return Menu.createMenu(context, new SimpleMenuDataList() {
            @Override
            public void integrationInterface(int index, int interfaceType) {
                switch (index){
                    case 0:
                        itemText = Strings.语言();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                        childList = new SimpleMenuDataList() {
                            @Override
                            public void integrationInterface(int index, int interfaceType) {
                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;
                                canOk = true;
                                switch (index){
                                    case 0:
                                        itemText = "中文";isSelected = Strings.language == Strings.Language_Chinses;
                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                            Strings.language = Strings.Language_Chinses;
                                            ParameterStorageModule.parameterStorage.putInt("language", Strings.language);
                                            sendCommand(context, ":wKey0");
                                        }
                                        break;
                                    case 1:
                                        itemText = "English";
                                        isSelected = Strings.language == Strings.Language_English;
                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                        Strings.language = Strings.Language_English;
                                        ParameterStorageModule.parameterStorage.putInt("language", Strings.language);
                                        sendCommand(context, ":wKey0");
                                    }
                                        break;
                                    /*case 2:
                                        itemText = "演示";
                                        itemType = Menu.LIST_ITEM_TYPE_Folder;
                                        canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public int getListSize() {
                                                return 3;
                                            }

                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                switch (index){
                                                    case 0:
                                                        itemText = "aaa";itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ToastUtils.toast(context, "qqqq");
                                                        }
                                                        break;
                                                    case 1:
                                                        itemText = "bbb";itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = false;
                                                        isSelected = false;
                                                        switch (interfaceType){
                                                            case SimpleMenuDataList.Interface_Type_onMoveToItem_DOWN:
                                                                ToastUtils.toast(context, "下");
                                                                break;
                                                            case SimpleMenuDataList.Interface_Type_onMoveToItem_UP:
                                                                ToastUtils.toast(context, "shang");
                                                                break;
                                                            case SimpleMenuDataList.Interface_Type_onMoveToItem_RIGHT:
                                                                ToastUtils.toast(context, "右");
                                                                break;
                                                        }
                                                        break;
                                                    case 2:
                                                        itemText = "sffseestrdytfghed";itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                        break;
                                                }
                                            }
                                        };*/
                                }
                            }
                            @Override
                            public int getListSize() {return 2;}
                        };
                        break;
                    case 1:
                        itemText = Strings.图像设置();
                        itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                        childList = new SimpleMenuDataList() {
                            @Override
                            public void integrationInterface(int index, int interfaceType) {
                                switch (index){
                                    case 0:
                                        itemText = "AWB";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                String[] text = {"auto", "incandescent", "fluorescent", "Warm-fluorescent",
                                                        "daylight", "Cloudy-daylight", "twilight", "Shade"};
                                                itemText = text[index];itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                int i = ParameterStorageModule.parameterStorage.getInt("integrationInterfaceAWBindex", 0);
                                                isSelected = index == i;
                                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                    ParameterStorageModule.parameterStorage.putInt("integrationInterfaceAWBindex", index);
                                                    sendCommand(context, "#TPED2wAWB0" + index +"RR");
                                                }
                                            }
                                            @Override
                                            public int getListSize() {return 8;}
                                        };
                                        break;
                                    case 1:
                                        itemText = "EV";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemText = String.valueOf(index - 3);itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                int i = ParameterStorageModule.parameterStorage.getInt("integrationInterfaceEVindex", 3);
                                                isSelected = index == i;
                                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                    ParameterStorageModule.parameterStorage.putInt("integrationInterfaceEVindex", index);
                                                    sendCommand(context, "#TPED2wEVS0" + index +"RR");
                                                }
                                            }
                                            @Override
                                            public int getListSize() {return 7;}
                                        };
                                        break;
                                    case 2:
                                        itemText = "ISO";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                if(index == 0)
                                                    itemText = "auto";
                                                else {
                                                    int a = 100;
                                                    for(int i = 1; i < index; i++){
                                                        a *= 2;
                                                    }
                                                    itemText = "ISO-" + a;
                                                }
                                                int i = ParameterStorageModule.parameterStorage.getInt("integrationInterfaceISOindex", 0);
                                                isSelected = index == i;
                                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                    ParameterStorageModule.parameterStorage.putInt("integrationInterfaceISOindex", index);
                                                    sendCommand(context, "#TPED2wISO0" + index +"RR");
                                                }
                                            }
                                            @Override
                                            public int getListSize() {return 6;}
                                        };
                                        break;
                                }
                            }
                            @Override
                            public int getListSize() {return 3;}
                        };
                        break;
                    case 2:
                        itemText = Strings.拍照录像();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                        childList = new SimpleMenuDataList() {
                            @Override
                            public void integrationInterface(int index, int interfaceType) {
                                switch (index){
                                    case 0:
                                        itemText = Strings.照片分辨率();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                switch (index){
                                                    case 0:
                                                        itemText = "400W";
                                                        break;
                                                    case 1:
                                                        itemText = "800W";
                                                        break;
                                                    case 2:
                                                        itemText = "1300W";
                                                        break;
                                                    case 3:
                                                        itemText = "1600W";
                                                        break;
                                                }
                                                int i = ParameterStorageModule.parameterStorage.getInt("integrationInterfacePICindex", 3);
                                                isSelected = i == index;
                                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                    sendCommand(context, "#TPDD2wPIC" + index + "NRR");
                                                    ParameterStorageModule.parameterStorage.putInt("integrationInterfacePICindex", index);
                                                }
                                            }
                                            @Override
                                            public int getListSize() {return 4;}
                                        };
                                        break;
                                    case 1:
                                        itemText = Strings.录像分辨率();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                switch (index){
                                                    case 0:
                                                        itemText = "720p";
                                                        break;
                                                    case 1:
                                                        itemText = "1080p";
                                                        break;
                                                }
                                                int i = ParameterStorageModule.parameterStorage.getInt("integrationInterfaceVIDindex", 1);
                                                isSelected = i == index;
                                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                    sendCommand(context, "#TPDD2wVID" + index + "NRR");
                                                    ParameterStorageModule.parameterStorage.putInt("integrationInterfaceVIDindex", index);
                                                }
                                            }
                                            @Override
                                            public int getListSize() {return 2;}
                                        };
                                        break;
                                    case 2:
                                        itemText = Strings.拍照设备();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                switch (index){
                                                    case 0:
                                                        itemText = Strings.主摄像头();
                                                        isSelected = CommandExecuter.mainCameraIsCapture && !CommandExecuter.subsidiaryCameraIsCapture;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsCapture", true);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsCapture", false);
                                                            CommandExecuter.mainCameraIsCapture = true;
                                                            CommandExecuter.subsidiaryCameraIsCapture = false;
                                                        }
                                                        break;
                                                    case 1:
                                                        itemText = Strings.副摄像头();
                                                        isSelected = !CommandExecuter.mainCameraIsCapture && CommandExecuter.subsidiaryCameraIsCapture;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsCapture", false);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsCapture", true);
                                                            CommandExecuter.mainCameraIsCapture = false;
                                                            CommandExecuter.subsidiaryCameraIsCapture = true;
                                                        }
                                                        break;
                                                    case 2:
                                                        itemText = Strings.主加副();
                                                        isSelected = CommandExecuter.mainCameraIsCapture && CommandExecuter.subsidiaryCameraIsCapture;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsCapture", true);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsCapture", true);
                                                            CommandExecuter.mainCameraIsCapture = true;
                                                            CommandExecuter.subsidiaryCameraIsCapture = true;
                                                        }
                                                        break;
                                                }
                                            }

                                            @Override
                                            public int getListSize() {
                                                return 3;
                                            }
                                        };
                                        break;
                                    case 3:
                                        itemText = Strings.录像设备();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                itemType = Menu.LIST_ITEM_TYPE_RadioButton;canOk = true;
                                                switch (index){
                                                    case 0:
                                                        itemText = Strings.主摄像头();
                                                        isSelected = CommandExecuter.mainCameraIsRecord && !CommandExecuter.subsidiaryCameraIsRecord;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsRecord", true);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsRecord", false);
                                                            CommandExecuter.mainCameraIsRecord = true;
                                                            CommandExecuter.subsidiaryCameraIsRecord = false;
                                                        }
                                                        break;
                                                    case 1:
                                                        itemText = Strings.副摄像头();
                                                        isSelected = !CommandExecuter.mainCameraIsRecord && CommandExecuter.subsidiaryCameraIsRecord;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsRecord", false);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsRecord", true);
                                                            CommandExecuter.mainCameraIsRecord = false;
                                                            CommandExecuter.subsidiaryCameraIsRecord = true;
                                                        }
                                                        break;
                                                    case 2:
                                                        itemText = Strings.主加副();
                                                        isSelected = CommandExecuter.mainCameraIsRecord && CommandExecuter.subsidiaryCameraIsRecord;
                                                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                            ParameterStorageModule.parameterStorage.putBoolean("mainCameraIsRecord", true);
                                                            ParameterStorageModule.parameterStorage.putBoolean("subsidiaryCameraIsRecord", true);
                                                            CommandExecuter.mainCameraIsRecord = true;
                                                            CommandExecuter.subsidiaryCameraIsRecord = true;
                                                        }
                                                        break;
                                                }
                                            }

                                            @Override
                                            public int getListSize() {
                                                return 3;
                                            }
                                        };
                                        break;
                                }
                            }
                            @Override
                            public int getListSize() {
                                if(Debugger.isDebug)
                                    return 4;
                                switch (Version.modelCode){
                                    case "SMTS":
                                        return 2;
                                    case "SMTST":
                                        return 4;
                                    default:
                                        return 2;
                                }
                            }
                        };
                        break;
                    case 3:
                        itemText = Strings.系统设置();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                        childList = new SimpleMenuDataList() {
                            @Override
                            public void integrationInterface(int index, int interfaceType) {
                                switch (index) {
                                    case 0:
                                    itemText = Strings.时间设置();
                                    itemType = Menu.LIST_ITEM_TYPE_Folder;
                                    canOk = false;
                                    childList = new SimpleMenuDataList() {
                                        int nian;

                                        @Override
                                        public void integrationInterface(int index, int interfaceType) {
                                            nian = index + 2015;
                                            itemText = nian + Strings.年();
                                            itemType = Menu.LIST_ITEM_TYPE_Folder;
                                            canOk = false;
                                            childList = new SimpleMenuDataList() {
                                                int yue;

                                                @Override
                                                public void integrationInterface(int index, int interfaceType) {
                                                    yue = index + 1;
                                                    itemText = yue + Strings.月();
                                                    itemType = Menu.LIST_ITEM_TYPE_Folder;
                                                    canOk = false;
                                                    childList = new SimpleMenuDataList() {
                                                        int ri;

                                                        @Override
                                                        public void integrationInterface(int index, int interfaceType) {
                                                            ri = index + 1;
                                                            itemText = ri + Strings.日();
                                                            itemType = Menu.LIST_ITEM_TYPE_Folder;
                                                            canOk = false;
                                                            childList = new SimpleMenuDataList() {
                                                                int shi;

                                                                @Override
                                                                public void integrationInterface(int index, int interfaceType) {
                                                                    shi = index;
                                                                    itemText = shi + Strings.时();
                                                                    itemType = Menu.LIST_ITEM_TYPE_Folder;
                                                                    canOk = false;
                                                                    childList = new SimpleMenuDataList() {
                                                                        int fen;

                                                                        @Override
                                                                        public void integrationInterface(int index, int interfaceType) {
                                                                            fen = index;
                                                                            itemText = fen + Strings.分();
                                                                            itemType = Menu.LIST_ITEM_TYPE_Folder;
                                                                            canOk = false;
                                                                            childList = new SimpleMenuDataList() {
                                                                                int miao;

                                                                                @Override
                                                                                public void integrationInterface(int index, int interfaceType) {
                                                                                    miao = index;
                                                                                    itemText = miao + Strings.秒();
                                                                                    itemType = Menu.LIST_ITEM_TYPE_Button;
                                                                                    canOk = true;
                                                                                    if (interfaceType == Interface_Type_ok) {
                                                                                        ToastUtils.toast(context.getApplicationContext(),
                                                                                                Toast.LENGTH_LONG, Color.BLUE, Color.RED,
                                                                                                nian + "-" + yue + "-" + ri + "  "
                                                                                                        + shi + ":" + fen + ":" + miao);
                                                                                        Setting.initSystemTimeSetting(context);
                                                                                        Setting.setSystemTimeZone(context, 28800000);
                                                                                        //北京时间  东八区 +08:00    1000 * 60 * 60 * 8 = 28800000(毫秒)
                                                                                        Date date = new Date(nian - 1900, yue - 1, ri, shi, fen, miao);
                                                                                        long millis = date.getTime();
                                                                                        Setting.setSystemTime(context, millis);
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public int getListSize() {
                                                                                    return 60;
                                                                                }
                                                                            };
                                                                        }

                                                                        @Override
                                                                        public int getListSize() {
                                                                            return 60;
                                                                        }
                                                                    };
                                                                }

                                                                @Override
                                                                public int getListSize() {
                                                                    return 24;
                                                                }
                                                            };
                                                        }

                                                        @Override
                                                        public int getListSize() {
                                                            if (yue == 2) {
                                                                if (nian % 4 == 0) {
                                                                    return 29;
                                                                }
                                                                return 28;
                                                            } else if (yue == 4 || yue == 6 || yue == 9 || yue == 11) {
                                                                return 30;
                                                            } else {
                                                                return 31;
                                                            }
                                                        }
                                                    };
                                                }

                                                @Override
                                                public int getListSize() {
                                                    return 12;
                                                }//yue
                                            };
                                        }

                                        @Override
                                        public int getListSize() {
                                            return 7985;
                                        }//nian
                                    };
                                        break;
                                }
                            }
                            @Override
                            public int getListSize() {return 1;}
                        };
                        break;
                    case 4:
                        itemText = Strings.版本信息();
                        itemType = Menu.LIST_ITEM_TYPE_Folder;
                        canOk = false;
                        childList = new SimpleMenuDataList() {
                            @Override
                            public void integrationInterface(int index, int interfaceType) {
                                itemType = Menu.LIST_ITEM_TYPE_TextView;
                                canOk = false;
                                switch (index){
                                    case 0:
                                        itemText = Version.versionToString();
                                        break;
                                }
                                switch (interfaceType){
                                    case SimpleMenuDataList.Interface_Type_onMoveToItem_RIGHT:
                                        developerIndex = 0;
                                        isDeveloper = false;
                                        break;
                                    case SimpleMenuDataList.Interface_Type_canOk:
                                        canOk = isDeveloper();
                                        if(!canOk)
                                            isDeveloper = false;
                                        break;
                                    case SimpleMenuDataList.Interface_Type_ok:
                                        isDeveloper = true;
                                        int length = developer.length;
                                        developer = new int[length];
                                        break;
                                }
                            }
                            @Override
                            public int getListSize() {return 1;}
                        };
                        switch (interfaceType){
                            case SimpleMenuDataList.Interface_Type_onMoveToItem_UP:
                                isDeveloper = false;
                                developer(SimpleMenuDataList.Interface_Type_onMoveToItem_UP);
                                break;
                            case SimpleMenuDataList.Interface_Type_onMoveToItem_DOWN:
                                isDeveloper = false;
                                developer(SimpleMenuDataList.Interface_Type_onMoveToItem_DOWN);
                                break;
                            case SimpleMenuDataList.Interface_Type_onMoveToItem_LEFT:
                                int length = developer.length;
                                developer = new int[length];
                                break;
                            case SimpleMenuDataList.Interface_Type_canOk:
                                if(isDeveloper)
                                    canOk = true;
                                break;
                            case SimpleMenuDataList.Interface_Type_ok:
                                Developer.isDeveloper = true;
                                sendCommand(context, ":wKey0");
                                break;
                        }
                        break;
                    case 5:
                        itemText = "开发者选项";
                        itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                        childList = new Developer(context);
                }
            }

            private boolean isDeveloper = false;
            private int[] developer = new int[4];
            private int developerIndex = 0;
            private void developer(int i){
                developer[developerIndex] = i;
                developerIndex = (developerIndex + 1) % developer.length;
            }
            private boolean isDeveloper(){
                if(developer[0] != SimpleMenuDataList.Interface_Type_onMoveToItem_UP)
                    return false;
                if(developer[1] != SimpleMenuDataList.Interface_Type_onMoveToItem_DOWN)
                    return false;
                if(developer[2] != SimpleMenuDataList.Interface_Type_onMoveToItem_DOWN)
                    return false;
                if(developer[3] != SimpleMenuDataList.Interface_Type_onMoveToItem_UP)
                    return false;
                return true;
            }

            @Override
            public int getListSize() {
                int size = 6;
                if(Developer.isDeveloper || Debugger.isDebug || LogManager.isDebug)
                    return size;
                return size - 1;
            }
        });
    }

    private static void sendCommand(Context context, String command){
        context.sendBroadcast(new Intent("com.topotek.service.data")
                .putExtra("string", command));
    }
}
