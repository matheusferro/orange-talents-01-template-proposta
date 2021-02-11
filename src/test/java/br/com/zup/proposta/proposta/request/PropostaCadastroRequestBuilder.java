package br.com.zup.proposta.proposta.request;

import br.com.zup.proposta.proposta.PropostaCadastroRequest;
import br.com.zup.proposta.proposta.endereco.Endereco;
import br.com.zup.proposta.proposta.endereco.EnderecoRequest;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PropostaCadastroRequestBuilder {

    private String nome;
    private String email;
    private String documento;
    private BigDecimal salario;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;

    public PropostaCadastroRequestBuilder comNome(String nome){
        this.nome = nome;
        return this;
    }

    public PropostaCadastroRequestBuilder comEmail(String email){
        this.email = email;
        return this;
    }

    public PropostaCadastroRequestBuilder comDocumento(String documento){
        this.documento = documento;
        return this;
    }

    public PropostaCadastroRequestBuilder comSalario(BigDecimal salario){
        this.salario = salario;
        return this;
    }

    public PropostaCadastroRequestBuilder comCep(String cep){
        this.cep = cep;
        return this;
    }

    public PropostaCadastroRequestBuilder comLogradouro(String logradouro){
        this.logradouro = logradouro;
        return this;
    }

    public PropostaCadastroRequestBuilder comNumero(String numero){
        this.numero = numero;
        return this;
    }

    public PropostaCadastroRequestBuilder comComplemento(String complemento){
        this.complemento = complemento;
        return this;
    }

    public PropostaCadastroRequest finalizar(){
        return new PropostaCadastroRequest(nome, email, documento, salario,new EnderecoRequest(cep, logradouro, numero, complemento));
    }
}
