#include <jni.h>
#include <string>
#include "stdlib.h"
#include "mvnc.h"
#include "syslog.h"
#include "errno.h"
#include "usb_boot.h"
extern "C"{
    #include "fp16.h"
}
#include "run.h"
#define NAME_SIZE 100
#include <string.h>
#include<android/log.h>
#define LOG_TAG   "LogWGD"
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) // 定义LOGE类型
extern "C"
JNIEXPORT jint JNICALL
Java_com_topotek_movidius_MainActivity_intFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    //init device
    /*int a=libusb_init(NULL);
    if(a<0){
        ALOGE("\ninit failed!,reason = %d\n",a);
        ALOGE("\nerrno value: %d, it means:%s",errno,strerror(errno));
        return  a;
    }
    return 1;*/
    if(run())
        return -1;
    return 0;

    mvncStatus retCode;
    void *deviceHandle;
    char devName[NAME_SIZE];
    retCode = mvncGetDeviceName(0, devName, NAME_SIZE);
    ALOGE("Excute!, retCode=%d,devName=%s\n",retCode,devName);
    ALOGE("Excute!, native-lib------------------->mvncGetDeviceName");
    if (retCode != MVNC_OK)
    {   // failed to get device name, maybe none plugged in.
        ALOGE("Excute!,reason = %d\n , 第一个if");
        syslog(1,"Error - No NCS devices found.\n");
        syslog(1,"    mvncStatus value: %d\n", retCode);
        return 100;
    }
    // Try to open the NCS device via the device name
 retCode = mvncOpenDevice(devName, &deviceHandle);
    ALOGE("\nmvncOpenDevice -->errno value: %d, error means:%s, retCode=%d",errno,strerror(errno),retCode);
    if (retCode != MVNC_OK)
    {   // failed to open the device.
        ALOGE("Excute!, 第二个if");
        printf("Error - Could not open NCS device.\n");
        printf("    mvncStatus value: %d\n", retCode);
        exit(-1);
    }
    // deviceHandle is ready to use now.
    // Pass it to other NC API calls as needed and close it when finished.
    ALOGE("Hello NCS! Device opened normally.\n");
    //add by liang, to test movidus;
//    int res = 1;
//    void *graphfile;
//    graphfile = loadfile(path, &len);
    //
    // Open NCS device and set deviceHandle to the valid handle.
    //
    //
    // Read graph from disk and call mvncAllocateGraph to set graphHandle appropriately.
    //

    //
    // Load an image from disk.
    // LoadImage will read image from disk, convert channels to floats.
    // Subtract network mean for each value in each channel. Then convert
    // floats to half precision floats.
    // Return pointer to the buffer of half precision floats.
    unsigned file_size;
    unsigned graph_size;
    char *tx_buf;
    char *graph_buf;
    void *my_graphHandle;
    mvncStatus mvnc_status = MVNC_OK;
    FILE* p_graphfile = NULL;
    FILE* imageBufFp16 = fopen("/sdcard/mvnc/cat.jpg", "r+w+");
    fseek(imageBufFp16, 0, SEEK_END);
    file_size = ftell(imageBufFp16);
    ALOGE("file_size= %d\n",file_size);
    rewind(imageBufFp16);
    if (!(tx_buf = (char*)malloc(file_size))) {
        if (mvnc_loglevel)
            perror("buffer");
        fclose(imageBufFp16);
//        pthread_mutex_unlock(&mm);
        return MVNC_OUT_OF_MEMORY;
    }
    ALOGE("EXCUTE= open image and malloc bufer\n");
    if (fread(tx_buf, 1, file_size, imageBufFp16) != file_size) {
//        if (mvnc_loglevel)
//            perror(mv_cmd_file);
        fclose(imageBufFp16);
        free(tx_buf);
//        pthread_mutex_unlock(&mm);
        ALOGE("Excute!,rc=%d\n , mvnc-api--->if (fread(tx_buf, 1, file_size, fp) != file_size)",file_size);
        return MVNC_MVCMD_NOT_FOUND;
    }
    fclose(imageBufFp16);
