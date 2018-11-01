package io.github.keep2iron.android.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import java.util.ArrayList;

import io.github.keep2iron.android.adapter.AbstractSubAdapter;
import io.github.keep2iron.android.adapter.WrapperVirtualLayoutManager;
import io.github.keep2iron.android.load.RefreshWithLoadMoreAdapter;

public class BindRecyclerView {

    @BindingAdapter("bundle")
    public static void bindRecyclerView(
            RecyclerView recyclerView,
            RefreshBundle refreshBundle
    ) {
        if (refreshBundle == null) {
            return;
        }

        WrapperVirtualLayoutManager virtualLayoutManager = new WrapperVirtualLayoutManager(recyclerView.getContext().getApplicationContext());
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        ArrayList<DelegateAdapter.Adapter> adapters = new ArrayList<>(refreshBundle.getAdapters());
        adapters.add(new RefreshWithLoadMoreAdapter.Builder(
                recyclerView,
                refreshBundle.getRefreshLayout())
                .setOnLoadListener(refreshBundle.getOnLoad())
                .build());
        delegateAdapter.addAdapters(adapters);

        recyclerView.setRecycledViewPool(refreshBundle.recyclerPool());
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);
    }

}
