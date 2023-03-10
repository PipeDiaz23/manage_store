package co.edu.umanizales.manage_store.service;

import co.edu.umanizales.manage_store.controller.dto.BestSellerDTO;
import co.edu.umanizales.manage_store.controller.dto.BestStoreDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class SaleService {
    private List<Sale> sales;

    public SaleService() {
        this.sales = new ArrayList<>();
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
    }

    public int getTotalSales() {
        int sum = 0;
        for (Sale sale : sales) {
            sum = sum + sale.getQuantity();
        }
        return sum;
    }

    //total de ventas del vendedor, igual para en total de ventas por sucursal
    public int getTotalSalesByseller(String codeSeller) {
        int sum = 0;
        for (Sale sale : sales) {
            if (sale.getSeller().getCode().equals(codeSeller)) {
                sum = sum + sale.getQuantity();
            }
        }
        return sum;
    }

    public int getTotalSalesByStore(String codeStore) {
        int sum = 0;
        for (Sale sale : sales) {
            if (sale.getStore().getCode().equals(codeStore)) {
                sum = sum + sale.getQuantity();
            }
        }
        return sum;
    }

    public BestSellerDTO getBestSeller(List<Seller> sellers) {
        BestSellerDTO bestSellerDTO = new BestSellerDTO(new Seller(), 0);
        for (Seller seller : sellers) {
            int quant = getTotalSalesByseller(seller.getCode());
            if (quant >= bestSellerDTO.getQuantity()) {
                bestSellerDTO = new BestSellerDTO(seller, quant);
            }
        }
        return bestSellerDTO;
    }

    public BestStoreDTO getBestStore(List<Store> stores) {
        BestStoreDTO bestStoreDTO = new BestStoreDTO(new Store(), 0);
        for (Store store : stores) {
            int quant = getTotalSalesByStore(store.getCode());
            if (quant >= bestStoreDTO.getQuantity()) {
                bestStoreDTO = new BestStoreDTO(store, quant);
            }
        }
        return bestStoreDTO;
    }


}