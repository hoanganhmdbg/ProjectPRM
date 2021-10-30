package com.project.prm391.shoesstore.Views.ProductDetailView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.project.prm391.shoesstore.Adapter.ReviewAdapter;
import com.project.prm391.shoesstore.Adapter.ViewPagerImageSliderAdapter;
import com.project.prm391.shoesstore.Configs.Constants;
import com.project.prm391.shoesstore.Configs.RequestCode;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.ProductReview;
import com.project.prm391.shoesstore.Presenter.ProductDetailPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Utils.Strings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lamtu on 2018-02-28.
 */

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailView, ViewPager.OnPageChangeListener, View.OnClickListener {
    private final List<TextView> dotSliderTextViews = new ArrayList<>();
    private final List<Fragment> productImageFragments = new ArrayList<>();
    private final ProductDetailPresenter productDetailPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vpProductImages)
    ViewPager vpProductImages;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.layoutAvailable)
    ViewGroup layoutAvailable;
    @BindView(R.id.layoutNotAvailable)
    ViewGroup layoutNotAvailable;
    @BindView(R.id.tvCurrentPrice)
    TextView tvCurrentPrice;
    @BindView(R.id.layoutDiscount)
    LinearLayout layoutDiscount;
    @BindView(R.id.tvOriginalPrice)
    TextView tvOriginalPrice;
    @BindView(R.id.tvDiscountRate)
    TextView tvDiscountRate;
    @BindView(R.id.rbAverageRatings)
    RatingBar rbAverageRatings;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvBrand)
    TextView tvBrand;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.tvDescription)
    ExpandableTextView tvDescription;
    @BindView(R.id.pbLoadReviews)
    ProgressBar pbLoadReviews;
    @BindView(R.id.btnDoReview)
    Button btnDoReview;
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;
    @BindView(R.id.tvShowAllReviews)
    TextView tvShowAllReviews;
    @BindView(R.id.layoutAddToCart)
    ViewGroup layoutAddToCart;
    @BindView(R.id.tvAddToCart)
    TextView tvAddToCart;
    @BindView(R.id.layoutAddRemoveFavorite)
    ViewGroup layoutAddRemoveFavorite;
    @BindView(R.id.ivAddRemoveFavorite)
    ImageView ivAddRemoveFavorite;
    @BindView(R.id.pbLoadFavorite)
    ProgressBar pbLoadFavorite;

    public ProductDetailActivity() {
        this.productDetailPresenter = new ProductDetailPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);
        ButterKnife.bind(this);
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");

        productDetailPresenter.loadProduct(product);
        productDetailPresenter.checkIfProductInWishList();

        vpProductImages.addOnPageChangeListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updateDotSlider(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void displayProduct(Product product) {
        displayProductImages(product);
        displayProductInfo(product);
    }

    private void createAndAddImageFragment(String imageUrl, List<Fragment> fragments) {
        ProductImageFragment fragment = new ProductImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", imageUrl);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

    private void displayProductImages(Product product) {
        productImageFragments.clear();
        if (product.getImageUrls().isEmpty()) {
            createAndAddImageFragment(null, productImageFragments);
        } else {
            product.getImageUrls().forEach(imageUrl -> createAndAddImageFragment(imageUrl, productImageFragments));
        }
        ViewPagerImageSliderAdapter adapter = new ViewPagerImageSliderAdapter(getSupportFragmentManager(), productImageFragments);
        vpProductImages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateDotSlider(0);
    }

    private void displayProductInfo(Product product) {
        setTitle(product.getName());
        tvName.setText(product.getName());
        if (product.isAvailable()) {
            layoutNotAvailable.setVisibility(View.INVISIBLE);
            layoutAvailable.setVisibility(View.VISIBLE);
            tvCurrentPrice.setText(Strings.formatPrice(product.getCurrentPrice()));
            tvOriginalPrice.setText(Strings.formatPrice(product.getOriginalPrice()));
            tvDiscountRate.setText(Strings.formatDiscountRate(product.getDiscountRate()));

            if (product.getDiscountRate() == 0) {
                layoutDiscount.setVisibility(View.INVISIBLE);
            } else {
                layoutDiscount.setVisibility(View.VISIBLE);
                if (product.getDiscountRate() == 1) {
                    tvDiscountRate.setText(getString(R.string.label_free));
                }
            }
        } else {
            layoutNotAvailable.setVisibility(View.VISIBLE);
            layoutAvailable.setVisibility(View.INVISIBLE);
        }

        displayAverageRatings(product.getAverageRatings());

        tvCategory.setText(product.getCategory().getName());
        tvBrand.setText(product.getBrand().getName());
        tvGender.setText(product.getGender().getName());
        tvSize.setText(String.format("%.1f US", product.getSize()));

        tvDescription.setText(product.getDescription());

        layoutAddToCart.setEnabled(product.isAvailable());
        tvAddToCart.setText(product.isAvailable() ? getString(R.string.label_add_to_cart) : getString(R.string.label_not_available));
    }

    @Override
    public void notifyAddToCartSuccess() {
        notificationService.displayToast(getString(R.string.msg_add_cart_success));
    }

    @Override
    public void notifyAddToCartFailed(String error) {
        notificationService.displayToast(error);
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void toggleLoadReviewsProgressBar(boolean show) {
        pbLoadReviews.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleReviewButtonLabel(boolean hasReviewed) {
        btnDoReview.setText(hasReviewed ? getString(R.string.label_update_review) : getString(R.string.label_create_review));
    }

    @Override
    public void toggleReviewButtonEnabled(boolean enabled) {
        btnDoReview.setEnabled(enabled);
    }

    @Override
    public void toggleAddRemoveFavoriteButton(Boolean action) {
        if (action != null) {
            ivAddRemoveFavorite.setImageResource(action ? R.drawable.ic_favorite_add_black : R.drawable.ic_favorite_remove_black);
        } else {
            ivAddRemoveFavorite.setImageResource(R.drawable.ic_favorite_border_black);
        }
    }

    @Override
    public void toggleAddRemoveFavoriteProgressBar(boolean show) {
        if (show) {
            ivAddRemoveFavorite.setVisibility(View.INVISIBLE);
            pbLoadFavorite.setVisibility(View.VISIBLE);
        } else {
            pbLoadFavorite.setVisibility(View.INVISIBLE);
            ivAddRemoveFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void startDoReviewActivity(ProductReview review) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("review", review);
        intent.putExtra("actionTitle", btnDoReview.getText().toString());
        startActivityForResult(intent, RequestCode.DO_REVIEW);
    }

    @Override
    public void notifyReviewRequireSignIn() {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), getString(R.string.msg_review_require_sign_in)).show();
    }

    @Override
    public void notifyFavoriteActionRequireSignIn() {
        Gui.buildSimpleAlertDialog(this, getString(R.string.label_error), getString(R.string.msg_favorite_action_require_sign_in)).show();
    }

    @Override
    public void notifyFavoriteActionSuccessful(String message) {
        notificationService.displayToast(message);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void displayProductReviews(List<ProductReview> reviews, int limit) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews, limit);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        tvShowAllReviews.setVisibility(reviews.size() > limit ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayAverageRatings(Double averageRatings) {
        rbAverageRatings.setRating(averageRatings == null ? 0.0f : averageRatings.floatValue());
    }


    private void updateDotSlider(int position) {
        dotSliderTextViews.clear();
        layoutDots.removeAllViews();

        for (int i = 0; i < productImageFragments.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(Html.fromHtml("&#8226;"));
            textView.setTextSize(40);
            textView.setTextColor(i == position ? getColorFromId(R.color.colorSliderActive) : getColorFromId(R.color.colorSliderInactive));
            layoutDots.addView(textView);
        }
    }

    private int getColorFromId(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.DO_REVIEW) {
            if (resultCode == RESULT_OK) {
                productDetailPresenter.loadReviews(Constants.REVIEW_LIST_LIMIT);
            }
        }
    }

    @OnClick({R.id.btnDoReview, R.id.tvShowAllReviews, R.id.layoutAddToCart, R.id.layoutAddRemoveFavorite})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDoReview: {
                productDetailPresenter.doReview();
                break;
            }
            case R.id.tvShowAllReviews: {
                productDetailPresenter.loadReviews();
                break;
            }
            case R.id.layoutAddToCart: {
                productDetailPresenter.addToCart();
                break;
            }
            case R.id.layoutAddRemoveFavorite: {
                productDetailPresenter.addRemoveFavorite();
                break;
            }

        }
    }
}