package space.lasf.ecommerce.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import space.lasf.ecommerce.pedidos.model.Status;

@Getter
@Setter
public class PedidoDto {
    private Long id;
    private LocalDateTime dataHora;
    private Status status;
    private List<ItemDoPedidoDto> itens;
}
