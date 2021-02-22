package hyve.petshow.util;

public class ProxyUtils {
    public static Boolean verificarIdentidade(Long domainId, Long requestId){
        return domainId.equals(requestId);
    }
}
