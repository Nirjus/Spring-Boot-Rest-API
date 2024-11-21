package com.nirjus.ecome_project.controller;

import com.nirjus.ecome_project.model.Product;
import com.nirjus.ecome_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String greet(){
        return "API is running";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){

        Product product = service.getProductById(id);
        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){

        try{
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
      Product product = service.getProductById(productId);
      byte[] imageFile = product.getImageData();

      return ResponseEntity.ok()
              .contentType(MediaType.valueOf(product.getImageType()))
              .body(imageFile);
    }
    @PutMapping("/product/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable int productId, @RequestPart Product product,@RequestPart MultipartFile imageFile){
       Product product1 = null;
        try {
             product1 = service.updateProduct(productId, product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(product1 != null){
            return  new ResponseEntity<>("Updated successfully", HttpStatus.OK);
        }else{
            return  new ResponseEntity<>("Not updated", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId){
     Product product = service.getProductById(productId);
     if(product != null){
         service.deleteProduct(productId);
         return new ResponseEntity<>("Deleted", HttpStatus.OK);
     }else{
         return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
     }
    }
    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(String keyword){
       List<Product> products = service.searchProducts(keyword);
       System.out.println("Searching with "+ keyword);
       return  new ResponseEntity<>(products, HttpStatus.OK);
    }
}
