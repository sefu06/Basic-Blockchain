# BASIC BLOCKCHAIN PART 1

## What is a blockchain?
A Blockchain is a chain where each block represents a transaction and contains data (hash, userID, date, previous block's hash). 
<br>
A defining feature of the blockchain is that each block in the chain must contain a **hash**. A hash is a unique digital footprint, 
<br> It’s produced by running data through a hash function; in this blockchain, I used SHA-256 (same as Bitcoin). <br>

A hash is super important for any data that is sensitive or needs to be protected.

**Data Integrity (Tamper Detection)**

Each block stores its own hash and the hash of the previous block.

If someone changes data in one block, its hash changes — and the link to the next block breaks immediately.

This makes it obvious that the blockchain has been tampered with.

**Immutability**

Because every block depends on the previous block’s hash, you’d have to recompute all subsequent hashes to hide any change — nearly impossible in large blockchains.

**Efficient Verification**

Hashes make it easy to check that data hasn’t changed without re-reading the entire block.

They’re like quick “checksums” to ensure authenticity.



A cryptographic hash function (e.g. SHA-256) is deterministic: the same input always produces the same output hash.
Example: hash("hello") → some fixed 256-bit value every time.

A good cryptographic hash is one-way (preimage resistant): given only the hash, you cannot feasibly reconstruct the original input by any direct mathematical inversion.

So: you can compute the hash from the data, but you cannot (practically) recover the data from the hash.


# How to run:
The final product of this project was to create a simple program where there are two wallets where transactions can be made via blockchains. After each transaction, the JSON format of the block chain is printed to see the process of each transaction.

To test out the project, clone this repository onto your comupter and then run: **mvn clean compile exec:java** this will build the project and run on java spark's local server, which is run on **http://localhost:4567**.
