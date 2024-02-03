package shopper.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopper.backend.responses.MessageResponsePayload;
import shopper.backend.services.GeneratorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generate")
public class GeneratorController {
    private final GeneratorService generatorService;

    // Endpoint pentru generarea de roluri.
    @PostMapping("/roles")
    public ResponseEntity<MessageResponsePayload> generateRoles() {
        MessageResponsePayload responsePayload = this.generatorService.generateRoles();
        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru generarea de mărimi.
    @PostMapping("/sizes")
    public ResponseEntity<MessageResponsePayload> generateSizes() {
        MessageResponsePayload responsePayload = this.generatorService.generateSizes();
        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru generarea de tipuri.
    @PostMapping("/types")
    public ResponseEntity<MessageResponsePayload> generateTypes() {
        MessageResponsePayload responsePayload = this.generatorService.generateTypes();
        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru generarea de produse.
    @PostMapping("/products")
    public ResponseEntity<MessageResponsePayload> generateProducts() {
        MessageResponsePayload responsePayload = this.generatorService.generateProducts();
        return ResponseEntity.ok(responsePayload);
    }

    // Endpoint pentru generarea de categorii.
    @PostMapping("/categories")
    public ResponseEntity<MessageResponsePayload> generateCategories() {
        MessageResponsePayload responsePayload = this.generatorService.generateCategories();
        return ResponseEntity.ok(responsePayload);
    }
}
