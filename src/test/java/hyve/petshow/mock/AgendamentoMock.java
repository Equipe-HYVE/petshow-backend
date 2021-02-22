package hyve.petshow.mock;

import hyve.petshow.controller.representation.*;
import hyve.petshow.domain.AdicionalAgendamento;
import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.AnimalEstimacaoAgendamento;
import hyve.petshow.domain.embeddables.Endereco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static hyve.petshow.mock.AdicionalMock.criaAdicional;
import static hyve.petshow.mock.AnimalEstimacaoMock.criaAnimalEstimacao;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacao;
import static hyve.petshow.mock.ClienteMock.criaCliente;
import static hyve.petshow.mock.PrestadorMock.criaPrestador;
import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhado;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;

public class AgendamentoMock {
    public static Agendamento criaAgendamento(){
        var agendamento = new Agendamento();
        var animalEstimacaoAgendamento = new AnimalEstimacaoAgendamento(agendamento, criaAnimalEstimacao());
        var adicionalAgendamento = new AdicionalAgendamento(agendamento, criaAdicional(1L));

        agendamento.setEndereco(new Endereco());
        agendamento.setData(LocalDateTime.now());
        agendamento.setPrecoFinal(BigDecimal.TEN);
        agendamento.setPrestador(criaPrestador());
        agendamento.setStatus(criaStatusAgendamento());
        agendamento.setAnimaisAtendidos(Arrays.asList(animalEstimacaoAgendamento));
        agendamento.setServicoDetalhado(criaServicoDetalhado());
        agendamento.setAuditoria(geraAuditoriaInsercao(Optional.of(1L)));
        agendamento.setCliente(criaCliente());
        agendamento.setComentario("teste");
        agendamento.setAdicionais(Arrays.asList(adicionalAgendamento));
        agendamento.setAvaliacao(criaAvaliacao());
        agendamento.setId(1L);

        return agendamento;
    }

    public static AgendamentoRepresentation criaAgendamentoRepresentation(){
        var agendamento = new AgendamentoRepresentation();

        agendamento.setId(1L);
        agendamento.setData(LocalDateTime.now());
        agendamento.setComentario("teste");
        agendamento.setEndereco(new Endereco());
        agendamento.setPrecoFinal(BigDecimal.TEN);
        agendamento.setStatusId(1);
        agendamento.setStatus(new StatusAgendamentoRepresentation());
        agendamento.setClienteId(1L);
        agendamento.setCliente(new ClienteRepresentation());
        agendamento.setPrestadorId(1L);
        agendamento.setPrestador(new PrestadorRepresentation());
        agendamento.setServicoDetalhadoId(1L);
        agendamento.setServicoDetalhado(new ServicoDetalhadoRepresentation());
        agendamento.setAvaliacao(new AvaliacaoRepresentation());
        agendamento.setAnimaisAtendidosIds(Arrays.asList(1L));
        agendamento.setAnimaisAtendidos(new ArrayList<>());
        agendamento.setAdicionaisIds(Arrays.asList(1L));
        agendamento.setAdicionais(new ArrayList<>());

        return agendamento;
    }
}
