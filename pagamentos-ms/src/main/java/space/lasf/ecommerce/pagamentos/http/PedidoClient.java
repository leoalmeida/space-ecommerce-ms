package space.lasf.ecommerce.pagamentos.http;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import space.lasf.ecommerce.pagamentos.dto.ItemDoPedidoDto;


@FeignClient("pedidos-ms")
public interface PedidoClient {
    @PutMapping("/pedidos/{id}/pago")
    void atualizaPagamento(@PathVariable Long id);

    @GetMapping("/pedidos/{id}/items")
    List<ItemDoPedidoDto> listarItensPedido(@PathVariable Long id);

}
