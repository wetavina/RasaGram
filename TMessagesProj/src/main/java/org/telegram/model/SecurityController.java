package org.telegram.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 3/6/2015.
 */
public class SecurityController {
    @SerializedName("secu")
    public List<SecurityController> listSachMoi;

    @SerializedName("secu")
    public List<SecurityController lisTruyenMoi;

    @SerializedName("secu")
    public List<SecurityController> listDocNhieuNhat;

    public static SecurityController paserDataFromServer(String data){
        DataHomeModel dataHomeModel = new Gson().fromJson(data, DataHomeModel.class);
        return dataHomeModel;
    }

}
