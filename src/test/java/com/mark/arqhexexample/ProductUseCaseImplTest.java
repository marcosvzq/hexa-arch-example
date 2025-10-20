import com.mark.arqhexexample.application.ProductUseCaseImpl;
import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.domain.port.output.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort repositoryPort;

    private ProductUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ProductUseCaseImpl(repositoryPort);
    }

    @Test
    void createProductSavesAndReturnsProduct() {
        Product saved = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(repositoryPort.save(any(Product.class))).thenReturn(saved);

        Product result = useCase.createProduct("name", "desc", new BigDecimal("10"), "sku", "1", "img");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repositoryPort).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(result).isEqualTo(saved);
    }

    @Test
    void getProductByIdReturnsProduct() {
        Product product = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(repositoryPort.getById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = useCase.getProductById(1L);

        assertThat(result).contains(product);
    }

    @Test
    void getAllProductsDelegatesToRepository() {
        Product p1 = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(repositoryPort.getAll()).thenReturn(List.of(p1));

        List<Product> result = useCase.getAllProducts();

        assertThat(result).containsExactly(p1);
    }

    @Test
    void updateExistingProductReturnsUpdated() {
        Product existing = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(repositoryPort.getById(1L)).thenReturn(Optional.of(existing));
        Product updated = new Product(1L, "new", "newd", new BigDecimal("20"), "sku2", "2", "img2");
        when(repositoryPort.save(existing)).thenReturn(updated);

        Optional<Product> result = useCase.updateProduct(1L, "new", "newd", "20", "sku2", "2", "img2");

        assertThat(result).contains(updated);
        assertThat(existing.getName()).isEqualTo("new");
    }

    @Test
    void updateNonExistingProductReturnsEmpty() {
        when(repositoryPort.getById(2L)).thenReturn(Optional.empty());

        Optional<Product> result = useCase.updateProduct(2L, "name", "desc", "10", "sku", "1", "img");

        assertThat(result).isEmpty();
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void deleteExistingProductReturnsTrue() {
        Product existing = new Product(1L, "name", "desc", new BigDecimal("10"), "sku", "1", "img");
        when(repositoryPort.getById(1L)).thenReturn(Optional.of(existing));

        boolean result = useCase.deleteProduct(1L);

        assertThat(result).isTrue();
        verify(repositoryPort).deleteById(1L);
    }

    @Test
    void deleteNonExistingProductReturnsFalse() {
        when(repositoryPort.getById(1L)).thenReturn(Optional.empty());

        boolean result = useCase.deleteProduct(1L);

        assertThat(result).isFalse();
        verify(repositoryPort, never()).deleteById(any());
    }
}
