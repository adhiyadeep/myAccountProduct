package skytechhub.myaccounts.utils;

import android.util.Log;

/**
 * Created by Asiya A Khatib on 3/14/2016.
 */
public class StringUtils {

    public static String splitAddress(String address) {
        String addressVal = "";

        try {

            if (addressVal.contains(",")) {
                String[] spiltAdd = address.split(",");

                for (int k=0;k<spiltAdd.length;k++)
                {


                }

            } else {
                return addressVal;
            }
        } catch (Exception e) {
            Log.e("splitAddress",""+e.getMessage());
        }
        return addressVal;
    }
}
