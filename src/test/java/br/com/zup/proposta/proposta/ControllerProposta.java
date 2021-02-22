package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.analise.AnaliseClient;
import br.com.zup.proposta.proposta.analise.SolicitacaoAnaliseResponse;
import br.com.zup.proposta.proposta.request.PropostaCadastroRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerProposta {

    @MockBean
    private AnaliseClient client;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Cadastro de uma proposta")
    public void cadastroProposta() throws Exception {
        PropostaCadastroRequest request = new PropostaCadastroRequestBuilder()
                .comNome("nomeTeste")
                .comEmail("email@teste.com.br")
                .comDocumento("576.984.370-51")
                .comSalario(new BigDecimal(200))
                .comCep("0000-000")
                .comLogradouro("Rua do meio")
                .comNumero("10")
                .comComplemento("Perto da lojinha.")
                .finalizar();

        SolicitacaoAnaliseResponse response = new SolicitacaoAnaliseResponse("576.984.370-51",
                "nomeTeste",
                1L,
                "ELEGIVEL");

        Mockito.when(client.solicitacaoAnalise(ArgumentMatchers.notNull())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/proposta")
                            .content(jsonMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("nomeTeste"));
    }

    @Test
    @DisplayName("Cadastro de uma proposta com documento já cadastrado.")
    public void cadastroPropostaDocJaCadastrado() throws Exception {
        PropostaCadastroRequest propostaValida = new PropostaCadastroRequestBuilder()
                .comNome("nomeTeste")
                .comEmail("email@teste.com.br")
                .comDocumento("576.984.370-51")
                .comSalario(new BigDecimal(200))
                .comCep("0000-000")
                .comLogradouro("Rua do meio")
                .comNumero("10")
                .comComplemento("Perto da lojinha.")
                .finalizar();

        propostaRepository.save(propostaValida.toModel());

        SolicitacaoAnaliseResponse response = new SolicitacaoAnaliseResponse("576.984.370-51",
                "nomeTeste",
                1L,
                "ELEGIVEL");

        Mockito.when(client.solicitacaoAnalise(ArgumentMatchers.notNull())).thenReturn(response);

        PropostaCadastroRequest propostaInalida = new PropostaCadastroRequestBuilder()
                .comNome("nomeTeste")
                .comEmail("emailValido@teste.com.br")
                .comDocumento("576.984.370-51")
                .comSalario(new BigDecimal(200))
                .comCep("0000-000")
                .comLogradouro("Rua do meio")
                .comNumero("10")
                .comComplemento("Perto da lojinha.")
                .finalizar();

        mockMvc.perform(MockMvcRequestBuilders.post("/proposta")
                .content(jsonMapper.writeValueAsString(propostaInalida))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity());
    }
}
