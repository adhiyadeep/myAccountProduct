package skytechhub.myaccounts.AppController;

import android.content.Context;

public class ApplicationPreferences {
    private Context context;
    private String preferenceName="partnerapppref";

    public ApplicationPreferences(Context context)
    {
        this.context=context;

    }

    public String savePreference(String key, String value)
    {

        context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).edit().putString(key,value).commit();
        return key;
    }

    public void savePreference_Int(String key, int value)
    {

        context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }



    public void savePreference_Boolean(String key, boolean value)
    {

        context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }



    public String getPreference(String key, String defaultValue)
    {

       return context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).getString(key,defaultValue);

    }

    public int getPreference_Int(String key, int defaultValue)
    {

        return context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).getInt(key,defaultValue);

    }

    public boolean getPreference_Boolean(String key, boolean defaultValue)
    {

        return context.getSharedPreferences(preferenceName,context.MODE_PRIVATE).getBoolean(key,defaultValue);

    }

}
