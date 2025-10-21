
import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain {


    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String[] args) {

        blockchain.add(new Block("Hi im the first block", "0"));
        blockchain.add(new Block("Yo im the second block", blockchain.get(blockchain.size() - 1).hash));
        blockchain.add(new Block("Hey im the third block", blockchain.get(blockchain.size() - 1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);

    }
    

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

        if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
            System.out.println("Current Hashes not equal");
            return false;
        }

        if (!previousBlock.hash.equals(previousBlock.calculateHash())) {
            System.out.println("Previous Hashes not equal");
            return false;

        }

    }

        return true;


    }
    
}
