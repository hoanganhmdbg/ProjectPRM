package com.project.prm391.shoesstore.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TooltipCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.OrderDeliveryInfo;
import com.project.prm391.shoesstore.Entity.OrderStatus;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.Utils.Strings;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private final List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText(String.valueOf(order.getId()));
        holder.tvOrderDate.setText(Strings.formatTime(order.getTimestamp()));
        holder.tvTotalPrice.setText(Strings.formatPrice(order.getTotalPrice()));
        switch (order.getPaymentMethod()) {
            case CASH_ON_DELIVERY:
                holder.ivPaymentMethod.setImageResource(R.drawable.ic_money_payment_black);
                TooltipCompat.setTooltipText(holder.ivPaymentMethod, context.getString(R.string.label_cash_on_delivery));
                break;
            case BANK_TRANSFER:
                holder.ivPaymentMethod.setImageResource(R.drawable.ic_credit_card);
                TooltipCompat.setTooltipText(holder.ivPaymentMethod, context.getString(R.string.label_bank_transfer));
                break;
        }
        OrderStatus status = order.getStatus();
        if (status.isCompleted()) {
            holder.layoutCompleted.setVisibility(View.VISIBLE);
            holder.layoutProcessing.setVisibility(View.GONE);
            if (status.isAccepted()) {
                holder.ivCompletionStatus.setImageResource(R.drawable.ic_check_green);
                TooltipCompat.setTooltipText(holder.ivCompletionStatus, context.getString(R.string.msg_order_completed));
            } else {
                holder.ivCompletionStatus.setImageResource(R.drawable.ic_cancel_red);
                TooltipCompat.setTooltipText(holder.ivCompletionStatus, context.getString(R.string.msg_order_rejected));
                holder.tvOrderId.setPaintFlags(holder.tvOrderId.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } else {
            holder.layoutCompleted.setVisibility(View.GONE);
            holder.layoutProcessing.setVisibility(View.VISIBLE);
            holder.ivPaymentStatus.setImageResource(status.isPaid() ? R.drawable.ic_payment_completed_black : R.drawable.ic_payment_waiting_black);
            TooltipCompat.setTooltipText(holder.ivPaymentStatus, status.isPaid() ? context.getString(R.string.msg_payment_made) : context.getString(R.string.msg_payment_waiting));
            switch (status.getDeliveryStatus()) {
                case NOT_YET_DELIVERED:
                    holder.ivDeliveryStatus.setImageResource(R.drawable.ic_box_black);
                    TooltipCompat.setTooltipText(holder.ivDeliveryStatus, context.getString(R.string.msg_not_yet_delivered));
                    break;
                case DELIVERING:
                    holder.ivDeliveryStatus.setImageResource(R.drawable.ic_delivering);
                    TooltipCompat.setTooltipText(holder.ivDeliveryStatus, context.getString(R.string.msg_delivering));
                    break;
                case DELIVERED:
                    holder.ivDeliveryStatus.setImageResource(R.drawable.ic_delivering);
                    TooltipCompat.setTooltipText(holder.ivDeliveryStatus, context.getString(R.string.msg_delivered));
                    break;
            }
        }
        OrderDeliveryInfo deliveryInfo = order.getDeliveryInfo();
        holder.tvName.setText(deliveryInfo.getName());
        holder.tvEmail.setText(deliveryInfo.getEmail());
        holder.tvPhoneNumber.setText(deliveryInfo.getPhoneNumber());
        holder.tvAddress.setText(deliveryInfo.getAddress());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvOrderDate)
        TextView tvOrderDate;
        @BindView(R.id.tvTotalPrice)
        TextView tvTotalPrice;
        @BindView(R.id.ivPaymentMethod)
        ImageView ivPaymentMethod;
        @BindView(R.id.layoutCompleted)
        ViewGroup layoutCompleted;
        @BindView(R.id.ivCompletionStatus)
        ImageView ivCompletionStatus;
        @BindView(R.id.layoutProcessing)
        ViewGroup layoutProcessing;
        @BindView(R.id.ivPaymentStatus)
        ImageView ivPaymentStatus;
        @BindView(R.id.ivDeliveryStatus)
        ImageView ivDeliveryStatus;
        @BindView(R.id.layoutExpand)
        ViewGroup layoutExpand;
        @BindView(R.id.layoutDeliveryInformation)
        ExpandableLayout layoutDeliveryInformation;
        @BindView(R.id.ivExpandStatus)
        ImageView ivExpandStatus;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;
        @BindView(R.id.tvAddress)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layoutExpand)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutExpand:
                    boolean expanded = layoutDeliveryInformation.isExpanded();
                    if (expanded) {
                        ivExpandStatus.setImageResource(R.drawable.ic_expand_more_black);
                        layoutDeliveryInformation.collapse();
                    } else {
                        ivExpandStatus.setImageResource(R.drawable.ic_expand_less_black);
                        layoutDeliveryInformation.expand();
                    }
                    break;
            }
        }
    }
}
