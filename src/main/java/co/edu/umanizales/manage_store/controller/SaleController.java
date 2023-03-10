package co.edu.umanizales.manage_store.controller;

import co.edu.umanizales.manage_store.controller.dto.ResponseDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import co.edu.umanizales.manage_store.service.SaleService;
import co.edu.umanizales.manage_store.service.SellerService;
import co.edu.umanizales.manage_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200,
                        saleService.getSales(),
                        null),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createSale(@RequestBody
                                                  SaleDTO saleDTO){
        Seller findSeller = sellerService.getSellerById(saleDTO.getSellerId());
        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "El vendedor ingresado no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        Store findStore = storeService.getStoreById(saleDTO.getStoreId());
        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "La tienda ingresada no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        saleService.addSale(new Sale(findStore,findSeller, saleDTO.getQuantity()));
        return new ResponseEntity<>(new ResponseDTO(200,
                "Venta adicionada",null),
                HttpStatus.OK);
    }
    @GetMapping(path="/totalSale")
    public ResponseEntity<ResponseDTO>getTotalSales(){

        int saleTotal= saleService.getTotalSales();
        if(saleTotal == 0){
            return new ResponseEntity<>(new ResponseDTO(404,"No es posible hacer el calculo, no " +
                    "se encuentran ventas",null),HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(new ResponseDTO(200, saleService.getTotalSales(), null),
                    HttpStatus.OK);
        }
    }
    @GetMapping(path="/totalSeller/{code}")
    public ResponseEntity<ResponseDTO>getTotalSalesBySelller(@PathVariable String code){
        Seller findSeller = sellerService.getSellerById(code);
        if (findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(404,"no es posible hacer el calculo," +
                    "no se encuentran ventas",null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO(200,saleService.getTotalSales(),null),HttpStatus.OK);
    }

    @GetMapping(path="/totalStore/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesByStore(@PathVariable String code){
        Store findStore= storeService.getStoreById(code);
        if(findStore==null){
            return new ResponseEntity<>(new ResponseDTO(404,"no  es posible hacer el caculo," +
                    "no se encuentran ventas",null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO(200,saleService.getTotalSales(),null),
                HttpStatus.OK);
    }
    @GetMapping(path = "/bestseller")
    public ResponseEntity<ResponseDTO> BestSeller(){

        if(saleService.getTotalSales()==0){
            return new ResponseEntity<>(new ResponseDTO(400,"No se encuentran ventas,no se pudo realizar el codigo",
                    null),
                    HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(new ResponseDTO(200,
                    "El mejor vededor es" +saleService.getBestSeller(sellerService.getSellers()),
                    null), HttpStatus.OK);
        }
    }
    @GetMapping(path="/bestStore")
    public ResponseEntity<ResponseDTO> getBestStore(){
        if(saleService.getTotalSales()==0){
            return new ResponseEntity<>(new ResponseDTO(400,"No se encuentran ventas,no se pudo realizar el codigo",
                    null),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO(200,"La mejor tienda es"+ saleService.getBestStore(storeService.getStores()),
                null),
                HttpStatus.OK);
    }
    @GetMapping(path = "/averagesalesbystore")
    public ResponseEntity<ResponseDTO> getAverageSalesByStore(@PathVariable List<Seller> sellers){
        if(saleService.getTotalSales()==0) {
            return new ResponseEntity<>(new ResponseDTO(400,"no es posible realizar el calculo, no hay ventas",null),
                    HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(new ResponseDTO(200,"El promedio es" + saleService.getTotalSales() / (float) storeService.getStores().size(),
                    null), HttpStatus.OK);
        }
    }
    @GetMapping(path = "/averagesalesbyseller")
    public ResponseEntity<ResponseDTO> getAverageSalesBySeller(@PathVariable List<Seller> sellers) {
        if(saleService.getTotalSales()==0){
            return new ResponseEntity<>(new ResponseDTO(400,"no es posible realizar el calculo, no hay ventas",null),
                    HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(new ResponseDTO(200, saleService.getTotalSales() / (float) sellerService.getSellers().size(),
                    null), HttpStatus.OK);
        }
    }
}