package noobchain;

import static spark.Spark.*;

import java.util.concurrent.BlockingDeque;

import com.google.gson.Gson;

import noobchain.NoobChain;

public class MainWeb {
    public static void main(String[] args) {
        NoobChain blockChain = new NoobChain(); 
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        Gson gson = new Gson();

        // Create genesis block
        System.out.println("Creating and Mining Genesis block...");
        //blockchain.addBlock(new Block("Genesis Block", "0"));

        get("/blocks", (req, res) -> {
            res.type("application/json");
            return gson.toJson(blockchain.getBlocks());
        });

        get("/wallets/:walletId", (req, res) -> {
            res.type("application/json");
            String id = req.params(":walletId");
            Wallet wallet;
            if (id.equals("A"))
                wallet = walletA;
            else if (id.equals("B"))
                wallet = walletB;
            else
                return gson.toJson("Wallet not found");
            return gson.toJson(wallet.getBalance());
        });

        post("/transaction", (req, res) -> {
            // expects JSON: { "from": "A", "to": "B", "amount": 10 }
            TransactionRequest tr = gson.fromJson(req.body(), TransactionRequest.class);
            Wallet fromWallet = tr.from.equals("A") ? walletA : walletB;
            Wallet toWallet = tr.to.equals("A") ? walletA : walletB;

            boolean success = fromWallet.sendFunds(toWallet, tr.amount, blockchain);
            if (success)
                return "Transaction Successful";
            else
                return "Transaction Failed: Not enough funds";
        });

        System.out.println("Server running on http://localhost:4567");
    }

    // Helper class to parse transaction JSON
    static class TransactionRequest {
        String from;
        String to;
        float amount;
    }
}
