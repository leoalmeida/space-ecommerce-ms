package space.lasf.ecommerce.pedidos.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import space.lasf.ecommerce.pedidos.dto.PedidoDto;
import space.lasf.ecommerce.pedidos.dto.StatusDto;
import space.lasf.ecommerce.pedidos.model.Pedido;
import space.lasf.ecommerce.pedidos.model.Status;
import space.lasf.ecommerce.pedidos.repository.PedidoRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {
    
    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<PedidoDto> obterTodos(Pageable paginacao){
        return repository.findAll(paginacao)
                        .map(p -> modelMapper.map(p,PedidoDto.class));

    }

    public PedidoDto obterPorId(Long id){
        Pedido pedido = repository.findById(id)
                                .orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(pedido,PedidoDto.class);
    }

    public PedidoDto criarPedido(PedidoDto dto){
        Pedido pedido = modelMapper.map(dto,Pedido.class);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(Status.REALIZADO);
        pedido.getItens().forEach(item -> item.setPedido(pedido));
        repository.save(pedido);
        return modelMapper.map(pedido, PedidoDto.class);
    }

    public PedidoDto atualizarPedido(Long id, PedidoDto dto){
        Pedido pedido = modelMapper.map(dto,Pedido.class);
        pedido.setId(id);
        pedido = repository.save(pedido);
        return modelMapper.map(pedido, PedidoDto.class);
    }

    public PedidoDto atualizaStatus(Long id, StatusDto dto) {

        Pedido pedido = repository.porIdComItens(id);

        if (pedido == null) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(dto.getStatus());
        repository.atualizaStatus(dto.getStatus(), pedido);
        return modelMapper.map(pedido, PedidoDto.class);
    }
    
    public void aprovaPagamentoPedido(Long id) {

        Pedido pedido = repository.porIdComItens(id);

        if (pedido == null) {
            throw new EntityNotFoundException();
        }

        pedido.setStatus(Status.PAGO);
        repository.atualizaStatus(Status.PAGO, pedido);
    }

    public void excluirPedido(Long id){
        repository.deleteById(id);
    }
}
