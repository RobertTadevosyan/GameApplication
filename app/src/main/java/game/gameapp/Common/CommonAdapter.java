package game.gameapp.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CommonAdapter extends BaseAdapter {
    CommonInterface commonInterface;
    private Context context;
    private Class<?> holder;
    private LayoutInflater inflater;
    private List list;
    private int resource;

    public CommonAdapter(Context context, CommonInterface commonInterface, List list, Class<?> holderClass, int resource) {
        this.context = context;
        this.list = list;
        this.commonInterface = commonInterface;
        this.inflater = LayoutInflater.from(commonInterface.getAdapterApplicationContext());
        this.holder = holderClass;
        this.resource = resource;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource, parent, false);
            CommonAdapterInterface object = null;
            Constructor constructor = null;
            try {
                constructor = this.holder.getConstructor(new Class[0]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                object = (CommonAdapterInterface) constructor.newInstance(new Object[0]);
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
            object.setView(convertView, this.commonInterface, this.context);
            convertView.setTag(object);
        }
        ((CommonAdapterInterface) convertView.getTag()).reloadRowWithData(this.list.get(position), position);
        return convertView;
    }
}
