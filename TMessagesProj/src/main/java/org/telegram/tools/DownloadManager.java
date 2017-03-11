package org.telegram.tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import com.inspius.read247.MainActivity;
import com.inspius.read247.R;
import com.inspius.read247.app.constant.AppConstant;
import com.inspius.read247.app.model.ProductModel;
import com.inspius.read247.app.util.DialogUtil;

/**
 * Created by Binh on 4/4/2015.
 */
@EBean
public class DownloadManager {
    DisplayImageOptions options;

    @RootContext
    MainActivity mContext;

    ImageLoader imageLoader;
    RPCListener listener;
    ProductModel productModel;
    ProgressDialog mDialog;
    int total = 0;
    int count = 0;
    AlertDialog alertDialogReload;

    public DownloadManager() {
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                //.showImageForEmptyUri(R.drawable.ic_emtry)
                //.showImageOnFail(R.drawable.ic_error)
                //.resetViewBeforeLoading(true)
                //.cacheInMemory(false)
                .cacheOnDisk(true)
                        //.imageScaleType(ImageScaleType.EXACTLY)
                        //.bitmapConfig(Bitmap.Config.RGB_565)
                        //.considerExifParams(true)
                        //.displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    /**
     * Download document
     *
     * @param productModel
     * @param listener
     */
    public void downloadDocument(final ProductModel productModel, final RPCListener listener) {
        if (productModel != null)
            this.productModel = productModel;

        if (listener != null)
            this.listener = listener;

        total = productModel.numberPages;
        count = 0;

        if (total > 0) {
            if (productModel == null)
                return;

            showLoading();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    imageLoader.loadImageSync(productModel.mobileThumbnail);
                    for (int i = 0; i < total; i++) {
                        final String imageUrl = String.format(AppConstant.URL_LIST_DATA_READ, productModel.documentId, i);
                        imageLoader.loadImage(imageUrl, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                if (alertDialogReload != null && alertDialogReload.isShowing())
                                    return;

                                if (mDialog != null && mDialog.isShowing())
                                    mDialog.dismiss();

                                stopLoadImage();

                                showReload();
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                countLoading();
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                if (mDialog != null && mDialog.isShowing())
                                    mDialog.dismiss();
                            }
                        });
                    }
                }
            }).start();
        } else {
            mContext.showMessageBox(mContext.getString(R.string.msg_not_update_data), null);
        }
    }

    @UiThread
    void showReload() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(mContext.getString(R.string.msg_download_false));
        alertDialogBuilder.setMessage(mContext.getString(R.string.msg_check_connect_or_memory));
        alertDialogBuilder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setPositiveButton(mContext.getString(R.string.msg_continue_download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadDocument(productModel, listener);
            }
        });

        alertDialogReload = alertDialogBuilder.create();

        alertDialogBuilder.show();
    }

    @UiThread
    void showLoading() {
        mDialog = DialogUtil.showDialogDownload(mContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelDownload();
            }
        });

        mDialog.show();
    }

    @UiThread
    void countLoading() {
        count++;
        float per = ((float) count / total) * 100f;
        mDialog.setMessage(mContext.getString(R.string.msg_downloading) + (int) per + "%");

        if (count == total) {
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();

            if (listener != null)
                listener.onSuccess(true);
        }

    }

    @UiThread
    @Background
    public void cancelDownload() {
        if (imageLoader == null)
            return;

        if (alertDialogReload != null && alertDialogReload.isShowing())
            alertDialogReload.dismiss();

        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        stopLoadImage();

        imageLoader.getDiskCache().remove(productModel.mobileThumbnail);

        for (int i = 0; i < total; i++) {
            final String imageUrl = String.format(AppConstant.URL_LIST_DATA_READ, productModel.documentId, i);
            if (imageLoader != null)
                imageLoader.getDiskCache().remove(imageUrl);
        }

        count = 0;
        total = 0;
    }

    public void stopLoadImage() {
        if (imageLoader == null)
            return;

        imageLoader.stop();
    }

    public boolean isDownSuccess() {
        if (count == total)
            return true;
        else
            return false;
    }
}
