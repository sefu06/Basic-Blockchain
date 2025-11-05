package noobchain;

import static spark.Spark.*;

import java.security.Security;

import com.google.gson.Gson;

import noobchain.MainWeb.TransactionRequest;

public class MainWeb {

    
    public static void main(String[] args) {

        staticFiles.location("/public");

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        Gson gson = new Gson();
        initializeBlockchain();

        // Get all blocks
        get("/blocks", (req, res) -> {
            res.type("application/json");
            return gson.toJson(NoobChain.blockchain);
        });

        // Get wallet balance
        get("/wallets/:walletId", (req, res) -> {
            res.type("application/json");
            String id = req.params(":walletId");
            if (id.equals("A"))
                return gson.toJson(NoobChain.walletA.getBalance());
            else if (id.equals("B"))
                return gson.toJson(NoobChain.walletB.getBalance());
            else
                return gson.toJson("Wallet not found");
        });

        // Send funds
        post("/transaction", (req, res) -> {
            res.type("application/json");

            TransactionRequest tr = gson.fromJson(req.body(), TransactionRequest.class);

            Wallet fromWallet = tr.from.equals("A") ? NoobChain.walletA : NoobChain.walletB;
            Wallet toWallet = tr.to.equals("A") ? NoobChain.walletA : NoobChain.walletB;

            // sendFunds expects recipient public key and amount
            Transaction tx = fromWallet.sendFunds(toWallet.publicKey, tr.amount);
            if (tx != null) {
                return gson.toJson("Transaction Successful");
            } else {
                return gson.toJson("Transaction Failed: Not enough funds");
            }

        });

        System.out.println("Server running on http://localhost:4567");
    }
    
    public static void initializeBlockchain() {
        if (NoobChain.blockchain.isEmpty()) {
            NoobChain.walletA = new Wallet();
            NoobChain.walletB = new Wallet();
            Wallet coinbase = new Wallet();

            NoobChain.genesisTransaction = new Transaction(coinbase.publicKey, NoobChain.walletA.publicKey, 100f, null);
            NoobChain.genesisTransaction.generateSignature(coinbase.privateKey);
            NoobChain.genesisTransaction.transactionId = "0";
            NoobChain.genesisTransaction.outputs.add(
                    new TransactionOutput(NoobChain.genesisTransaction.recipient, NoobChain.genesisTransaction.value,
                            NoobChain.genesisTransaction.transactionId));
            NoobChain.UTXOs.put(
                    NoobChain.genesisTransaction.outputs.get(0).id,
                    NoobChain.genesisTransaction.outputs.get(0));

            Block genesis = new Block("0");
            genesis.addTransaction(NoobChain.genesisTransaction);
            NoobChain.addBlock(genesis);
        }
    }

    // Helper class to parse transaction JSON
    static class TransactionRequest {
        String from;
        String to;
        float amount;
    }
}
