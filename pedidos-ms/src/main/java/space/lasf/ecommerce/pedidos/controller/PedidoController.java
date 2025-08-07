package space.lasf.ecommerce.pedidos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import space.lasf.ecommerce.pedidos.dto.ItemDoPedidoDto;
import space.lasf.ecommerce.pedidos.dto.PedidoDto;
import space.lasf.ecommerce.pedidos.dto.StatusDto;
import space.lasf.ecommerce.pedidos.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService service;

    @GetMapping("{id}")
    public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id){
        return ResponseEntity.ok(service.obterPorId(id));
    }
    @GetMapping("{id}/items")
    public ResponseEntity<List<ItemDoPedidoDto>> listarItensPedido(@PathVariable @NotNull Long id){
        PedidoDto pedido = service.obterPorId(id);
        return ResponseEntity.ok(pedido.getItens());
    }
    @GetMapping("/porta")
    public String getPorta(@Value("${local.server.port}") String porta){
        return String.format("Requisição respondida pela instância da porta: $s", porta);
    }

    @GetMapping("")
    public Page<PedidoDto> listarTodos(@PageableDefault(size = 10) Pageable paginacao){
        return service.obterTodos(paginacao);
    }


    @PostMapping("")
    public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto dto, UriComponentsBuilder uriBuilder){
        PedidoDto pedido = service.criarPedido(dto);
        URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(endereco).body(pedido);
    }
    
    @PutMapping("{id}/status")
    public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable @NotNull Long id, StatusDto status){
        return ResponseEntity.ok(service.atualizaStatus(id, status));
    }
    @PutMapping("{id}/pago")
    public ResponseEntity<PedidoDto> aprovaPagamento(@PathVariable @NotNull Long id){
        service.aprovaPagamentoPedido(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<PedidoDto> delete(@PathVariable @NotNull Long id){
        service.excluirPedido(id);

        return ResponseEntity.noContent().build();
    }
}
