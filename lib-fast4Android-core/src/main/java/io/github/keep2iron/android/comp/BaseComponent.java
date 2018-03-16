package io.github.keep2iron.android.comp;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2017/11/24 10:43
 */
public class BaseComponent implements IComponentLifeCycle {
    @Override
    public void onAttachApp(@NonNull Application application) {

    }

    @Override
    public void onRegisterService(@NonNull ComponentManager componentManager) {

    }

    @Override
    public void onDetach(@NonNull Application application) {

    }
}
