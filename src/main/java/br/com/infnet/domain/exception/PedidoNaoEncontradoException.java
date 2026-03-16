package br.com.infnet.domain.exception;

import java.util.UUID;

public class PedidoNaoEncontradoException extends DomainException {

    private static final String MSG_FMT = "Pedido não encontrado: %s";

    public PedidoNaoEncontradoException(UUID id) {
        super(String.format(MSG_FMT, id));
    }
}
