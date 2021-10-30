package com.project.prm391.shoesstore.Views.ProductDetailView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Presenter.ReviewPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by lamtu on 2018-03-11.
 */

public class ReviewActivity extends AppCompatActivity implements ReviewView, View.OnClickListener, View.OnFocusChangeListener {
    private final ReviewPresenter reviewPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tilTitle)
    TextInputLayout tilTitle;
    @BindView(R.id.tilContent)
    TextInputLayout tilContent;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.rbRating)
    RatingBar rbRating;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    private ProgressDialog progressDialog;

    public ReviewActivity() {
        this.reviewPresenter = new ReviewPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String actionTitle = intent.getStringExtra("actionTitle");
        ProductReview review = (ProductReview) intent.getSerializableExtra("review");
        reviewPresenter.loadReview(review);
        if (actionTitle != null) {
            setTitle(actionTitle);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btnSubmit)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSubmit: {
                reviewPresenter.submitReview();
                break;
            }
        }
    }

    @OnFocusChange({R.id.etTitle, R.id.etContent})
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etTitle:
                if (!hasFocus) {
                    reviewPresenter.validateTitleAndNotifyError();
                }
                break;
            case R.id.etContent:
                if (!hasFocus) {
                    reviewPresenter.validateContentAndNotifyError();
                }
                break;
        }
    }

    @Override
    public String getInputTitle() {
        return etTitle.getText().toString();
    }

    @Override
    public void setInputTitle(String title) {
        etTitle.setText(title);
    }

    @Override
    public String getInputContent() {
        return etContent.getText().toString();
    }

    @Override
    public void setInputContent(String content) {
        etContent.setText(content);
    }

    @Override
    public int getInputRating() {
        return (int) rbRating.getRating();
    }

    @Override
    public void setInputRating(int rating) {
        rbRating.setRating((float) rating);
    }

    @Override
    public void notifyInputTitleError(String error) {
        Gui.showInputError(tilTitle, error);
    }

    @Override
    public void notifyInputContentError(String error) {
        Gui.showInputError(tilContent, error);
    }

    @Override
    public void notifySubmitReviewSuccess() {
        AlertDialog dialog = Gui.buildSimpleAlertDialog(this, getString(R.string.label_success), getString(R.string.msg_submit_review_success));
        dialog.setOnDismissListener(dialog1 -> finishActivity(true));
        dialog.show();
    }

    @Override
    public void notifySubmitReviewFailed(String error) {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), error).show();
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void showSubmitReviewProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getTitle().toString(), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideSubmitReviewProgressDialog() {
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