package com.topotek.libs.libs0.project.imageBlendDataHandle;

//k
public class ImageBlendData {

    //镜头zoom位置
    public float zoom;

    //热成像缩放比
    public float previewView1SizeScale = 1;

    public ImageBlendData(float zoom) {
        this.zoom = zoom;
    }

    public ImageBlendData(float zoom, float previewView1SizeScale) {
        this.zoom = zoom;
        this.previewView1SizeScale = previewView1SizeScale;
    }
}
