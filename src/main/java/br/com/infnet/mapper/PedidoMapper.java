package br.com.infnet.mapper;

import br.com.infnet.domain.ItemPedido;
import br.com.infnet.domain.Pedido;
import br.com.infnet.dto.ItemPedidoResponse;
import br.com.infnet.dto.PedidoRequest;
import br.com.infnet.dto.PedidoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target = "valorTotal", expression = "java(pedido.calcularTotal())")
    PedidoResponse toResponse(Pedido pedido);

    List<PedidoResponse> toResponseList(List<Pedido> pedidos);

    ItemPedidoResponse toItemResponse(ItemPedido item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "itens", ignore = true)
    Pedido toEntity(PedidoRequest request);
}
