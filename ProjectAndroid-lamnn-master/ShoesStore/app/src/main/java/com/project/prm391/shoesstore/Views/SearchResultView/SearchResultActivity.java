package com.project.prm391.shoesstore.Views.SearchResultView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.project.prm391.shoesstore.Adapter.VerticalListProductAdapter;
import com.project.prm391.shoesstore.Configs.RequestCode;
import com.project.prm391.shoesstore.Entity.Brand;
import com.project.prm391.shoesstore.Entity.Category;
import com.project.prm391.shoesstore.Entity.FilterObject;
import com.project.prm391.shoesstore.Entity.Gender;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Presenter.SearchResultPresenter;
import com.project.prm391.shoesstore.R;
import com.project.prm391.shoesstore.ServiceManager;
import com.project.prm391.shoesstore.Services.INotificationService;
import com.project.prm391.shoesstore.Utils.Gui;
import com.project.prm391.shoesstore.Views.ProductDetailView.ProductDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;


/**
 * Created by lamtu on 2018-03-03.
 */

public class SearchResultActivity extends AppCompatActivity implements SearchResultView, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final SearchResultPresenter searchResultPresenter;
    private final INotificationService notificationService;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spnSort)
    Spinner spnSort;
    @BindView(R.id.rvSearchResult)
    RecyclerView rvSearchResult;
    @BindView(R.id.btnFilterShow)
    Button btnFilterShow;
    @BindView(R.id.layoutSearchResultEmpty)
    ViewGroup layoutSearchResultEmpty;
    @BindView(R.id.layoutSearchResultNotEmpty)
    ViewGroup layoutSearchResultNotEmpty;
    private ProgressDialog progressDialog;

    public SearchResultActivity() {
        this.searchResultPresenter = new SearchResultPresenter(this);
        this.notificationService = ServiceManager.getInstance().getNotificationService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchResultPresenter.initializeSortActions();

        Intent intent = getIntent();
        Brand brand = (Brand) intent.getSerializableExtra("brand");
        Category category = (Category) intent.getSerializableExtra("category");
        Gender gender = (Gender) intent.getSerializableExtra("gender");
        String keyword = intent.getStringExtra("keyword");

        searchResultPresenter.setKeyword(keyword);
        searchResultPresenter.setFilters(new FilterObject(category, brand, gender, null, null));

        searchResultPresenter.searchProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mitRefresh) {
            searchResultPresenter.searchProducts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemSelected(R.id.spnSort)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSort:
                searchResultPresenter.setSortActionIndex(position);
                searchResultPresenter.sortProducts();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @OnClick(R.id.btnFilterShow)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilterShow: {
                startFilterActivity();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.FILTER_SEARCH_RESULT) {
            if (resultCode == RESULT_OK) {
                FilterObject filters = (FilterObject) data.getSerializableExtra("filters");
                if (filters != null) {
                    searchResultPresenter.setFilters(filters);
                    searchResultPresenter.searchProducts();
                }
            }
        }
    }

    @Override
    public void initializeSortSpinner(List<String> sortActions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortActions);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnSort.setAdapter(adapter);
    }

    @Override
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            layoutSearchResultEmpty.setVisibility(View.VISIBLE);
            layoutSearchResultNotEmpty.setVisibility(View.GONE);
        } else {
            layoutSearchResultEmpty.setVisibility(View.GONE);
            layoutSearchResultNotEmpty.setVisibility(View.VISIBLE);

            VerticalListProductAdapter adapter = new VerticalListProductAdapter(this, products, this::startProductDetailActivity);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvSearchResult.setLayoutManager(layoutManager);
            rvSearchResult.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void startProductDetailActivity(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }


    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = Gui.buildIndeterminateProgressDialog(this, getString(R.string.label_searching), getString(R.string.msg_please_wait));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void notifyException(Exception ex) {
        notificationService.displayToastForException(ex);
    }

    @Override
    public void startFilterActivity() {
        Intent iReview = new Intent(this, FilterActivity.class);
        iReview.putExtra("filters", searchResultPresenter.getFilters());
        startActivityForResult(iReview, RequestCode.FILTER_SEARCH_RESULT);
    }
}
