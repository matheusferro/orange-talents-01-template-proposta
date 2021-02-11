package br.com.zup.proposta.exception;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroValidacaoGlobalDTO handler(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();

        return buildResponse(globalErrors, fieldErrors);
    }

    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErroValidacaoGlobalDTO handler(BindException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();

        return buildResponse(globalErrors, fieldErrors);
    }

    private ErroValidacaoGlobalDTO buildResponse(List<ObjectError> globalErrors, List<FieldError> fieldErrors){
        ErroValidacaoGlobalDTO validacaoGlobal = new ErroValidacaoGlobalDTO();
        globalErrors.forEach(global -> validacaoGlobal.addError(getErrorMessage(global)));
        fieldErrors.forEach(field ->{
            String mensagem = getErrorMessage(field);
            validacaoGlobal.addFieldError(field.getField(), mensagem);
        });
        return validacaoGlobal;
    }
    private String getErrorMessage(ObjectError error){
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }
}
