package com.project.prm391.shoesstore.Views.PaymentView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.PaymentMethod;
import com.project.prm391.shoesstore.Presenter.PlaceOrderPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by duytq on 3/14/2018.
 */

public class PlaceOrderActivity extends AppCompatActivity implements PlaceOrderView, View.OnClickListener, View.OnFocusChangeListener {
    private final PlaceOrderPresenter presenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.rdoCashOnDelivery)
    RadioButton rdoCashOnDelivery;
    @BindView(R.id.rdoBankTransfer)
    RadioButton rdoBankTransfer;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPhoneNumber)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    @BindView(R.id.tilMessage)
    TextInputLayout tilMessage;
    @BindView(R.id.btnSubmitOrder)
    Button btnSubmitOrder;
    private ProgressDialog progressDialog;

    public PlaceOrderActivity() {
        this.presenter = new PlaceOrderPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter.loadDeliveryInformation();
        setInputPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.rdoCashOnDelivery, R.id.rdoBankTransfer, R.id.btnSubmitOrder})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdoCashOnDelivery:
                setInputPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
                break;
            case R.id.rdoBankTransfer:
                setInputPaymentMethod(PaymentMethod.BANK_TRANSFER);
                break;
            case R.id.btnSubmitOrder:
                presenter.submitOrder();
                break;
        }
    }

    @OnFocusChange({R.id.etName, R.id.etEmail, R.id.etPhoneNumber, R.id.etAddress, R.id.etMessage})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etName:
                if (!hasFocus) {
                    presenter.validateNameAndNotifyError();
                }
                break;
            case R.id.etEmail:
                if (!hasFocus) {
                    presenter.validateEmailAndNotifyError();
                }
                break;
            case R.id.etPhoneNumber:
                if (!hasFocus) {
                    presenter.validatePhoneNumberAndNotifyError();
                }
                break;
            case R.id.etAddress:
                if (!hasFocus) {
                    presenter.validateAddressAndNotifyError();
                }
                break;
            case R.id.etMessage:
                if (!hasFocus) {
                    presenter.validateMessageAndNotifyError();
                }
                break;
        }
    }

    @Override
    public String getInputName() {
        return etName.getText().toString();
    }

    @Override
    public void setInputName(String name) {
        etName.setText(name);
    }

    @Override
    public String getInputEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void setInputEmail(String email) {
        etEmail.setText(email);
    }

    @Override
    public String getInputPhoneNumber() {
        return etPhoneNumber.getText().toString();
    }

    @Override
    public void setInputPhoneNumber(String phoneNumber) {
        etPhoneNumber.setText(phoneNumber);
    }

    @Override
    public String getInputAddress() {
        return etAddress.getText().toString();
    }

    @Override
    public void setInputAddress(String address) {
        etAddress.setText(address);
    }

    @Override
    public String getInputMessage() {
        return etMessage.getText().toString();
    }

    @Override
    public void setInputMessage(String message) {
        etMessage.setText(message);
    }

    @Override
    public PaymentMethod getInputPaymentMethod() {
        return rdoCashOnDelivery.isChecked() ? PaymentMethod.CASH_ON_DELIVERY : PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public void setInputPaymentMethod(PaymentMethod paymentMethod) {
        rdoCashOnDelivery.setChecked(paymentMethod == PaymentMethod.CASH_ON_DELIVERY);
        rdoBankTransfer.setChecked(paymentMethod == PaymentMethod.BANK_TRANSFER);
    }

    @Override
    public void notifyInputNameError(String error) {
        Gui.showInputError(tilName, error);
    }

    @Override
    public void notifyInputEmailError(String error) {
        Gui.showInputError(tilEmail, error);
    }

    @Override
    public void notifyInputPhoneNumberError(String error) {
        Gui.showInputError(tilPhoneNumber, error);
    }

    @Override
    public void notifyInputAddressError(String error) {
        Gui.showInputError(tilAddress, error);
    }

    @Override
    public void notifyInputMessageError(String error) {
        Gui.showInputError(tilMessage, error);
    }

    @Override
    public void notifyPlaceOrderSuccessful(Order order) {
        Intent intent = new Intent(this, OrderSuccessfulActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    @Override
    public void notifyPlaceOrderFailed(String error) {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception e) {
        notificationService.displayToastForException(e);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_submit_order), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishActivity(boolean success) {
        setResult(success ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        finish();
    }
}
