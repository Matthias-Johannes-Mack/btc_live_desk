package connection;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.security.InvalidKeyException;
import java.util.Base64;

/**
 * Created by robevansuk on 17/03/2017.
 */

public class Signature {

    private String secretKey;

    public Signature() { }

   

    /**
     * The CB-ACCESS-SIGN header is generated by creating a sha256 HMAC using
     * the base64-decoded secret key on the prehash string for:
     * timestamp + method + requestPath + body (where + represents string concatenation)
     * and base64-encode the output.
     * The timestamp value is the same as the CB-ACCESS-TIMESTAMP header.
     * @param requestPath
     * @param method
     * @param body
     * @param timestamp
     * @return
     */
    public String generate(String requestPath, String method, String body, String timestamp, String secretKey) {
        try {
            String prehash = timestamp + method.toUpperCase() + requestPath + body;
            byte[] secretDecoded = Base64.getDecoder().decode(secretKey);
            SecretKeySpec keyspec = new SecretKeySpec(secretDecoded, GdaxConstants.SHARED_MAC.getAlgorithm());
            Mac sha256 = (Mac) GdaxConstants.SHARED_MAC.clone();
            sha256.init(keyspec);
            return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes()));
        } catch (CloneNotSupportedException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeErrorException(new Error("Cannot set up authentication headers."));
        }
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
