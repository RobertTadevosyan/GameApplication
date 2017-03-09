package game.gameapp.Common;

import android.content.Context;
import android.view.View;

public interface CommonAdapterInterface {
    void onClick(View view);

    void reloadRowWithData(Object obj, int i);

    void setView(View view, CommonInterface commonInterface, Context context);
}
