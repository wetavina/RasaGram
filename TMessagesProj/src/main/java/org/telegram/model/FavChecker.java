package org.telegram.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 3/6/2015.
 */
public class FavChecker {
    @SerializedName("fav_checker")
    public List<ProductModel> listTruyenMoi;

    @SerializedName("fav_checker")
    public List<ProductModel> listTruyenNoiBat;

    public static FavChecker paserDataFromServer(String data) {
        FavChecker dataHomeModel = new Gson().fromJson(data, FavChecker.class);
        return dataHomeModel;
    }

}
