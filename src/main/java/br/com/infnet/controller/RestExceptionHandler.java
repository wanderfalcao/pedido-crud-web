package br.com.infnet.controller;

import br.com.infnet.domain.exception.DomainException;
import br.com.infnet.domain.exception.PedidoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(assignableTypes = PedidoRestController.class)
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
    private static final String BASE_TYPE = "https://pedidos.infnet.com.br/errors/";

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(PedidoNaoEncontradoException ex,
                                                         HttpServletRequest request) {
        log.warn("Pedido não encontrado em {}: {}", request.getRequestURI(), ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Pedido não encontrado");
        pd.setType(URI.create(BASE_TYPE + "not-found"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(DomainException ex,
                                                                HttpServletRequest request) {
        log.warn("DomainException em {}: {}", request.getRequestURI(), ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setTitle("Regra de negócio violada");
        pd.setType(URI.create(BASE_TYPE + "domain-error"));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos.");
        log.warn("Validação falhou em {}: {}", request.getRequestURI(), detail);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setTitle("Dados inválidos");
        pd.setType(URI.create(BASE_TYPE + "validation-error"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado em {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente.");
        pd.setTitle("Erro interno");
        pd.setType(URI.create(BASE_TYPE + "internal-error"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}
