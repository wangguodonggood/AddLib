package com.topotek.topotekmodule.cameraManage;

import android.hardware.Camera;

import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;

import java.util.Arrays;
import java.util.List;


class CameraParametersUtils {

    /**
     * @return 若会出现空指针则返回-1;
     */
    static int setPictureSize(Camera camera, int width, int height) {

        if (camera == null)
            return -1;

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return -1;

        parameters.setPictureSize(width, height);
        camera.setParameters(parameters);

        return 0;
    }

    /**
     * @return 若会出现空指针则返回-1;
     */
    static int getWhiteBalanceIndex(Camera camera) {
        if (camera == null)
            return -1;

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return -1;

        String whiteBalance = parameters.getWhiteBalance();
        if (whiteBalance == null)
            return -1;

        List<String> supportedWhiteBalance = parameters.getSupportedWhiteBalance();
        if (supportedWhiteBalance == null)
            return -1;

        return supportedWhiteBalance.indexOf(whiteBalance);
    }

    /**
     * @return 若会出现空指针则返回Integer.MIN_VALUE;
     */
    static int getExposureCompensation(Camera camera) {

        if (camera == null)
            return Integer.MIN_VALUE;

        Camera.Parameters parameters = camera.getParameters();

        if (parameters == null)
            return Integer.MIN_VALUE;

        return parameters.getExposureCompensation();
    }

    /**
     * @return 若会出现空指针则返回-1;
     */
    static int getIsoIndex(Camera camera) {
        return get(camera, "iso-speed", "iso-speed-values");
    }

    /**
     * @return 返回key_Type的index;若会出现空指针则返回-1;
     */
    private static int get(Camera camera, String key_Type, String key_SupportedValue) {

        if (camera == null || key_Type == null || key_SupportedValue == null)
            return -1;

        Camera.Parameters parameters = camera.getParameters();

        if (parameters == null)
            return -1;

        String value = parameters.get(key_Type);
        if (value == null)
            return -1;

        String supportedValue = parameters.get(key_SupportedValue);
        if (supportedValue == null)
            return -1;

        String[] array_supportedValue = supportedValue.split(",");
        List<String> list_supportedValue = Arrays.asList(array_supportedValue);

        return list_supportedValue.indexOf(value);
    }

    /**
     * @return 返回设置的白平衡;
     * 若会出现空指针则返回null;
     */
    static String setWhiteBalance(Camera camera, int index) {

        if (camera == null)
            return null;

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return null;

        List<String> supportedWhiteBalance = parameters.getSupportedWhiteBalance();
        if (supportedWhiteBalance == null)
            return null;

        index = remain(index, supportedWhiteBalance.size());

        String whiteBalanceValue = supportedWhiteBalance.get(index);
        if (whiteBalanceValue == null)
            return null;

        ParameterStorageModule.parameterStorage.putInt("cameraWhiteBalanceIndex", index);//==============================================

        parameters.setWhiteBalance(whiteBalanceValue);
        camera.setParameters(parameters);

        return whiteBalanceValue;
    }

    /**
     * @return 返回设置的白平衡;
     * 若会出现空指针则返回null;
     */
    static String changeWhiteBalance(Camera camera, int change) {

        int whiteBalanceIndex = getWhiteBalanceIndex(camera);
        if (whiteBalanceIndex == -1)
            return null;

        return setWhiteBalance(camera, whiteBalanceIndex + change);
    }

    /**
     * @return 返回设置的曝光;
     * 若会出现空指针则返回Integer.MIN_VALUE;
     */
    static int setExposureCompensation(Camera camera, int value) {

        if (camera == null)
            return Integer.MIN_VALUE;

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return Integer.MIN_VALUE;

        int minExposureCompensation = parameters.getMinExposureCompensation();
        int maxExposureCompensation = parameters.getMaxExposureCompensation();

        value = Math.min(maxExposureCompensation, Math.max(value, minExposureCompensation));

        ParameterStorageModule.parameterStorage.putInt("cameraExposureCompensationValue", value);//==========================================

        parameters.setExposureCompensation(value);
        camera.setParameters(parameters);

        return value;
    }

    /**
     * @return 返回设置的曝光;
     * 若会出现空指针则返回Integer.MIN_VALUE;
     */
    static int changeExposureCompensation(Camera camera, int change) {

        int exposureCompensation = getExposureCompensation(camera);
        if (exposureCompensation == Integer.MIN_VALUE)
            return Integer.MIN_VALUE;

        return setExposureCompensation(camera, exposureCompensation + change);
    }

    /**
     * @return 返回设置的ISO;
     * 若会出现空指针则返回null;
     */
    static String setIso(Camera camera, int index) {

        return set(camera, "iso-speed", "iso-speed-values", index);
    }

    /**
     * @return 返回设置的ISO;
     * 若会出现空指针则返回null;
     */
    static String changeIso(Camera camera, int change) {

        int isoIndex = getIsoIndex(camera);
        if (isoIndex == -1)
            return null;

        return setIso(camera, isoIndex + change);
    }

    /**
     * @return 返回设置的值;
     * 若会出现空指针则返回null;
     */
    private static String set(Camera camera, String key_Type, String key_SupportedValue, int index) {

        if (camera == null || key_Type == null || key_SupportedValue == null)
            return null;

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return null;

        String supportedValue = parameters.get(key_SupportedValue);
        if (supportedValue == null)
            return null;

        String[] array_supportedValue = supportedValue.split(",");
        List<String> list_supportedValue = Arrays.asList(array_supportedValue);

        index = remain(index, list_supportedValue.size());

        String value = list_supportedValue.get(index);
        if (value == null)
            return null;

        if("iso-speed".equals(key_Type))
            ParameterStorageModule.parameterStorage.putInt("cameraIsoIndex", index);//======================================================

        parameters.set(key_Type, value);
        camera.setParameters(parameters);

        return value;
    }

    /**
     * @param dividend 可为负数
     * @param divisor  必须是正数
     * @return 返回余数;若divisor不是正数则返回-1;
     */
    private static int remain(int dividend, int divisor) {

        if (divisor <= 0) {
            return -1;
        }

        if (dividend < 0) {
            dividend = divisor - (-dividend % divisor);
        }

        return dividend % divisor;
    }
}
