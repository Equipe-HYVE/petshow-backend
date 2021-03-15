package hyve.petshow.domain.embeddables;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
@NoArgsConstructor
public class Geolocalizacao {
    @Size(max = 50, message = "A geolocalização de longitude informada ultrapassa o limite de 50 caracteres.")
    @NotNull(message = "A geolocalização de longitude é obrigatória.")
    private String geolocLongitude;
    @Size(max = 50, message = "A geolocalização de latitude informada ultrapassa o limite de 50 caracteres.")
    @NotNull(message = "A geolocalização de latitude é obrigatória.")
    private String geolocLatitude;
}
