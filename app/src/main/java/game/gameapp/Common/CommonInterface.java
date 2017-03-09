package game.gameapp.Common;

import android.content.Context;
import android.view.View;

public interface CommonInterface {
    void deleteItem(int i);

    Context getAdapterApplicationContext();

    void onListItemClick(View view);
}
