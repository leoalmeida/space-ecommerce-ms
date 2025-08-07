package space.lasf.ecommerce.pagamentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import space.lasf.ecommerce.pagamentos.model.Pagamento;


@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
    
}
