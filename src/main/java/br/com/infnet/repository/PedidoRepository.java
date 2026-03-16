package br.com.infnet.repository;

import br.com.infnet.domain.Pedido;
import br.com.infnet.domain.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findAllByOrderByDataCriacaoDesc();

    Page<Pedido> findAll(Pageable pageable);

    long countByStatus(StatusPedido status);

    @Query("SELECT SUM(p.valor) FROM Pedido p WHERE p.status <> br.com.infnet.domain.StatusPedido.CANCELADO")
    java.math.BigDecimal somarValoresAtivos();
}
