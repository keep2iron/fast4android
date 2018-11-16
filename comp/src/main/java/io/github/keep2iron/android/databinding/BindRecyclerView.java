package io.github.keep2iron.android.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.github.keep2iron.android.adapter.WrapperVirtualLayoutManager;
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter;

public class BindRecyclerView {

    @BindingAdapter("bundle")
    public static void bindRecyclerView(
            RecyclerView recyclerView,
            ListBundle refreshBundle
    ) {
        if (refreshBundle == null) {
            return;
        }

        DelegateAdapter delegateAdapter = refreshBundle.getDelegateAdapter();
        ArrayList<DelegateAdapter.Adapter> adapters = new ArrayList<>(refreshBundle.getAdapters());
        adapters.add(refreshBundle.buildAdapter(recyclerView).getLoadMoreAdapter());
        delegateAdapter.addAdapters(adapters);

        recyclerView.setRecycledViewPool(refreshBundle.recyclerPool());
        recyclerView.setLayoutManager(refreshBundle.getVirtualLayoutManager());
        recyclerView.setAdapter(delegateAdapter);
    }

}
