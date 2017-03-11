package org.telegram.setting;

import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface SettingPrefs {
    @DefaultString("")
    String listSubMenuStory();

    @DefaultString("")
    String listSubMenuBook();

    @DefaultString("")
    String username();

    @DefaultString("")
    String password();

    @DefaultString("")
    String listdataHome();

    @DefaultString("")
    String listdataBook();

    @DefaultString("")
    String listdataStory();

    @DefaultLong(-1)
    long timeLoadSubMenu();

    @DefaultLong(-1)
    long timeGetDataHome();

    @DefaultLong(-1)
    long timeGetDataBook();

    @DefaultLong(-1)
    long timeGetDataStory();

//    @DefaultString("")
//    String showSearch();
}
