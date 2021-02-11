package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.endereco.Endereco;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String documento;

    @Min(value = 0)
    @Column(nullable = false)
    private BigDecimal salario;

    @NotNull
    @Embedded
    private Endereco endereco;

    public Proposta(@NotBlank String nome,
                    @Email String email,
                    @Valid @NotBlank String documento,
                    @Min(value = 0) BigDecimal salario,
                    @Valid @NotNull Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

}
