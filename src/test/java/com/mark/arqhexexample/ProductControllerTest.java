import com.fasterxml.jackson.databind.ObjectMapper;
import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.domain.port.input.ProductUseCase;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.ProductController;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.mapper.ProductApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@Import(ProductApiMapper.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductUseCase productUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
    }

    @Test
    void createProductReturnsCreatedProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO("name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(productUseCase.createProduct(anyString(), anyString(), any(BigDecimal.class), anyString(), anyString(), anyString()))
                .thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void getProductByIdReturnsProduct() throws Exception {
        when(productUseCase.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void getAllProductsReturnsList() throws Exception {
        when(productUseCase.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void updateProductReturnsUpdatedProduct() throws Exception {
        Product updated = new Product(1L, "new", "desc", new BigDecimal("20"), "sku", "1", "img");
        ProductRequestDTO request = new ProductRequestDTO("new", "desc", new BigDecimal("20"), "sku", "1", "img");
        when(productUseCase.updateProduct(eq(1L), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("new"))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    void deleteProductReturnsNoContent() throws Exception {
        when(productUseCase.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }
}
