package com.project.prm391.shoesstore.Views.PaymentView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.prm391.shoesstore.Entity.BankAccountInformation;
import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.PaymentMethod;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Utils.Strings;
import com.project.prm391.shoesstore.Views.HomeView.HomeActivity;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by duytq on 3/15/2018.
 */

public class OrderSuccessfulActivity extends AppCompatActivity implements View.OnClickListener {
    private final IDataCacheService dataCacheService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;
    @BindView(R.id.lvOrderItems)
    ListView lvOrderItems;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPaymentMethod)
    TextView tvPaymentMethod;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.layoutBankTransfer)
    ViewGroup layoutBankTransfer;
    @BindView(R.id.tvAccountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.tvAccountHolder)
    TextView tvAccountHolder;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvTransferAmount)
    TextView tvTransferAmount;
    @BindView(R.id.tvTransferContent)
    TextView tvTransferContent;
    @BindView(R.id.btnHome)
    Button btnHome;

    public OrderSuccessfulActivity() {
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_successful_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra("order");
        if (order != null) {
            displayOrder(order);
        } else {
            backToHomeActivity();
        }
    }

    private void displayOrder(Order order) {
        tvOrderId.setText(order.getId());
        tvTimestamp.setText(Strings.formatTime(order.getTimestamp()));
        displayOrderItems(order);
        tvTotalPrice.setText(Strings.formatPrice(order.getTotalPrice()));
        tvName.setText(order.getDeliveryInfo().getName());
        tvEmail.setText(order.getDeliveryInfo().getEmail());
        tvPhoneNumber.setText(order.getDeliveryInfo().getPhoneNumber());
        tvAddress.setText(order.getDeliveryInfo().getAddress());
        switch (order.getPaymentMethod()) {
            case CASH_ON_DELIVERY:
                tvPaymentMethod.setText(R.string.label_cash_on_delivery);
                break;
            case BANK_TRANSFER:
                tvPaymentMethod.setText(R.string.label_bank_transfer);
                break;
        }
        tvMessage.setText(order.getMessage());
        if (order.getPaymentMethod() == PaymentMethod.BANK_TRANSFER) {
            layoutBankTransfer.setVisibility(View.VISIBLE);
            BankAccountInformation accountInfo = dataCacheService.getStoreInformation().getBankAccountInformation();
            tvAccountNumber.setText(accountInfo.getAccountNumber());
            tvAccountHolder.setText(accountInfo.getAccountHolderName());
            tvBankName.setText(accountInfo.getBankName());
            tvTransferAmount.setText(Strings.formatPrice(order.getTotalPrice()));
            tvTransferContent.setText(String.format("Payment of order %s.", order.getId()));
        } else {
            layoutBankTransfer.setVisibility(View.GONE);
        }
    }

    private void displayOrderItems(Order order) {
        List<String> orderItemStrings = order.getOrderItems()
                .stream()
                .map(item -> String.format("%d x %s", item.getQuantity(), item.getProduct().getName()))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_narrow, orderItemStrings);
        lvOrderItems.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToHomeActivity();
    }

    @OnClick(R.id.btnHome)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                backToHomeActivity();
                break;
        }
    }

    private void backToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
