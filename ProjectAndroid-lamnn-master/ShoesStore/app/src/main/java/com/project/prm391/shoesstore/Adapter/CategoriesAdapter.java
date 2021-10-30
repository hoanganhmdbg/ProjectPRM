package com.project.prm391.shoesstore.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.project.prm391.shoesstore.R;

import java.util.List;
import java.util.Map;


public class CategoriesAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List headers;
    private final Map<Object, List> children;
    private OnMenuItemSelectedListener onMenuItemSelectedListener;

    public CategoriesAdapter(Context context, List headers,
                             Map<Object, List> children) {
        this(context, headers, children, null);
    }

    public CategoriesAdapter(Context context, List headers, Map<Object, List> children, OnMenuItemSelectedListener onMenuItemSelectedListener) {
        this.context = context;
        this.headers = headers;
        this.children = children;
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    @Override
    public int getGroupCount() {
        return this.headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        Object header = getGroup(groupPosition);
        if (view == null) {
            LayoutInflater li = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.categories_expandlistview_header_layout, null);
        }

        TextView tvHeader = view.findViewById(R.id.tvHeader);
        tvHeader.setText(header.toString());

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        Object header = getChild(groupPosition, childPosition);
        Object child = getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater li = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.categories_expandlistview_item_layout, null);
        }

        TextView tvChild = view.findViewById(R.id.tvChild);
        tvChild.setText(child.toString());

        tvChild.setOnClickListener(v -> {
            if (onMenuItemSelectedListener != null) {
                onMenuItemSelectedListener.onMenuItemSelected(header, child);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(Object header, Object child);
    }
}
