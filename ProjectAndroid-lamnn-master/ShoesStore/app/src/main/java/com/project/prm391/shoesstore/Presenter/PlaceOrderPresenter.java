package com.project.prm391.shoesstore.Presenter;

import android.content.res.Resources;
import android.util.Log;

import com.project.prm391.shoesstore.Entity.Order;
import com.project.prm391.shoesstore.Entity.PaymentMethod;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Enum.InputValidationResult;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.IDataCacheService;
import com.project.prm391.shoesstore.Services.IOrderService;
import com.project.prm391.shoesstore.Services.IUserService;
import com.project.prm391.shoesstore.Utils.InputValidation;
import com.project.prm391.shoesstore.Views.PaymentView.PlaceOrderView;

/**
 * Created by duytq on 3/24/2018.
 */

public class PlaceOrderPresenter {
    private static final String TAG = PlaceOrderPresenter.class.getName();

    private final PlaceOrderView view;
    private final IOrderService orderService;
    private final IUserService userService;
    private final IDataCacheService dataCacheService;
    private final Resources resources;

    public PlaceOrderPresenter(PlaceOrderView view) {
        this.view = view;
        this.orderService = ServiceManager.getInstance().getOrderService();
        this.userService = ServiceManager.getInstance().getUserService();
        this.dataCacheService = ServiceManager.getInstance().getDataCacheService();
        this.resources = ServiceManager.getInstance().getApplicationContext().getResources();
    }

    private boolean checkIfCanPlaceOrder() {
        return userService.isSignedIn()
                && userService.getCurrentUserProfile() != null
                && !dataCacheService.getCart().getCartItems().isEmpty();
    }

    public void submitOrder() {
        if (!checkIfCanPlaceOrder()) {
            view.finishActivity(false);
            return;
        }

        if (validateNameAndNotifyError() == InputValidationResult.VALID
                && validateEmailAndNotifyError() == InputValidationResult.VALID
                && validatePhoneNumberAndNotifyError() == InputValidationResult.VALID
                && validateAddressAndNotifyError() == InputValidationResult.VALID
                && validateMessageAndNotifyError() == InputValidationResult.VALID) {
            User user = userService.getCurrentUserProfile();
            String name = view.getInputName();
            String email = view.getInputEmail();
            String phoneNumber = view.getInputPhoneNumber();
            String address = view.getInputAddress();
            PaymentMethod paymentMethod = view.getInputPaymentMethod();
            String message = view.getInputMessage();

            Order order = new Order();
            order.setUser(user);
            order.getDeliveryInfo().setName(name);
            order.getDeliveryInfo().setEmail(email);
            order.getDeliveryInfo().setPhoneNumber(phoneNumber);
            order.getDeliveryInfo().setAddress(address);
            order.setPaymentMethod(paymentMethod);
            order.setMessage(message);

            dataCacheService.getCart().getCartItems().forEach(cartItem -> order.addOrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getCurrentPrice())
            );

            view.showProgressDialog();
            orderService.createOrder(order).addOnCompleteListener(task -> {
                view.hideProgressDialog();
                if (task.isSuccessful()) {
                    dataCacheService.getCart().clearCartItems();
                    view.notifyPlaceOrderSuccessful(order);
                } else {
                    Exception e = task.getException();
                    Log.e(TAG, null, e);
                    view.notifyPlaceOrderFailed(resources.getString(R.string.msg_place_order_fail));
                    view.notifyException(e);
                }
            });
        }
    }

    public void loadDeliveryInformation() {
        if (!checkIfCanPlaceOrder()) {
            view.finishActivity(false);
            return;
        }
        User user = userService.getCurrentUserProfile();
        view.setInputName(user.getName());
        view.setInputEmail(user.getEmail());
        view.setInputPhoneNumber(user.getPhoneNumber());
        view.setInputAddress(user.getAddress());
    }

    public InputValidationResult validateNameAndNotifyError() {
        String name = view.getInputName();
        InputValidationResult result = InputValidation.validateName(name);
        switch (result) {
            case VALID:
                view.notifyInputNameError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputNameError(resources.getString(R.string.msg_input_name_empty));
                break;
        }
        return result;
    }

    public InputValidationResult validateEmailAndNotifyError() {
        String email = view.getInputEmail();
        InputValidationResult result = InputValidation.validateEmail(email);
        switch (result) {
            case VALID:
                view.notifyInputEmailError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputEmailError(resources.getString(R.string.msg_input_email_empty));
                break;
            case INVALID_EMAIL:
                view.notifyInputEmailError(resources.getString(R.string.msg_input_email_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validatePhoneNumberAndNotifyError() {
        String phoneNumber = view.getInputPhoneNumber();
        InputValidationResult result = InputValidation.validatePhoneNumber(phoneNumber);
        switch (result) {
            case VALID:
                view.notifyInputPhoneNumberError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputPhoneNumberError(resources.getString(R.string.msg_input_phone_number_empty));
                break;
            case INVALID_PHONE_NUMBER:
                view.notifyInputPhoneNumberError(resources.getString(R.string.msg_input_phone_number_invalid));
                break;
        }
        return result;
    }

    public InputValidationResult validateAddressAndNotifyError() {
        String address = view.getInputAddress();
        InputValidationResult result = InputValidation.validateAddress(address);
        switch (result) {
            case VALID:
                view.notifyInputAddressError(null);
                break;
            case EMPTY_STRING:
                view.notifyInputAddressError(resources.getString(R.string.msg_input_address_empty));
                break;
        }
        return result;
    }

    public InputValidationResult validateMessageAndNotifyError() {
        return InputValidationResult.VALID;
    }
}
