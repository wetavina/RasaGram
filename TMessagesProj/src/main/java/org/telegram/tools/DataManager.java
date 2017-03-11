package org.telegram.tools;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.Calendar;

import com.inspius.read247.MainActivity;
import com.inspius.read247.app.constant.AppConstant;
import com.inspius.read247.app.model.DataBookModel;
import com.inspius.read247.app.model.DataHomeModel;
import com.inspius.read247.app.model.DataStoryModel;
import com.inspius.read247.app.pref.SettingPrefs_;
import com.inspius.read247.std.util.Logger;

/**
 * Created by dev on 3/6/2015.
 */
public class DataManager {
    DataHomeModel dataHome;
    DataBookModel dataBook;
    DataStoryModel dataStory;

    MainActivity mContext;

    SettingPrefs_ settingPrefs;

    public DataManager(MainActivity mContext, SettingPrefs_ settingPrefs) {
        this.mContext = mContext;
        this.settingPrefs = settingPrefs;
    }

    /**
     * get All Data
     *
     * @param listener
     */
    public void getAllData(final RPCListener listener) {
        getDataHome(new RPCListener() {
            @Override
            public void onError(int code, String error) {
                listener.onError(code, error);
            }

            @Override
            public void onSuccess(Object object) {
                getDataStory(new RPCListener() {
                    @Override
                    public void onError(int code, String error) {
                        listener.onError(code, error);
                    }

                    @Override
                    public void onSuccess(Object object) {
                        getDataBook(new RPCListener() {
                            @Override
                            public void onError(int code, String error) {
                                listener.onError(code, error);
                            }

                            @Override
                            public void onSuccess(Object object) {
                                listener.onSuccess(null);
                            }
                        });
                    }
                });
            }
        });
    }

//    public void requestData(AppConstant.SUB_MENU_TYPE type, final RPCListener listener) {
//        if (type == AppConstant.SUB_MENU_TYPE.Home) {
//            getDataHome(listener);
//        } else if (type == AppConstant.SUB_MENU_TYPE.BOOK) {
//            getDataBook(listener);
//        } else if (type == AppConstant.SUB_MENU_TYPE.STORY) {
//            getDataStory(listener);
//        }
//    }

    /**
     * Get list book
     *
     * @param listener
     */
    public void getDataBook(final RPCListener listener) {
        if (dataBook != null) {
            listener.onSuccess(dataBook);
        } else {
            String data = settingPrefs.listdataBook().get();
            if (data.isEmpty()) {
                requestDataBook(listener);
            } else if (isLoadData(settingPrefs.timeGetDataBook().get())) {
                requestDataBook(listener);
            } else {
                parseDataBook(data, listener);
            }
        }
    }

    /**
     * Request list Book from Server
     *
     * @param listener
     */
    public void requestDataBook(final RPCListener listener) {
        String url = AppConstant.URL_LIST_DATA_BOOK;
        mContext.mRpc.getListData(url, new RPCListener() {
            @Override
            public void onError(int code, String error) {
                String data = settingPrefs.listdataBook().get();
                if (data.isEmpty()) {
                    listener.onError(code, error);
                } else {
                    parseDataBook(data, listener);
                }
            }

            @Override
            public void onSuccess(Object object) {
                String data = (String) object;
                settingPrefs.edit().listdataBook().put(data).timeGetDataBook().put(Calendar.getInstance().getTimeInMillis()).apply();
                parseDataBook(data, listener);
            }
        });
    }

    /**
     * Parse response to list book
     *
     * @param data
     * @param listener
     */
    void parseDataBook(final String data, final RPCListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataBook = DataBookModel.paserDataFromServer(data);
                listener.onSuccess(dataBook);
            }
        }).start();
    }

    /**
     * Get list story
     *
     * @param listener
     */
    public void getDataStory(final RPCListener listener) {
        if (dataStory != null) {
            listener.onSuccess(dataStory);
        } else {
            String data = settingPrefs.listdataStory().get();
            if (data.isEmpty()) {
                requestDataStory(listener);
            } else if (isLoadData(settingPrefs.timeGetDataStory().get())) {
                requestDataStory(listener);
            } else {
                parserListDataStory(data, listener);
            }
        }
    }

    /**
     * Request get list story from Server
     *
     * @param listener
     */
    public void requestDataStory(final RPCListener listener) {
        String url = AppConstant.URL_LIST_DATA_STORY;
        mContext.mRpc.getListData(url, new RPCListener() {
            @Override
            public void onError(int code, String error) {
                String data = settingPrefs.listdataStory().get();
                if (data.isEmpty()) {
                    listener.onError(code, error);
                } else {
                    parserListDataStory(data, listener);
                }
            }

            @Override
            public void onSuccess(Object object) {
                String data = (String) object;
                settingPrefs.edit().listdataStory().put(data).timeGetDataStory().put(Calendar.getInstance().getTimeInMillis()).apply();
                parserListDataStory(data, listener);
            }
        });
    }

    /**
     * Parse response to list story
     *
     * @param data
     * @param listener
     */
    void parserListDataStory(final String data, final RPCListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataStory = DataStoryModel.paserDataFromServer(data);
                listener.onSuccess(dataStory);
            }
        }).start();
    }

    /**
     * Get list data at Home
     *
     * @param listener
     */
    public void getDataHome(final RPCListener listener) {
        if (dataHome != null) {
            listener.onSuccess(dataHome);
        } else {
            String data = settingPrefs.listdataHome().get();
            if (data.isEmpty()) {
                requestDataHome(listener);
            } else if (isLoadData(settingPrefs.timeGetDataHome().get())) {
                requestDataHome(listener);
            } else {
                parserListDataHome(data, listener);
            }
        }
    }

    /**
     * Request get list data at Home from Server
     *
     * @param listener
     */
    public void requestDataHome(final RPCListener listener) {
        String url = AppConstant.URL_LIST_DATA_HOME;
        mContext.mRpc.getListData(url, new RPCListener() {
            @Override
            public void onError(int code, String error) {
                String data = settingPrefs.listdataHome().get();
                if (data.isEmpty()) {
                    listener.onError(code, error);
                } else {
                    parserListDataHome(data, listener);
                }
            }

            @Override
            public void onSuccess(Object object) {
                String data = (String) object;
                settingPrefs.edit().listdataHome().put(data).timeGetDataHome().put(Calendar.getInstance().getTimeInMillis()).apply();
                parserListDataHome(data, listener);
            }
        });
    }

    /**
     * Parse response to data Home
     *
     * @param listener
     */
    void parserListDataHome(final String data, final RPCListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataHome = DataHomeModel.paserDataFromServer(data);

                listener.onSuccess(dataHome);
            }
        }).start();
    }

    /**
     * check space time load data
     *
     * @param sTime
     * @return
     */
    boolean isLoadData(long sTime) {
        if (sTime <= 0) {
            return true;
        } else {
            DateTime start = new DateTime(sTime);
            DateTime end = new DateTime(Calendar.getInstance().getTimeInMillis());
            //int countDay = Days.daysBetween(start, end).getDays();
            
            int countHour = Hours.hoursBetween(start, end).getHours();
            Logger.i("time Load Data", countHour + "");

            // after 3h then reload
            if (countHour > 3 || countHour < 0)
                return true;
            else
                return false;
        }
    }
}
