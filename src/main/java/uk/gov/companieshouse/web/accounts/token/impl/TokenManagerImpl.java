package uk.gov.companieshouse.web.accounts.token.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

@Component
public class TokenManagerImpl implements TokenManager {

    @Autowired
    private EnvironmentReader environmentReader;

    private static final String CHS_JWT_SECRET = "CHS_JWT_SECRET";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T decodeJWT(String token, Class<T> clazz) {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token);
        return OBJECT_MAPPER.convertValue(claimsJws.getBody(), clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createJWT(Object object) throws JsonProcessingException {

        String payload = OBJECT_MAPPER.writeValueAsString(object);

        return Jwts.builder()
                .setPayload(payload)
                .signWith(getKey(), SIGNATURE_ALGORITHM)
                .compact();
    }

    private Key getKey() {

        String chsJWTSecret = environmentReader.getMandatoryString(CHS_JWT_SECRET);

        return new SecretKeySpec(chsJWTSecret.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
    }

}
