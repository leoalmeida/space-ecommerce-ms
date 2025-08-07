package space.lasf.ecommerce.pagamentos.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import space.lasf.ecommerce.pagamentos.dto.PagamentoDto;
import space.lasf.ecommerce.pagamentos.model.Status;
import space.lasf.ecommerce.pagamentos.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    
    @Autowired
    private PagamentoService service;

    @GetMapping("{id}")
    public ResponseEntity<PagamentoDto> getOne(@PathVariable @NotNull Long id){
        return ResponseEntity.ok(service.obterPorId(id));
    }
    
    @GetMapping("/porta")
    public String getPorta(@Value("${local.server.port}") String porta){
        return String.format("Requisição respondida pela instância da porta: $s", porta);
    }
    
    @GetMapping("")
    public Page<PagamentoDto> getAll(@PageableDefault(size = 10) Pageable paginacao){
        return service.obterTodos(paginacao);
    }

    @PostMapping("")
    public ResponseEntity<PagamentoDto> post(@RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriBuilder){
        PagamentoDto pagamento = service.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(endereco).body(pagamento);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<PagamentoDto> put(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto){
        return ResponseEntity.ok(service.atualizarPagamento(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<PagamentoDto> delete(@PathVariable @NotNull Long id){
        service.excluirPagamento(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido",fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        service.confirmarPagamento(id);
    }

    public void pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e){
        service.alterarStatus(id, Status.CONFIRMADO_SEM_INTEGRACAO);
    }
}
