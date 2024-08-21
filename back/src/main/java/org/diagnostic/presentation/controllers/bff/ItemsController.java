package org.diagnostic.presentation.controllers.bff;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.diagnostic.application.useCases.bff.GetProductsWithOrder;
import org.diagnostic.presentation.controllers.BaseController;
import org.diagnostic.presentation.dto.ItemOrderDto;

import java.util.List;

@RestController
@Tag(name = "Orders")
@RequestMapping(value = "/api/items")
public class ItemsController extends BaseController {

    private final GetProductsWithOrder getProductsWithOrder;

    public ItemsController(GetProductsWithOrder getProductsWithOrder) {
        this.getProductsWithOrder = getProductsWithOrder;
    }

    @GetMapping("")
    public ResponseEntity<List<ItemOrderDto>> getProducts(@RequestParam(value = "order_id", required = false) String orderId) {
        return ResponseEntity.ok(getProductsWithOrder.getProductsWithOrder(orderId, getUserFromJwt().getId()));
    }
}
