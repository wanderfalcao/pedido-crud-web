package br.com.infnet.config;

import br.com.infnet.domain.Pedido;
import br.com.infnet.domain.StatusPedido;
import br.com.infnet.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Popula o banco com dados de exemplo ao iniciar no perfil {@code dev}.
 * Idempotente em reinicializações.
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final PedidoRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            log.info("DataLoader: banco já possui dados — seed ignorado.");
            return;
        }

        log.info("DataLoader: inserindo pedidos de exemplo...");

        LocalDateTime now = LocalDateTime.now();

        // PENDENTE (15)
        save("Notebook Dell XPS 15",              new BigDecimal("8499.90"), StatusPedido.PENDENTE,    null,                                     now.minusDays(1),  null);
        save("Monitor LG UltraWide 34\"",          new BigDecimal("2899.00"), StatusPedido.PENDENTE,    null,                                     now.minusDays(2),  null);
        save("Teclado mecânico Keychron K2",        new BigDecimal("599.90"),  StatusPedido.PENDENTE,    "Cor: preto / Switch: Brown",             now.minusDays(3),  null);
        save("Mouse Logitech MX Master 3",          new BigDecimal("699.90"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(4),  null);
        save("Headset Sony WH-1000XM5",             new BigDecimal("1799.90"), StatusPedido.PENDENTE,    "Cor: prata",                             now.minusDays(4),  null);
        save("Cadeira gamer ThunderX3 EC3",         new BigDecimal("1299.00"), StatusPedido.PENDENTE,    null,                                     now.minusDays(5),  null);
        save("Webcam Logitech C920 HD",             new BigDecimal("399.90"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(6),  null);
        save("SSD Samsung 970 EVO 1TB",             new BigDecimal("699.00"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(7),  null);
        save("Impressora HP LaserJet Pro",          new BigDecimal("1249.00"), StatusPedido.PENDENTE,    "Inclui cartucho extra",                  now.minusDays(8),  null);
        save("Switch TP-Link 8 portas Gigabit",     new BigDecimal("229.90"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(9),  null);
        save("Roteador Asus AX3000",                new BigDecimal("799.00"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(10), null);
        save("Hub USB-C 7 em 1",                    new BigDecimal("89.90"),   StatusPedido.PENDENTE,    null,                                     now.minusDays(11), null);
        save("Cabo HDMI 2.1 2m",                    new BigDecimal("39.90"),   StatusPedido.PENDENTE,    null,                                     now.minusDays(12), null);
        save("Suporte ergonômico para notebook",    new BigDecimal("149.00"),  StatusPedido.PENDENTE,    null,                                     now.minusDays(13), null);
        save("Licença Microsoft Office 2024",       new BigDecimal("899.00"),  StatusPedido.PENDENTE,    "Versão para 5 dispositivos",             now.minusDays(14), null);

        // PROCESSANDO (10)
        save("iPhone 15 Pro Max 256GB",             new BigDecimal("9499.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(15), now.minusDays(14));
        save("MacBook Air M3 16GB",                 new BigDecimal("9999.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(16), now.minusDays(15));
        save("iPad Pro 11\" M4",                    new BigDecimal("8299.00"), StatusPedido.PROCESSANDO, "Cor: prata / Wi-Fi + 5G",                now.minusDays(17), now.minusDays(16));
        save("Tablet Samsung Galaxy Tab S9",        new BigDecimal("3499.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(18), now.minusDays(17));
        save("Smart TV Samsung 65\" QLED 4K",       new BigDecimal("4299.90"), StatusPedido.PROCESSANDO, "Instalação incluída",                    now.minusDays(18), now.minusDays(17));
        save("Projetor Epson 3600 Lumens",          new BigDecimal("3199.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(19), now.minusDays(18));
        save("Console PlayStation 5 Slim",          new BigDecimal("3799.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(20), now.minusDays(19));
        save("Controle Xbox Series X",              new BigDecimal("499.00"),  StatusPedido.PROCESSANDO, null,                                     now.minusDays(20), now.minusDays(19));
        save("Servidor Dell PowerEdge T150",        new BigDecimal("12499.00"),StatusPedido.PROCESSANDO, "Memória: 32GB / HD: 2x4TB",              now.minusDays(21), now.minusDays(20));
        save("Rack de servidor 12U fechado",        new BigDecimal("1899.00"), StatusPedido.PROCESSANDO, null,                                     now.minusDays(22), now.minusDays(21));

        // CONCLUIDO (10)
        save("Curso Engenharia de Software",        new BigDecimal("499.00"),  StatusPedido.CONCLUIDO,   null,                                     now.minusDays(23), now.minusDays(20));
        save("Treinamento Docker e Kubernetes",     new BigDecimal("799.00"),  StatusPedido.CONCLUIDO,   null,                                     now.minusDays(24), now.minusDays(21));
        save("Consultoria DevOps 8h",               new BigDecimal("2400.00"), StatusPedido.CONCLUIDO,   "Relatório entregue por e-mail",          now.minusDays(25), now.minusDays(22));
        save("Desenvolvimento de API REST",         new BigDecimal("4500.00"), StatusPedido.CONCLUIDO,   null,                                     now.minusDays(25), now.minusDays(23));
        save("Setup workstation completo",          new BigDecimal("1200.00"), StatusPedido.CONCLUIDO,   null,                                     now.minusDays(26), now.minusDays(24));
        save("Licença IntelliJ IDEA Ultimate",      new BigDecimal("699.00"),  StatusPedido.CONCLUIDO,   null,                                     now.minusDays(27), now.minusDays(25));
        save("Domínio + Hospedagem 2 anos",         new BigDecimal("349.90"),  StatusPedido.CONCLUIDO,   "domínio: pedidos.infnet.com.br",         now.minusDays(27), now.minusDays(26));
        save("Certificado SSL wildcard",            new BigDecimal("299.00"),  StatusPedido.CONCLUIDO,   null,                                     now.minusDays(28), now.minusDays(27));
        save("Migração de banco de dados",          new BigDecimal("1800.00"), StatusPedido.CONCLUIDO,   null,                                     now.minusDays(28), now.minusDays(28));
        save("Suporte técnico mensal",              new BigDecimal("450.00"),  StatusPedido.CONCLUIDO,   "Referência: fevereiro/2026",             now.minusDays(29), now.minusDays(29));

        // CONTESTADO (8)
        save("Notebook Lenovo ThinkPad X1",         new BigDecimal("7299.00"), StatusPedido.CONTESTADO,  "Produto chegou com tela arranhada",      now.minusDays(30), now.minusDays(25));
        save("Smartphone Motorola Edge 50",         new BigDecimal("2499.00"), StatusPedido.CONTESTADO,  "Bateria não carrega acima de 80%",       now.minusDays(30), now.minusDays(26));
        save("Serviço de manutenção de rede",       new BigDecimal("1500.00"), StatusPedido.CONTESTADO,  "Problema recorrente não resolvido",      now.minusDays(31), now.minusDays(27));
        save("Placa de vídeo RTX 4070 Ti",          new BigDecimal("4999.00"), StatusPedido.CONTESTADO,  "Artefatos visuais em jogos 3D",          now.minusDays(32), now.minusDays(28));
        save("Licença antivírus corporativo",       new BigDecimal("899.00"),  StatusPedido.CONTESTADO,  "Cobrança duplicada no cartão",           now.minusDays(33), now.minusDays(29));
        save("Mesa digitalizadora Wacom",           new BigDecimal("1199.00"), StatusPedido.CONTESTADO,  "Caneta sem sensibilidade de pressão",    now.minusDays(34), now.minusDays(30));
        save("Memória RAM DDR5 64GB Kit",           new BigDecimal("1899.00"), StatusPedido.CONTESTADO,  "Frequência abaixo do especificado",      now.minusDays(35), now.minusDays(31));
        save("Desenvolvimento landing page",        new BigDecimal("2200.00"), StatusPedido.CONTESTADO,  "Design entregue diferente do briefing",  now.minusDays(36), now.minusDays(32));

        // CANCELADO (7)
        save("Processador AMD Ryzen 9 7950X",       new BigDecimal("4299.00"), StatusPedido.CANCELADO,   null,                                     now.minusDays(37), now.minusDays(35));
        save("Placa-mãe ASUS ROG Strix X670E",      new BigDecimal("3299.00"), StatusPedido.CANCELADO,   null,                                     now.minusDays(38), now.minusDays(37));
        save("Fonte Corsair 850W 80+ Gold",         new BigDecimal("699.00"),  StatusPedido.CANCELADO,   "Compra cancelada por mudança de orçamento", now.minusDays(39), now.minusDays(38));
        save("Gabinete Lian Li PC-O11 Dynamic",     new BigDecimal("899.00"),  StatusPedido.CANCELADO,   null,                                     now.minusDays(40), now.minusDays(39));
        save("Kit water cooler 360mm",              new BigDecimal("799.00"),  StatusPedido.CANCELADO,   null,                                     now.minusDays(41), now.minusDays(40));
        save("NAS Synology DS923+",                 new BigDecimal("3999.00"), StatusPedido.CANCELADO,   "Substituído por solução em nuvem",       now.minusDays(42), now.minusDays(41));
        save("Cabo de gerenciamento de rede",       new BigDecimal("59.90"),   StatusPedido.CANCELADO,   null,                                     now.minusDays(43), now.minusDays(42));

        log.info("DataLoader: {} pedidos inseridos.", repository.count());
    }

    private void save(String descricao, BigDecimal valor, StatusPedido status,
                      String observacao, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        Pedido p = new Pedido();
        p.setId(UUID.randomUUID());
        p.setDescricao(descricao);
        p.setValor(valor);
        p.setStatus(status);
        p.setObservacao(observacao);
        p.setDataCriacao(dataCriacao);
        p.setDataAtualizacao(dataAtualizacao);
        repository.save(p);
    }
}
