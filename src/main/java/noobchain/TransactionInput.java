package noobchain;

public class TransactionInput {
    
    public String TransactionOutputID;
    public TransactionOutput UTXO;

    public TransactionInput(String TransactionOutputID) {
        this.TransactionOutputID = TransactionOutputID;

    }



}
