package hyve.petshow.controller.filter;

import lombok.Data;

import java.math.BigDecimal;

import hyve.petshow.domain.embeddables.Geolocalizacao;

@Data
public class ServicoDetalhadoFilter {
    private Integer tipoServicoId;
    private String ordenacao;
    private Boolean possuiAdicionais;
    private Integer mediaAvaliacao;
    private BigDecimal menorPreco;
    private BigDecimal maiorPreco;
    private Geolocalizacao posicaoAtual;
    private Double metrosGeoloc = 600d;
    private String cidade;
    private String estado;
}
