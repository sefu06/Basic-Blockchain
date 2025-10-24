
package noobchain;
import java.util.ArrayList;
import java.security.*;


//transaction will carry the data: public key of sender, public key of reciever, amount transferred, inputs and outputs, a cryptographic signature
public class Transaction {

    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
    
    private static int sequence = 0;


    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;

    }

    private String calculateHash() { //the signature only allows one user to spend their coins, and prevent others from tampering with submitted transactions.
        sequence++;
        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) + Float.toString(value) + sequence);

    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
