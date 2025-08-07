package space.lasf.ecommerce.pedidos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDoPedidoDto {
    private Long id;
    private Integer quantidade;
    private String descricao;
}
