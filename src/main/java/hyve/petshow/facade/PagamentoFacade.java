package hyve.petshow.facade;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.*;
import hyve.petshow.Application;
import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.ClienteService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.service.port.ServicoDetalhadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static hyve.petshow.util.TextTranscriptionUtils.transcreveNomeServico;

@Component
public class PagamentoFacade {
    private static final String FEEDBACK_PATH = "/agendamento-feedback/";
    private static final String DESCRICAO = "Pagamento para o agendamento do serviço {0} prestado por {1} na data {2}";
    private static final String IDENTIFICATION_TYPE = "CPF";
    private static final List<String> PAGAMENTOS_EXCLUIDOS = Arrays.asList("pec", "bolbradesco");

    @Autowired
    private Application application;
    @Autowired
    private ServicoDetalhadoService servicoDetalhadoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PrestadorService prestadorService;
    @Autowired
    private AgendamentoService agendamentoService;

    public Preference efetuarPagamento(Long agendamentoId, Long clienteId) throws Exception {
        var agendamento = agendamentoService.buscarPorId(agendamentoId, clienteId);
        var cliente = agendamento.getCliente();
        var prestador = agendamento.getPrestador();
        var servicoDetalhado = agendamento.getServicoDetalhado();

        return this.geraPreference(cliente, prestador, servicoDetalhado, agendamento);
    }

    private Preference geraPreference(Cliente cliente,
                                      Prestador prestador,
                                      ServicoDetalhado servicoDetalhado,
                                      Agendamento agendamento) throws MPException {
        var preference = new Preference();
        var item = new Item();
        var address = new Address();
        var phone = new Phone();
        var identification = new Identification();
        var payer = new Payer();
        var paymentMethods = new PaymentMethods();
        var operacao = Preference.OperationType.regular_payment;
        var baseUrl = application.getUrlBaseFront().concat(
                FEEDBACK_PATH.concat(agendamento.getId().toString()));
        var succesUrl = baseUrl.concat("/success");
        var pendingUrl = baseUrl.concat("/pending");
        var failureUrl = baseUrl.concat("/failure");
        var backUrls = new BackUrls(succesUrl, pendingUrl, failureUrl);
        var endereco = cliente.getEndereco();

        item.setEventDate(Date.from(agendamento.getData().atZone(ZoneId.systemDefault()).toInstant()))
                .setDescription(MessageFormat.format(DESCRICAO, servicoDetalhado.getTipo().getNome(),
                        prestador.getNome(),
                        agendamento.getData()))
                .setQuantity(1)
                .setTitle(transcreveNomeServico(servicoDetalhado.getTipo().getNome()))
                .setUnitPrice(agendamento.getPrecoFinal().floatValue());

        address.setZipCode(endereco.getCep())
                .setStreetName(endereco.getLogradouro())
                .setStreetNumber(Integer.valueOf(endereco.getNumero()));

        phone.setAreaCode(cliente.getTelefone().substring(1,3))
                .setNumber(cliente.getTelefone());

        identification.setType(IDENTIFICATION_TYPE)
                .setNumber(cliente.getCpf());

        payer.setName(cliente.getNome())
                .setEmail(cliente.getEmail())
                .setPhone(phone)
                .setAddress(address);

        PAGAMENTOS_EXCLUIDOS.stream()
                .forEach(pagamento -> paymentMethods.setExcludedPaymentMethods(pagamento));
        paymentMethods.setExcludedPaymentTypes("ticket");

        preference.setPayer(payer)
                .setOperationType(operacao)
                .setBackUrls(backUrls)
                .appendItem(item)
                .setPaymentMethods(paymentMethods);

        return preference.save();
    }
}
