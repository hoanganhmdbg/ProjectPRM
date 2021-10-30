package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.prm391.shoesstore.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IconMenuAdapter extends ArrayAdapter<IconMenuAdapter.MenuItem> {
    private static final int LAYOUT_RESOURCE_ID = R.layout.row_item_icon_menu_layout;

    private final Context context;
    private final List<MenuItem> menuItems;

    public IconMenuAdapter(Context context, List<MenuItem> menuItems) {
        super(context, LAYOUT_RESOURCE_ID, menuItems);
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(LAYOUT_RESOURCE_ID, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MenuItem menuItem = menuItems.get(position);
        viewHolder.ivIcon.setImageResource(menuItem.getIconResourceId());
        viewHolder.tvLabel.setText(menuItem.getLabel());
        return convertView;
    }

    public static class MenuItem {
        private final int iconResourceId;
        private final String label;

        public MenuItem(int iconResourceId, String label) {
            this.iconResourceId = iconResourceId;
            this.label = label;
        }

        public int getIconResourceId() {
            return iconResourceId;
        }

        public String getLabel() {
            return label;
        }
    }

    static class ViewHolder {
        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.tvLabel)
        TextView tvLabel;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
