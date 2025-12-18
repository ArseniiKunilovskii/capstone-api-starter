package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class ShoppingCartController {
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal) {
        ShoppingCart shoppingCart = null;
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            shoppingCart = shoppingCartDao.getByUserId(userId);

            return shoppingCart;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", e);
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addItem(@PathVariable int productId, Principal principal) {
        ShoppingCartItem item = null;

        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

            if (shoppingCart.contains(productId)) {
                shoppingCartDao.increaseQuantityByOne(userId, productId);
                shoppingCart = shoppingCartDao.getByUserId(userId);
                return shoppingCart;
            } else {

                Product product = productDao.getById(productId);


                item = new ShoppingCartItem();
                item.setQuantity(1);
                item.setProduct(product);

                shoppingCartDao.addItemToCart(userId, item);

                return shoppingCartDao.getByUserId(userId);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", e);
        }

    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public void updateQuantity(@RequestBody ShoppingCartItem item, Principal principal, @PathVariable int productId){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

        if(shoppingCart.contains(productId)){
            int newQuantity = item.getQuantity();
            shoppingCartDao.updateQuantity(userId,productId,newQuantity);
        }

    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.clearCart(userId);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
