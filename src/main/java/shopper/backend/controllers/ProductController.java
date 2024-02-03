package shopper.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopper.backend.dtos.responses.ProductResponseDto;
import shopper.backend.services.ProductService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // Endpoint pentru obținerea tuturor produselor grupate după tip.
    @GetMapping("/grouped-by-type")
    public ResponseEntity<Map<String, Set<ProductResponseDto>>> getAllGroupedByType(
        @RequestParam(name="category", required = false) String category
    ) {
        // Obține produsele grupate după tip în funcție de categoria specificată.
        Map<String, Set<ProductResponseDto>> productResponseDtos = this.productService.getAllGroupedByType(category);
        return ResponseEntity.ok(productResponseDtos);
    }

    // Endpoint pentru obținerea unui produs după ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable UUID id) {
        ProductResponseDto productResponseDto = this.productService.getByIdDto(id);
        return ResponseEntity.ok(productResponseDto);
    }
}
