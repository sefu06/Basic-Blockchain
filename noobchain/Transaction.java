
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

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }


//Returns true if new transaction could be created.
public boolean processTransaction() {

    if (verifySignature() == false) {
        System.out.println("#Transaction Signature failed to verify");
        return false;
    }

    // gather transaction inputs (Make sure they are unspent):
    for (TransactionInput i : inputs) {
        i.UTXO = NoobChain.UTXOs.get(i.TransactionOutputID);
    }

    // check if transaction is valid:
    if (getInputsValue() < NoobChain.minimumTransaction) {
        System.out.println("#Transaction Inputs to small: " + getInputsValue());
        return false;
    }

    // generate transaction outputs:
    float leftOver = getInputsValue() - value; // get value of inputs then the left over change:
    transactionId = calculateHash();
    outputs.add(new TransactionOutput(this.recipient, value, transactionId)); // send value to recipient
    outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // send the left over 'change' back to
                                                                              // sender

    // add outputs to Unspent list
    for (TransactionOutput o : outputs) {
        NoobChain.UTXOs.put(o.id, o);
    }

    // remove transaction inputs from UTXO lists as spent:
    for (TransactionInput i : inputs) {
        if (i.UTXO == null)
            continue; // if Transaction can't be found skip it
        NoobChain.UTXOs.remove(i.UTXO.id);
    }

    return true;
}

// returns sum of inputs(UTXOs) values
public float getInputsValue() {
    float total = 0;
    for (TransactionInput i : inputs) {
        if (i.UTXO == null)
            continue; // if Transaction can't be found skip it
        total += i.UTXO.value;
    }
    return total;
}

// returns sum of outputs:
public float getOutputsValue() {
    float total = 0;
    for (TransactionOutput o : outputs) {
        total += o.value;
    }
    return total;
}


    
}
