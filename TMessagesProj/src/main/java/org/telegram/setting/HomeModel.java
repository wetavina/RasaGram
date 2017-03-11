package org.telegram.setting;

import android.content.Context;

import com.inspius.read247.R;

/**
 * Created by dev on 3/9/2015.
 */
public class HomeModel {
    public ProductModel productModel;
    public String countView;
    public String countDown;
    public String author;

    public HomeModel(Context context, ProductModel productModel) {
        this.productModel = productModel;
        countView = String.format(context.getString(R.string.item_view), productModel.viewCount);
        countDown= String.format(context.getString(R.string.item_down), productModel.viewCount);

        author = productModel.author;
        if(author==null||author.isEmpty())
            author = context.getString(R.string.not_known);
    }
}
