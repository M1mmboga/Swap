package com.example.swap.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.adapters.GoodsListAdapter;
import com.example.swap.models.Good;
import com.example.swap.rest.NetworkState;
import com.example.swap.viewmodels.GoodsFetcherViewModel;
import com.example.swap.viewmodels.GoodsListViewModel;
import com.example.swap.viewmodels.factories.GoodsListViewModelFactory;

public class GoodsListFragment extends Fragment {

    private LiveData<PagedList<Good>> goodsPagedList;
    private LiveData<NetworkState> loadInitialNetworkState;
    private LiveData<NetworkState> loadAfterNetworkState;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        String category = getArguments().getString(ListActivity.CATEGORY_CRITERIA);

        /*GoodsByCategoryViewModel model = ViewModelProviders
                .of(this, new GoodsByCategoryViewModelFactory(getActivity().getApplication(), category))
                .get(GoodsByCategoryViewModel.class);*/
        GoodsFetcherViewModel goodsFetcherViewModel = ViewModelProviders
                .of(getActivity())
                .get(GoodsFetcherViewModel.class);
        GoodsListViewModel goodsListViewModel = ViewModelProviders
                .of(this, new GoodsListViewModelFactory(goodsFetcherViewModel.getGoodsFetcherLiveData().getValue()))
                .get(GoodsListViewModel.class);

        this.goodsPagedList = goodsListViewModel.getGoodsPagedList();
        this.loadInitialNetworkState = goodsListViewModel.getLoadInitialNetworkState();
        this.loadAfterNetworkState = goodsListViewModel.getLoadAfterNetworkState();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_list, container, false);

        GoodsListAdapter adapter = new GoodsListAdapter(getDiffCallback(), getContext());
        goodsPagedList.observe(this, adapter::submitList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = v.findViewById(R.id.goods_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        loadInitialNetworkState.observe(this, this::handleNetworkState);
        loadAfterNetworkState.observe(this, adapter::setNetworkState);

        return v;
    }

    private DiffUtil.ItemCallback<Good> getDiffCallback() {
        return new DiffUtil.ItemCallback<Good>() {
            @Override
            public boolean areItemsTheSame(@NonNull Good oldItem, @NonNull Good newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Good oldItem, @NonNull Good newItem) {
                return oldItem.equals(newItem);
            }
        };
    }

    private void handleNetworkState(NetworkState networkState) {
        if(networkState.getStatus() == NetworkState.Status.FAILED) {
            View errorView = getView().findViewById(R.id.retry_error);
            Button retry = (Button) errorView.findViewById(R.id.retry_btn);
            retry.setOnClickListener(v -> {
                errorView.setVisibility(View.GONE);
                networkState.getRetryable().retry();
            });
            errorView.setVisibility(View.VISIBLE);
        }
    }

}
