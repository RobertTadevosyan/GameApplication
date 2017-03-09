package game.gameapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtil {
    public static void saveInSharedPreference(Context context, String key, Object value) {
        Editor editor = getSharedPreferences(context).edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        }
        editor.apply();
    }

    public static Object readPreference(Context context, String key, Object defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (defaultValue instanceof String) {
            return sharedPreferences.getString(key, (String) defaultValue);
        }
        if (defaultValue instanceof Boolean) {
            return Boolean.valueOf(sharedPreferences.getBoolean(key, ((Boolean) defaultValue).booleanValue()));
        }
        if (defaultValue instanceof Integer) {
            return Integer.valueOf(sharedPreferences.getInt(key, ((Integer) defaultValue).intValue()));
        }
        return null;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
