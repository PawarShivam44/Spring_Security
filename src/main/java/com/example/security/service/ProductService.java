package com.example.security.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.security.dto.Product;
import com.example.security.entity.UserInfo;
import com.example.security.repository.UserInfoRepo;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {
	
	
	List<Product> productList= null;
	
	@PostConstruct
	public void addProductWhileApplicationStart() {
		productList = IntStream.rangeClosed(1, 100)
				.mapToObj(p->
					new Product(p, "product"+p, new Random().nextInt(10), new Random().nextInt(100))
				).collect(Collectors.toList());
	}
	
	
	public List<Product> getAllProducts(){
		
		return productList;
	}
	
	
	public Product getProductById(int id) {
		
		return productList.stream().
				filter(product->product.getId()== id)
				.findAny()
				.orElseThrow(()->new RuntimeException("product not found with product id :"+id));
	}
	
	
	

}
