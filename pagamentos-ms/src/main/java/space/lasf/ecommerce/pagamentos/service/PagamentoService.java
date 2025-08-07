package space.lasf.ecommerce.pagamentos.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import space.lasf.ecommerce.pagamentos.dto.PagamentoDto;
import space.lasf.ecommerce.pagamentos.http.PedidoClient;
import space.lasf.ecommerce.pagamentos.model.Pagamento;
import space.lasf.ecommerce.pagamentos.model.Status;
import space.lasf.ecommerce.pagamentos.repository.PagamentoRepository;
import space.lasf.ecommerce.pagamentos.dto.ItemDoPedidoDto;

@Service
public class PagamentoService {
    
    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient pedido;

    public Page<PagamentoDto> obterTodos(Pageable paginacao){
        return repository.findAll(paginacao)
                        .map(p -> modelMapper.map(p,PagamentoDto.class));

    }

    public PagamentoDto obterPorId(Long id){
        Pagamento pagamento = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException());
        PagamentoDto result = modelMapper.map(pagamento,PagamentoDto.class);
        List<ItemDoPedidoDto> itensPedido = pedido.listarItensPedido(pagamento.getPedidoId());
        result.setItens(itensPedido);
        return result;
    }

    public PagamentoDto criarPagamento(PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto,Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto,Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id){
        repository.deleteById(id);
    }
 
    public void confirmarPagamento(Long id){
        Long idPagamento = this.alterarStatus(id, Status.CONFIRMADO);
        pedido.atualizaPagamento(idPagamento);
    }

    public Long alterarStatus(Long id, Status novoStatus){
        Optional<Pagamento> pagamento = repository.findById(id);
        if (!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }
        pagamento.get().setStatus(novoStatus);
        repository.save(pagamento.get());
        return pagamento.get().getId();
    }
}
