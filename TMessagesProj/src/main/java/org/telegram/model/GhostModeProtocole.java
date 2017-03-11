package org.telegram.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 3/6/2015.
 */
public class GhostModeProtocole {
    @SerializedName("gost_mode")
    public List<ProductModel> listSachMoi;

    @SerializedName("gost_mode")
    public List<ProductModel> listSachNoiBat;

    public static GhostModeProtocole paserDataFromServer(String data){
        DataBookModel dataHomeModel = new Gson().fromJson(data, DataBookModel.class);
        return dataHomeModel;
    }

}
