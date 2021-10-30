package com.project.prm391.shoesstore.Services;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.project.prm391.shoesstore.DAO.IProductDao;
import com.project.prm391.shoesstore.Entity.Product;
import com.project.prm391.shoesstore.Entity.User;
import com.project.prm391.shoesstore.Exceptions.AuthenticationException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WishListService implements IWishListService {
    private final IUserService userService;
    private final IProductDao productDao;

    public WishListService(IUserService userService, IProductDao productDao) {
        this.userService = userService;
        this.productDao = productDao;
    }

    @Override
    public Task<List<Product>> getCurrentUserWishList() {
        return getCurrentUserWishList(false);
    }

    @Override
    public Task<List<Product>> getCurrentUserWishList(boolean fetchProducts) {
        User user = userService.getCurrentUserProfile();
        if (user == null) {
            return Tasks.forException(new AuthenticationException());
        }
        return productDao
                .getUserWishList(user)
                .continueWithTask(task -> {
                    List<Product> products = task.getResult();
                    if (fetchProducts) {
                        List<Task<Boolean>> taskList = products.stream().map(product -> productDao.fetchProduct(product)).collect(Collectors.toList());
                        return Tasks.whenAllSuccess(taskList).continueWith(aggregateTask -> {
                            List<Boolean> results = aggregateTask.getResult().stream().map(o -> (boolean) o).collect(Collectors.toList());
                            List<Product> fetchedProducts = IntStream.range(0, results.size()).filter(i -> results.get(i)).mapToObj(i -> products.get(i)).collect(Collectors.toList());
                            return fetchedProducts;
                        });
                    } else {
                        return Tasks.forResult(products);
                    }
                });
    }

    @Override
    public Task<Boolean> checkIfProductInCurrentUserWishList(Product product) {
        User user = userService.getCurrentUserProfile();
        if (user == null) {
            return Tasks.forException(new AuthenticationException());
        }
        return productDao.checkIfProductInUserWishList(user, product);
    }

    @Override
    public Task<Boolean> addProductToCurrentUserWishList(Product product) {
        User user = userService.getCurrentUserProfile();
        if (user == null) {
            return Tasks.forException(new AuthenticationException());
        }
        return productDao.addProductToUserWishList(user, product);
    }

    @Override
    public Task<Boolean> removeProductFromUserWishList(Product product) {
        User user = userService.getCurrentUserProfile();
        if (user == null) {
            return Tasks.forException(new AuthenticationException());
        }
        return productDao.removeProductFromUserWishList(user, product);
    }
}
