package com.topotek.topotekmodule.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Size;
import android.view.SurfaceView;
import android.view.TextureView;

import com.topotek.libs.libs0.project.imageBlendDataHandle.ImageBlendData;
import com.topotek.libs.libs0.project.imageBlendDataHandle.ImageBlendDataHandler;
import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.menu.Menu;
import com.topotek.module.android.menu.SimpleMenuDataList;
import com.topotek.module.project.storageDevice.TfCardHelper;
import com.topotek.module.project.version.Version;
import com.topotek.modules.modules0.project.cameraZoom.CameraZoomModule;
import com.topotek.modules.modules0.project.imageBlendModule.ImageBlendModule;
import com.topotek.topotekmodule.SimplifyCallback.MyCameraManagerCaptureCallback;
import com.topotek.topotekmodule.cameraView.AutoPreviewView;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Developer extends SimpleMenuDataList {

    public static boolean isDeveloper = false;

    private Context context;

    public Developer(Context context){
        this.context = context;
    }

    @Override
    public void integrationInterface(int index, int interfaceType) {
        switch (index){
            case 0:
                itemText = "模拟红外遥控器";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {
                    @Override
                    public void integrationInterface(int index, int interfaceType) {
                        switch (index){
                            case 0:
                                itemText = ":wRecC";
                                break;
                            case 8:
                                itemText = ":wRecA";
                                break;
                            case 9:
                                itemText = ":wRecB";
                                break;
                            default:
                                itemText = ":wKey" + index;
                                break;
                        }
                        itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                            sendCommand(context, itemText);
                    }
                    @Override
                    public int getListSize() {return 10;}
                };
                break;
            case 1:
                itemText = "预留命令";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {
                    @Override
                    public void integrationInterface(int index, int interfaceType) {
                        itemText = ":wKey" + (char)('A' + index);itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                        if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                            sendCommand(context, itemText);
                    }
                    @Override
                    public int getListSize() {return 26;}
                };
                break;
            case 2:
                itemText = "预留按钮";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {
                    @Override
                    public void integrationInterface(int index, int interfaceType) {
                        itemText = String.valueOf(index);itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                        switch (index){
                            case 0://按钮0
                                itemText = itemText + "_服务版本";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    context.sendBroadcast(new Intent("com.topotek.service.write").putExtra("debug", "稳定版"));
                                break;
                            case 1://按钮1
                                itemText = itemText + "    List";itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                if(ImageBlendModule.imageBlendDatas != null){
                                    itemType = Menu.LIST_ITEM_TYPE_Folder;
                                    childList = new SimpleMenuDataList() {

                                        private ArrayList<ImageBlendData> arrayList = (ArrayList<ImageBlendData>) ImageBlendModule.imageBlendDatas.clone();

                                        @Override
                                        public void integrationInterface(int index, int interfaceType) {
                                            if(index == 0){
                                                itemText = "list";
                                                itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                canOk = false;
                                                return;
                                            }
                                            ImageBlendData imageBlendData = arrayList.get(index - 1);
                                            itemText = imageBlendData.zoom + " : " + imageBlendData.previewView1SizeScale;
                                            itemType = Menu.LIST_ITEM_TYPE_CheckBox;
                                            if(imageBlendData.previewView1SizeScale == -4){
                                                isSelected = false;
                                                canOk = false;
                                            }else {
                                                isSelected = true;
                                                canOk = true;
                                            }
                                            if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                                imageBlendData.previewView1SizeScale = -4;
                                                for (ImageBlendData ibd : ImageBlendModule.imageBlendDatas){
                                                    if(ibd.zoom == imageBlendData.zoom){
                                                        ImageBlendModule.imageBlendDatas.remove(ibd);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public int getListSize() {
                                            return arrayList.size() + 1;
                                        }
                                    };
                                }
                                break;
                            case 2:
                                itemText = itemText + "    放大0.01";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    UiModule.cameraView1.plusPreviewViewSizeScale(0.01F);
                                    ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom, UiModule.cameraView1.getPreviewViewSizeScale());
                                    ImageBlendDataHandler.putImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
                                }
                                break;
                            case 3:
                                itemText = itemText + "    缩小0.01";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    UiModule.cameraView1.plusPreviewViewSizeScale(-0.01F);
                                    ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom, UiModule.cameraView1.getPreviewViewSizeScale());
                                    ImageBlendDataHandler.putImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
                                }
                                break;
                            case 4:
                                itemText = itemText + "    放大0.1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    UiModule.cameraView1.plusPreviewViewSizeScale(0.1F);
                                    ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom, UiModule.cameraView1.getPreviewViewSizeScale());
                                    ImageBlendDataHandler.putImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
                                }
                                break;
                            case 5:
                                itemText = itemText + "    缩小0.1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    UiModule.cameraView1.plusPreviewViewSizeScale(-0.1F);
                                    ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom, UiModule.cameraView1.getPreviewViewSizeScale());
                                    ImageBlendDataHandler.putImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
                                }
                                break;
                            case 6:
                                itemText = itemText + "    0+0.1A";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    ImageBlendModule.previewView0Alpha += 0.1F;
                                    UiModule.previewView0.setAlpha(ImageBlendModule.previewView0Alpha);
                                }
                                break;
                            case 7:
                                itemText = itemText + "    0-0.1A";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    ImageBlendModule.previewView0Alpha -= 0.1F;
                                    UiModule.previewView0.setAlpha(ImageBlendModule.previewView0Alpha);
                                }
                                break;
                            case 8:
                                itemText = itemText + "    save";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    ImageBlendModule.saveImageBlendDatas(context, ImageBlendModule.imageBlendDatas);
                                break;
                            case 9:
                                itemText = itemText + "    融合开关";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    sendCommand(context, "#TPDD2wFUS0ARR");
                                break;
                            case 10:
                                itemText = itemText + "    ↑10";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationY(-10F);
                                break;
                            case 11:
                                itemText = itemText + "    ↓10";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationY(10F);
                                break;
                            case 12:
                                itemText = itemText + "    ←10";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationX(-10F);
                                break;
                            case 13:
                                itemText = itemText + "    →10";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationX(10F);
                                break;
                            case 14:
                                itemText = itemText + "    ↑1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationY(-1F);
                                break;
                            case 15:
                                itemText = itemText + "    ↓1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationY(1F);
                                break;
                            case 16:
                                itemText = itemText + "    ←1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationX(-1F);
                                break;
                            case 17:
                                itemText = itemText + "    →1";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                                    UiModule.cameraView1.plusPreviewViewTranslationX(1F);
                                break;
                            case 18:
                                itemText = itemText + "_isAlwaysHaveTfCard";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    MyCameraManagerCaptureCallback.cameraIsAlwaysHaveTfCard = !MyCameraManagerCaptureCallback.cameraIsAlwaysHaveTfCard;
                                    Debugger.addShow("cameraIsAlwaysHaveTfCard = " + MyCameraManagerCaptureCallback.cameraIsAlwaysHaveTfCard);
                                }
                                break;
                            case 19:
                                itemText = itemText + "    10.0X";
                                if(interfaceType == SimpleMenuDataList.Interface_Type_ok){
                                    sendCommand(context, "#tpDD7wZMP1001000RR");
                                }
                                break;
                        }
                    }
                    @Override
                    public int getListSize() {return 10000;}
                };
                break;
            case 3:
                itemText = "参数";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {

                    CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                    String[] cameraIdList;

                    {
                        try {
                            cameraIdList = cameraManager.getCameraIdList();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void integrationInterface(int index, int interfaceType) {

                        switch (index){
                            case 0:
                                itemText = "Camera";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                childList = new SimpleMenuDataList() {

                                    @Override
                                    public void integrationInterface(int index, int interfaceType) {
                                        final String cameraId = cameraIdList[index];
                                        itemText = cameraIdList[index];itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                        childList = new SimpleMenuDataList() {
                                            @Override
                                            public void integrationInterface(int index, int interfaceType) {
                                                CameraCharacteristics cameraCharacteristics = null;
                                                try {
                                                    cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                                                } catch (CameraAccessException e) {
                                                    e.printStackTrace();
                                                }

                                                StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                                                switch (index){
                                                    case 0:
                                                        itemText = "AvailableCaptureRequestKeys";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final List<CaptureRequest.Key<?>> availableCaptureRequestKeys = cameraCharacteristics.getAvailableCaptureRequestKeys();
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                itemText = index + "_" + availableCaptureRequestKeys.get(index).getName();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }
                                                            @Override
                                                            public int getListSize() {
                                                                return availableCaptureRequestKeys.size();
                                                            }
                                                        };
                                                        break;
                                                    case 1:
                                                        itemText = "AvailableCaptureResultKeys";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final List<CaptureResult.Key<?>> availableCaptureResultKeys = cameraCharacteristics.getAvailableCaptureResultKeys();
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                itemText = index + "_" + availableCaptureResultKeys.get(index).getName();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return availableCaptureResultKeys.size();
                                                            }
                                                        };
                                                        break;
                                                    case 2:
                                                        itemText = "keys";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final List<CameraCharacteristics.Key<?>> keys = cameraCharacteristics.getKeys();
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                itemText = index + "_" + keys.get(index).getName();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }
                                                            @Override
                                                            public int getListSize() {
                                                                return keys.size();
                                                            }
                                                        };
                                                        break;
                                                    case 3:
                                                        itemText = "MediaRecorder_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] mediaRecorder_outputSizes = streamConfigurationMap.getOutputSizes(MediaRecorder.class);
                                                        if(mediaRecorder_outputSizes == null || mediaRecorder_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size mediaRecorder_outputSize = mediaRecorder_outputSizes[index];
                                                                itemText = mediaRecorder_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return mediaRecorder_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 4:
                                                        itemText = "MediaCodec_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] mediaCodec_outputSizes = streamConfigurationMap.getOutputSizes(MediaCodec.class);
                                                        if(mediaCodec_outputSizes == null || mediaCodec_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size mediaCodec_outputSize = mediaCodec_outputSizes[index];
                                                                itemText = mediaCodec_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return mediaCodec_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 5:
                                                        itemText = "TextureView_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] textureView_outputSizes = streamConfigurationMap.getOutputSizes(TextureView.class);
                                                        if(textureView_outputSizes == null || textureView_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size textureView_outputSize = textureView_outputSizes[index];
                                                                itemText = textureView_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return textureView_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 6:
                                                        itemText = "SurfaceView_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] surfaceView_outputSizes = streamConfigurationMap.getOutputSizes(SurfaceView.class);
                                                        if(surfaceView_outputSizes == null || surfaceView_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size surfaceView_outputSize = surfaceView_outputSizes[index];
                                                                itemText = surfaceView_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return surfaceView_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 7:
                                                        itemText = "ImageFormat_JPEG_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] imageFormat_JPEG_outputSizes = streamConfigurationMap.getOutputSizes(ImageFormat.JPEG);
                                                        if(imageFormat_JPEG_outputSizes == null || imageFormat_JPEG_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size imageFormat_JPEG_outputSize = imageFormat_JPEG_outputSizes[index];
                                                                itemText = imageFormat_JPEG_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return imageFormat_JPEG_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 8:
                                                        itemText = "ImageFormat_NV21_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] imageFormat_NV21_outputSizes = streamConfigurationMap.getOutputSizes(ImageFormat.NV21);
                                                        if(imageFormat_NV21_outputSizes == null || imageFormat_NV21_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size imageFormat_NV21_outputSize = imageFormat_NV21_outputSizes[index];
                                                                itemText = imageFormat_NV21_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return imageFormat_NV21_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 9:
                                                        itemText = "ImageFormat_YUY2_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] imageFormat_YUY2_outputSizes = streamConfigurationMap.getOutputSizes(ImageFormat.YUY2);
                                                        if(imageFormat_YUY2_outputSizes == null || imageFormat_YUY2_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size imageFormat_YUY2_outputSize = imageFormat_YUY2_outputSizes[index];
                                                                itemText = imageFormat_YUY2_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return imageFormat_YUY2_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                    case 10:
                                                        itemText = "ImageFormat_YUV_420_888_outputSizes";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                                                        final Size[] imageFormat_YUV_420_888_outputSizes = streamConfigurationMap.getOutputSizes(ImageFormat.YUV_420_888);
                                                        if(imageFormat_YUV_420_888_outputSizes == null || imageFormat_YUV_420_888_outputSizes.length < 1)
                                                            itemType = Menu.LIST_ITEM_TYPE_TextView;
                                                        childList = new SimpleMenuDataList() {
                                                            @Override
                                                            public void integrationInterface(int index, int interfaceType) {
                                                                Size imageFormat_YUV_420_888_outputSize = imageFormat_YUV_420_888_outputSizes[index];
                                                                itemText = imageFormat_YUV_420_888_outputSize.toString();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                                            }

                                                            @Override
                                                            public int getListSize() {
                                                                return imageFormat_YUV_420_888_outputSizes.length;
                                                            }
                                                        };
                                                        break;
                                                }
                                            }

                                            @Override
                                            public int getListSize() {
                                                return 11;
                                            }
                                        };
                                    }
                                    @Override
                                    public int getListSize() {return cameraIdList.length;}
                                };
                                break;
                        }
                    }
                    @Override
                    public int getListSize() {return 1;}
                };
                break;
            case 4:
                File availableTfCardDirectory = TfCardHelper.getTfCardDirectory();
                if(availableTfCardDirectory == null)
                    availableTfCardDirectory = Environment.getExternalStorageDirectory();
                itemText = availableTfCardDirectory.getName();itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {

                    @Override
                    public void integrationInterface(int index, int interfaceType) {
                        File availableTfCardDirectory = TfCardHelper.getTfCardDirectory();
                        if(availableTfCardDirectory == null)
                            availableTfCardDirectory = Environment.getExternalStorageDirectory();
                        final File file = availableTfCardDirectory.listFiles()[index];
                        itemText = file.getName();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                        if(itemText.equals("DCIM") || itemText.equals("TOPOTEK") && file.isDirectory() && file.listFiles().length > 0){
                            itemType = Menu.LIST_ITEM_TYPE_Folder;
                            childList = new SimpleMenuDataList() {
                                @Override
                                public void integrationInterface(int index, int interfaceType) {
                                    itemText = file.listFiles()[index].getName();itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                                }

                                @Override
                                public int getListSize() {
                                    return file.listFiles().length;
                                }
                            };
                        }
                    }

                    @Override
                    public int getListSize() {
                        File availableTfCardDirectory = TfCardHelper.getTfCardDirectory();
                        if(availableTfCardDirectory == null)
                            availableTfCardDirectory = Environment.getExternalStorageDirectory();
                        return availableTfCardDirectory.listFiles().length;
                    }
                };
                break;
            case 5:
                itemText = "版本说明";itemType = Menu.LIST_ITEM_TYPE_Folder;canOk = false;
                childList = new SimpleMenuDataList() {

                    @Override
                    public void integrationInterface(int index, int interfaceType) {
                        itemText = Version.description[index];itemType = Menu.LIST_ITEM_TYPE_TextView;canOk = false;
                    }
                    @Override
                    public int getListSize() {return Version.description.length;}
                };
                break;
            case 6:
                itemText = "exit";itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                if(interfaceType == SimpleMenuDataList.Interface_Type_ok)
                    System.exit(1);
                break;
            case 7:
                itemText = "开启调试模式";itemType = Menu.LIST_ITEM_TYPE_Button;canOk = true;
                if(interfaceType == SimpleMenuDataList.Interface_Type_ok && !Debugger.isDebug)
                    sendCommand(context, ":wKey8");
                break;
        }
    }

    @Override
    public int getListSize() {return 8;}

    private static void sendCommand(Context context, String command){
        context.sendBroadcast(new Intent("com.topotek.service.data").putExtra("string", command));
    }
}
