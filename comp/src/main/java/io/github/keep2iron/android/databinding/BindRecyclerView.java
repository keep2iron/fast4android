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

        WrapperVirtualLayoutManager virtualLayoutManager = new WrapperVirtualLayoutManager(recyclerView.getContext().getApplicationContext());
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        ArrayList<DelegateAdapter.Adapter> adapters = new ArrayList<>(refreshBundle.getAdapters());
        adapters.add(new RefreshWithLoadMoreAdapter.Builder(
                recyclerView,
                refreshBundle.getRefreshLayout())
                .setOnLoadListener(refreshBundle.getRefreshLoadListener())
                .build());
        delegateAdapter.addAdapters(adapters);

        recyclerView.setRecycledViewPool(refreshBundle.recyclerPool());
        recyclerView.setLayoutManager(virtualLayoutManager);
        recyclerView.setAdapter(delegateAdapter);
    }

}
