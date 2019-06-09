package com.example.swap.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.GoodDetailsActivity;
import com.example.swap.R;
import com.example.swap.adapters.GoodsListAdapter;
import com.example.swap.models.Good;
import com.example.swap.rest.NetworkState;
import com.example.swap.viewmodels.GoodsFetcherViewModel;
import com.example.swap.viewmodels.GoodsListViewModel;
import com.example.swap.viewmodels.factories.GoodsListViewModelFactory;

public class GoodsListFragment extends Fragment {
    public static final String CHOSEN_GOOD = "choosen good";

    private LiveData<PagedList<Good>> goodsPagedList;
    private LiveData<NetworkState> loadInitialNetworkState;
    private LiveData<NetworkState> loadAfterNetworkState;

    private GoodsListAdapter adapter;

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
                .of(getActivity(), new GoodsListViewModelFactory(goodsFetcherViewModel.getGoodsFetcherLiveData().getValue()))
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

        adapter = new GoodsListAdapter(getDiffCallback(), getContext());
        goodsPagedList.observe(this, adapter::submitList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = v.findViewById(R.id.goods_recyclerview);
        recyclerView.setAdapter(adapter);
        adapter.setGoodItemOnClickListener(new OnGoodItemClickListener());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);

        LayoutAnimationController animationController = AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_rise_from_bottom);
        recyclerView.setLayoutAnimation(animationController);

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
        Log.d("GoodsListFragment", networkState.getStatus().toString());
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

    private class OnGoodItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)v.getTag();
            Good chosenGood = adapter.getCurrentList().get(viewHolder.getAdapterPosition());

            Toast.makeText(
                    getContext(),
                    chosenGood.getSupplementaryImageFileNames().get(0),
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(getActivity(), GoodDetailsActivity.class);
            intent.putExtra(CHOSEN_GOOD, chosenGood);
            startActivity(intent);
        }
    }

}
