package uk.gov.companieshouse.web.accounts.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.SignatureException;

public interface TokenManager {
    /**
     * Decodes a provided {@code token} and converts the payload into a POJO of type {@code clazz}
     * @param token The token to decode
     * @param clazz The Class into which to parse the token payload
     * @return A POJO of type {@code clazz} which holds the data from the token payload
     */
     <T> T decodeJWT(String token, Class<T> clazz) throws SignatureException;

    /**
     * Creates a JWT using a provided {@code object} as the payload
     * @param object The POJO to populate the JWT payload
     * @return A created JWT
     * @throws JsonProcessingException if an exception occurs when signing the token
     */
     String createJWT(Object object) throws JsonProcessingException;

}