//    p_graphfile = fopen("/sdcard/mvnc/graph", "rb+w");
    p_graphfile = fopen("/sdcard/mvnc/facenet_celeb_ncs.graph", "rb+w");
    fseek(p_graphfile, 0, SEEK_END);
    graph_size = ftell(p_graphfile);
    ALOGE("graph_size= %d\n",graph_size);
    rewind(p_graphfile);
    if (!(graph_buf = (char*)malloc(graph_size))) {
        if (mvnc_loglevel)
            perror("buffer");
        fclose(p_graphfile);
//        pthread_mutex_unlock(&mm);
        return MVNC_OUT_OF_MEMORY;
    }
    ALOGE("EXCUTE  open graph file and malloc buffer\n");
    if (fread(graph_buf, 1, graph_size, p_graphfile) != graph_size) {
//        if (mvnc_loglevel)
//            perror(mv_cmd_file);
        fclose(p_graphfile);
        free(graph_buf);
//        pthread_mutex_unlock(&mm);
        ALOGE("Excute!,rc=%d\n , mvnc-api--->if (fread(tx_buf, 1, file_size, fp) != file_size)",file_size);
        return MVNC_MVCMD_NOT_FOUND;
    }
    fclose(p_graphfile);
    ALOGE("function mvncAllocateGraph\n");
    mvnc_status = mvncAllocateGraph(deviceHandle, &my_graphHandle, graph_buf, graph_size);
    if(mvnc_status){
        ALOGE("allocate graph %d\n", mvnc_status);
    }
    // Calculate the length of the buffer that contains the half precision floats.
    // 3 channels * width * height * sizeof a 16-bit float
    // Start the inference with mvncLoadTensor()
    ALOGE("Above---mvncLoadTensor\n");
    retCode = mvncLoadTensor(my_graphHandle, tx_buf, file_size, NULL);
    ALOGE("retCode= %d\n",retCode);
    if (retCode == MVNC_OK)
    {   // The inference has been started, now call mvncGetResult() for the
        // inference result.
        ALOGE("Successfully loaded the tensor for image %s\n", "image.png");
        // Here mvncGetResult() can be called to get the result of the inference
        // that was started with mvncLoadTensor() above.
    }
    {
        void* resultData16;
        void* userParam;
        unsigned int lenResultData;
        retCode = mvncGetResult(my_graphHandle, &resultData16, &lenResultData, &userParam);
        if (retCode == MVNC_OK)
        {   // Successfully got the result. The inference result is in the buffer pointed to by resultData.
            ALOGE("Successfully got the inference result for image %s\n", "test.jpg");
            ALOGE("resultData is %d bytes which is %d 16-bit floats.\n", lenResultData, lenResultData/(int)sizeof(unsigned short));
            // convert half precision floats to full floats
            int numResults = lenResultData / sizeof(unsigned short);
            float* resultData32;
            resultData32 = (float*)malloc(numResults * sizeof(*resultData32));
            fp16tofloat(resultData32, (unsigned char*)resultData16, numResults);
            float maxResult = 0.0;
            int maxIndex = -1;
            for (int index = 0; index < numResults; index++)
            {
                ALOGE("Category %d is: %f\n", index, resultData32[index]);
                if (resultData32[index] > maxResult)
                {
                    maxResult = resultData32[index];
                    maxIndex = index;
                }
            }
            ALOGE("index of top result is: %d - probability of top result is: %f\n", maxIndex, resultData32[maxIndex]);
            free(resultData32);
        }
    }
    //end add;
    retCode = mvncCloseDevice(deviceHandle);
    ALOGE("Excute!,reason = %d\n , native-lib---------->mvncCloseDevice");
    deviceHandle = NULL;
    if (retCode != MVNC_OK)
    {
        ALOGE("Excute!,reason = %d\n , 第三个if");
        printf("Error - Could not close NCS device.\n");
        printf("    mvncStatus value: %d\n", retCode);
        exit(-1);
    }
    ALOGE("Goodbye NCS!  Device Closed normally.\n");
    ALOGE("NCS device working.\n");
    return 0;
}
