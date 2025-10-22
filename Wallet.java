
import java.security.*;

public class Wallet {
    public PrivateKey privateKey; //only for the owner, so only they can spend their coins
    public PublicKey publicKey; //ok to share with everyone (address)
}

