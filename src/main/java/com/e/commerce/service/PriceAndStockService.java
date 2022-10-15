package com.e.commerce.service;

import com.e.commerce.exceptions.DataNotFoundException;
import com.e.commerce.model.PriceAndStock;
import com.e.commerce.model.Product;
import com.e.commerce.model.Seller;
import com.e.commerce.repository.PriceAndStockRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class PriceAndStockService {

    private final PriceAndStockRepository priceAndStockRepository;

    public PriceAndStockService(PriceAndStockRepository priceAndStockRepository) {
        this.priceAndStockRepository = priceAndStockRepository;
    }

    public PriceAndStock createAndSavePriceAndStock(BigDecimal price, Long stock, Seller seller, Product product) {
       return this.savePriceAndStock(new PriceAndStock(price, stock, seller, product));
    }

    public PriceAndStock savePriceAndStock(PriceAndStock priceAndStock) {
        return priceAndStockRepository.save(priceAndStock);
    }

    public PriceAndStock findPriceAndStockByIdOrElseThrow(Long priceAndStockId) {
        return priceAndStockRepository.findById(priceAndStockId)
                .orElseThrow(
                        () ->  new DataNotFoundException("PriceAndStock not found by id :" + priceAndStockId));
    }

    public Optional<PriceAndStock> findPriceAndStockByProductAndSeller(Seller seller, Product product) {
        return priceAndStockRepository.findBySellerAndProduct(seller, product);
    }

    public List<PriceAndStock> findAllByProductId(Long productId) {
        return priceAndStockRepository.findAllByProduct_Id(productId);
    }

    public List<PriceAndStock> findAllBySellerId(Long sellerId) {
        return priceAndStockRepository.findAllBySeller_Id(sellerId);
    }

    public PriceAndStock updatePriceAndStock(Long priceAndStockId, BigDecimal price, Long stock) {
        PriceAndStock priceAndStock = this.findPriceAndStockByIdOrElseThrow(priceAndStockId);

        priceAndStock.setPrice(price);
        priceAndStock.setStock(stock);

        return this.savePriceAndStock(priceAndStock);
    }

    public void deleteAll(List<PriceAndStock> priceAndStockList) {
        if(!priceAndStockList.isEmpty()) {
            priceAndStockRepository.deleteAll(priceAndStockList);
        }
    }

}
