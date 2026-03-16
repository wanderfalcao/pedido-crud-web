package br.com.infnet.controller;

import br.com.infnet.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = PedidoController.class)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String REDIRECT_LISTA    = "redirect:/pedidos";
    private static final String FLASH_ERRO        = "erro";
    private static final String MSG_ERRO_GENERICO = "Ocorreu um erro inesperado. Tente novamente.";

    @ExceptionHandler(DomainException.class)
    public String handleDomainException(DomainException ex,
                                        RedirectAttributes ra,
                                        HttpServletRequest request) {
        log.warn("DomainException em {}: {}", request.getRequestURI(), ex.getMessage());
        String msg = ex.getMessage() != null ? ex.getMessage() : MSG_ERRO_GENERICO;
        ra.addFlashAttribute(FLASH_ERRO, msg);
        return REDIRECT_LISTA;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex,
                                  RedirectAttributes ra,
                                  HttpServletRequest request) {
        log.error("Erro inesperado em {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        ra.addFlashAttribute(FLASH_ERRO, MSG_ERRO_GENERICO);
        return REDIRECT_LISTA;
    }
}
