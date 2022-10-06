package com.globits.da;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    // Check Blank
    public static final String BLANK_REGEX = ".*\\s.*";

    // Json format
    public static final String FORMAT_PATTERN = "dd-MM-yyyy";

    // Excel
    public static final String EMAIL_REGEX = "^[\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
    public static final String PHONE_REGEX = "^\\d{9,12}$";
    public static final String NAME_REGEX = "^[A-Za-z]+((\\s)?((\\'|\\-|\\.)?([A-Za-z])+))*$";
    public static final String AGE_REGEX = "^\\d{1,3}$";
    public static final String CODE_REGEX = "^\\S{6,10}$";
    public static final String DATE_DIPLOMA_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final int MAX_DIPLOMA_NUMBER = 3;
    public static final int MIN_DIPLOMA_NUMBER = 0;
    public static final int COLUMN_NAME = 0;
    public static final int COLUMN_CODE = 1;
    public static final int COLUMN_EMAIL = 2;
    public static final int COLUMN_PHONE = 3;
    public static final int COLUMN_AGE = 4;
    public static final int COLUMN_PROVINCE_ID = 5;
    public static final int COLUMN_DISTRICT_ID = 6;
    public static final int COLUMN_COMMUNE_ID = 7;
    public static final int[] COLUMN_LIST = {COLUMN_NAME, COLUMN_CODE, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_AGE,
            COLUMN_PROVINCE_ID, COLUMN_DISTRICT_ID, COLUMN_COMMUNE_ID};
    public static final String[] CELL_VALUE_LIST = {"Name", "Code", "Email", "Phone", "Age",
            "Province Id", "District Id", "Commune Id"};

    // HTTPServletResponse
    public static final String DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss";
    public static final String CONTENT_TYPE = "application/octet-stream";
    public static final String HEADER_KEY = "Content-Disposition";
    public static final String HEADER_VALUE = "attachment; filename=employee " +
            new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date()) + ".xlsx";

    public enum StaffType {
        Sale(1), // nhân viên bán hàng
        Cashier(2), // nhân viên thu ngân
        Other(3)// khác
        ;

        private final Integer value;

        StaffType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum ChannelAds {// kenh quang cao
        Webiste(1), // website
        Contextual_Advertiser(2), // khen hquang cao
        Social_Netword(3), // mang xa hoi
        Youtube_channel(4)// youtube
        ;

        private final Integer value;

        ChannelAds(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum Social_Netword {// kenh quang cao
        Facebook(1), // website
        Zalo(2), // khen hquang cao
        Tiktok(3), // mang xa hoi
        Other(4)// youtube
        ;

        private final Integer value;

        Social_Netword(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

}
