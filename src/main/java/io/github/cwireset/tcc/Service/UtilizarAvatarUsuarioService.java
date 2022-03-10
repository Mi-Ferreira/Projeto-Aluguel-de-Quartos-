package io.github.cwireset.tcc.Service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
@Service
public class UtilizarAvatarUsuarioService {
    private static final String URI = "https://some-random-api.ml/img/dog";

    public String utilizarAvatar() {
        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject(URI, String.class);
        return response;
    }
}
