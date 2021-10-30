package com.project.prm391.shoesstore.Listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lamtu on 2018-03-03.
 */

public class LoadMoreScroll extends RecyclerView.OnScrollListener {
    private int itemFirst = 0;
    private int itemTotal = 0;
    private int itemLoadBefore = 10;
    private RecyclerView.LayoutManager layoutManager;
    private ILoadMore iLoadMore;

    public LoadMoreScroll(RecyclerView.LayoutManager layoutManager, ILoadMore iLoadMore) {
        this.layoutManager = layoutManager;
        this.iLoadMore = iLoadMore;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        itemTotal = layoutManager.getItemCount();

        if (layoutManager instanceof LinearLayoutManager) {
            itemFirst = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            itemFirst = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if (itemTotal <= (itemFirst + itemLoadBefore)) {
            iLoadMore.LoadMore(itemTotal);
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}