package com.taylor.screenshortdemo.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhq13 on 2017/6/19.
 */

public class GraphicPath implements Parcelable {
    protected GraphicPath(Parcel in) {
        int size=in.readInt();
        int[] x=new int[size];
        int[] y=new int[size];
        in.readIntArray(x);
        in.readIntArray(y);
        pathX=new ArrayList<>();
        pathY=new ArrayList<>();

        for (int i=0;i<x.length;i++){
            pathX.add(x[i]);
        }

        for (int i=0;i<y.length;i++){
            pathY.add(y[i]);
        }
    }

    public static final Creator<GraphicPath> CREATOR = new Creator<GraphicPath>() {
        @Override
        public GraphicPath createFromParcel(Parcel in) {
            return new GraphicPath(in);
        }

        @Override
        public GraphicPath[] newArray(int size) {
            return new GraphicPath[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pathX.size());
        dest.writeIntArray(getXArray());
        dest.writeIntArray(getYArray());
    }

    public List<Integer> pathX;
    public List<Integer> pathY;

    public GraphicPath(){
        pathX=new ArrayList<>();
        pathY=new ArrayList<>();
    }

    private int[] getXArray(){
        int[] x=new int[pathX.size()];
        for (int i=0;i<x.length;i++){
            x[i]=pathX.get(i);
        }
        return x;
    }

    private int[] getYArray(){
        int[] y=new int[pathY.size()];
        for (int i=0;i<y.length;i++){
            y[i]=pathY.get(i);
        }
        return y;
    }

    public void addPath(int x,int y){
        pathX.add(x);
        pathY.add(y);
    }

    public void clear(){
        pathX.clear();
        pathY.clear();
    }

    public int getTop(){
        int min=pathY.size()>0?pathY.get(0):0;
        for (int y:pathY){
            if (y<min){
                min=y;
            }
        }
        return min;
    }

    public int getLeft(){
        int min=pathX.size()>0?pathX.get(0):0;
        for (int x:pathX){
            if (x<min){
                min=x;
            }
        }
        return min;
    }

    public int getBottom(){
        int max=pathY.size()>0?pathY.get(0):0;
        for (int y:pathY){
            if (y>max){
                max=y;
            }
        }
        return max;
    }
    public int getRight(){
        int max=pathX.size()>0?pathX.get(0):0;
        for (int x:pathX){
            if (x>max){
                max=x;
            }
        }
        return max;
    }
    public int size(){
        return pathY.size();
    }

}
