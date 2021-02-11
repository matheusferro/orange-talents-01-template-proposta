package br.com.zup.proposta.exception;

import java.util.ArrayList;
import java.util.List;

public class ErroValidacaoGlobalDTO {
    private List<String> globalErrorMessages = new ArrayList<>();
    private List<ErroDeValidacaoFieldDTO> fieldErrors = new ArrayList<>();

    public void addError(String message){
        globalErrorMessages.add(message);
    }

    public void addFieldError(String field, String message){
        ErroDeValidacaoFieldDTO fieldErro = new ErroDeValidacaoFieldDTO(field, message);
        fieldErrors.add(fieldErro);
    }

    public List<String> getGlobalErrorMessages() {
        return globalErrorMessages;
    }

    public List<ErroDeValidacaoFieldDTO> getFieldErrors() {
        return fieldErrors;
    }
}
