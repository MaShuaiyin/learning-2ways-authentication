# learning-2ways-authentication
Workshop for 2 ways ssl authentication

To create a new JKS keystore from scratch, containing a single self-signed Certificate, execute the following from a terminal command line:

```sh
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA
    # Or
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore /path/to/my/keystore
```
