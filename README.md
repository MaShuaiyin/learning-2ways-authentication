# learning-2ways-authentication
Workshop for 2 ways ssl authentication

Reference to: http://www.baeldung.com/x-509-authentication-in-spring-security

```sh
    cd keystore
    make
    cd -
    mvn clean test
```

## How to Create JKS keystore
To create a new JKS keystore from scratch, containing a single self-signed Certificate, execute the following from a terminal command line:

```sh
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA
    # Or
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore /path/to/my/keystore
```
