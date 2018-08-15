package com.kippinretail.callbacks;

import com.kippinretail.ApplicationuUlity.CheckBoxType;

/**
 * Created by sandeep.singh on 8/1/2016.
 */
public interface CheckBoxClickHandler {
        public void onChechkBoxClick(int id, String folderName, boolean isChecked, CheckBoxType checkboxtype);
}
