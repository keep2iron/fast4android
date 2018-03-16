package io.github.keep2iron.android.comp;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * @author keep2iron <a href="http://keep2iron.github.io">Contract me.</a>
 * @version 1.0
 * @since 2017/10/13 11:44
 */
public interface IComponentLifeCycle {

    /**
     * 当Application 创建的时候进行调用
     *
     * @param application   application对象
     */
    void onAttachApp(@NonNull Application application);

    /**
     * 在中间进行注册服务
     * @param componentManager
     */
    void onRegisterService(@NonNull ComponentManager componentManager);

    void onDetach(@NonNull Application application);
}