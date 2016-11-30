# learning-2ways-authentication
Workshop for 2 ways ssl authentication

Reference to: http://www.baeldung.com/x-509-authentication-in-spring-security

```sh
    cd keystore
    make
    cd -
    mvn clean test
```

# Generate all Certificates
```sh
    ca.crt          # CA certificate
    cid.crt         # Client signed certificate with the CA
    cid.csr         # Client Certificate Signing Request CSR
    cid.p12         # Client private certificate
    cid.key         # Client private certificate
    localhost.crt   # Server signed certificate with the CA
    localhost.csr   # Server Certificate Signing Request CSR
    localhost.p12   # Client private certificate
    localhost.key   # Client private certificate
    truststore.jks
                    |- ca
                    |- cid
    keystore.jks
                    |- ca
                    |- localhost
```