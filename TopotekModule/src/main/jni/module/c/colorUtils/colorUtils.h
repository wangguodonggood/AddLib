#ifndef TOPOTEKCAMERA_COLORUTILS_H
#define TOPOTEKCAMERA_COLORUTILS_H

#include <memory.h>

void yuyvFieldToYuyvFrame(unsigned char *yuyvField, int width, int height, unsigned char *yuyvFrame){

    int oneRowByteCount = width * 2;
    int oneFieldByteCount = oneRowByteCount * height / 2;
    unsigned char *yuyvOddFieldIndex = yuyvField;
    unsigned char *yuyvEvenFieldIndex = yuyvOddFieldIndex + oneFieldByteCount;

    size_t size_t_oneRowByteCount = (size_t) oneRowByteCount;

    int doubleRowCount = height / 2;
    int doubleRowIndex;
    for(doubleRowIndex = 0; doubleRowIndex < doubleRowCount; ++doubleRowIndex){

        memcpy(yuyvFrame, yuyvOddFieldIndex, size_t_oneRowByteCount);
        yuyvFrame += oneRowByteCount;
        yuyvOddFieldIndex += oneRowByteCount;
        memcpy(yuyvFrame, yuyvEvenFieldIndex, size_t_oneRowByteCount);
        yuyvFrame += oneRowByteCount;
        yuyvEvenFieldIndex += oneRowByteCount;
    }
}

void calculateRedByYV(unsigned char *a, unsigned char Y, unsigned char V){

    double d = Y + 1.402 * (V - 128);
    *a = (unsigned char) (d > 255 ? 255 : d);
}

void calculateGreenByYUV(unsigned char *a, unsigned char Y, unsigned char U, unsigned char V){

    double d = Y - 0.34414 * (U - 128) - 0.71414 * (V - 128);
    *a = (unsigned char) (d > 255 ? 255 : d);
}

void calculateBlueByYU(unsigned char *a, unsigned char Y, unsigned char U){

    double d = Y + 1.772 * (U - 128);
    *a = (unsigned char) (d > 255 ? 255 : d);
}

void calculateAlpha(unsigned char *a){
    *a = 255;
}

void yuyvFrameToRgbaFrame(unsigned char *yuvData, int width, int height, unsigned char *argbData){

    unsigned char currentY1;
    unsigned char currentU;
    unsigned char currentY2;
    unsigned char currentV;

    int doublePixelCount = width * height / 2;
    int doublePixelIndex;
    for(doublePixelIndex = 0; doublePixelIndex < doublePixelCount; ++doublePixelIndex){

        currentY1 = *yuvData++;
        currentU = *yuvData++;
        currentY2 = *yuvData++;
        currentV = *yuvData++;

        calculateRedByYV(argbData++, currentY1, currentV);
        calculateGreenByYUV(argbData++, currentY1, currentU, currentV);
        calculateBlueByYU(argbData++, currentY1, currentU);
        calculateAlpha(argbData++);

        calculateRedByYV(argbData++, currentY2, currentV);
        calculateGreenByYUV(argbData++, currentY2, currentU, currentV);
        calculateBlueByYU(argbData++, currentY2, currentU);
        calculateAlpha(argbData++);
    }
}

unsigned char *rgbFrameData;

unsigned char r(unsigned char Y, unsigned char V){
    return ((Y << 8) + ((V << 8) + (V << 5) + (V << 2))) >> 8;
}

unsigned char g(unsigned char Y, unsigned char U, unsigned char V){
    return ((Y << 8) - ((U << 6) + (U << 5) + (U << 2)) - ((V << 7) + (V << 4) + (V << 2) + V)) >> 8;
}

unsigned char b(unsigned char Y, unsigned char U){
    return ((Y << 8) + (U << 9) + (U << 3)) >> 8;
}

unsigned char a(){
    return 255;
}

void yuyvToRgb(unsigned char *yuvFrameData, int width, int height){
    int doubleRowIndex;
    for(doubleRowIndex = 0; doubleRowIndex < height / 2; ++doubleRowIndex){
        int rowDoublePixelIndex;
        for(rowDoublePixelIndex = 0; rowDoublePixelIndex < width / 2; ++rowDoublePixelIndex){

            int RGB_currentOddRowOddPixelByteIndex = doubleRowIndex * width * 8 + rowDoublePixelIndex * 8;
            int YUYV_currentOddFieldRowOddPixelByteIndex = doubleRowIndex * width * 2 + rowDoublePixelIndex * 4;
            unsigned char U = yuvFrameData[YUYV_currentOddFieldRowOddPixelByteIndex + 1];
            unsigned char V = yuvFrameData[YUYV_currentOddFieldRowOddPixelByteIndex + 3];

            unsigned char Y = yuvFrameData[YUYV_currentOddFieldRowOddPixelByteIndex];
            unsigned char A = 255;//-----------------------------------------------------===========--
            unsigned char R = r(Y, V);
            unsigned char G = g(Y, U, V);
            unsigned char B = b(Y, U);
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex] = A;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 1] = R;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 2] = G;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 3] = B;

            Y = yuvFrameData[YUYV_currentOddFieldRowOddPixelByteIndex + 2];
            R = r(Y, V);
            G = g(Y, U, V);
            B = b(Y, U);
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 4] = A;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 5] = R;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 6] = G;
            rgbFrameData[RGB_currentOddRowOddPixelByteIndex + 7] = B;

            int RGB_currentEvenRowOddPixelByteIndex = RGB_currentOddRowOddPixelByteIndex + width * 4;
            int YUYV_currentEvenFieldRowOddPixelByteIndex = YUYV_currentOddFieldRowOddPixelByteIndex + width * height;
            U = yuvFrameData[YUYV_currentEvenFieldRowOddPixelByteIndex + 1];
            V = yuvFrameData[YUYV_currentEvenFieldRowOddPixelByteIndex + 3];

            Y = yuvFrameData[YUYV_currentEvenFieldRowOddPixelByteIndex];
            R = r(Y, V);
            G = g(Y, U, V);
            B = b(Y, U);
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex] = A;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 1] = R;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 2] = G;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 3] = B;

            Y = yuvFrameData[YUYV_currentEvenFieldRowOddPixelByteIndex + 2];
            R = r(Y, V);
            G = g(Y, U, V);
            B = b(Y, U);
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 4] = A;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 5] = R;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 6] = G;
            rgbFrameData[RGB_currentEvenRowOddPixelByteIndex + 7] = B;
        }
    }
}

#endif //TOPOTEKCAMERA_COLORUTILS_H
