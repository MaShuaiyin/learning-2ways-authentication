# learning-2ways-authentication
Workshop for 2 ways ssl authentication

To create a new JKS keystore from scratch, containing a single self-signed Certificate, execute the following from a terminal command line:

```sh
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA
    # Or
    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore /path/to/my/keystore
```

http://www.baeldung.com/x-509-authentication-in-spring-security

Before we finish this section and look at the site, we still need to install our generated certificate authority as trusted certificate in a browser of our choice.

An exemplary installation of our certificate authority for Mozilla Firefox would look like follows:

1. Type about:preferences in the address bar
2. Open Advanced -> Certificates -> View Certificates -> Authorities
3. Click on Import
4. Locate the Baeldung tutorials folder and its subfolder spring-security-x509/keystore
5. Select the ca.crt file and click OK
6. Choose “Trust this CA to identify websites” and click OK

Note: If you don’t want to add our certificate authority to the list of trusted authorities, you’ll later have the option to make an exception and show the website tough, even when it is mentioned as insecure. But then you’ll see a ‘yellow exclamation mark’ symbol in the address bar, indicating the insecure connection!


Now, if we run the application and point our browser to https://localhost:8443/user, we become informed that the peer cannot be verified and it denies to open our website. So we also have to install our client certificate, which is outlined here exemplary for Mozilla Firefox:

1. Type about:preferences in the address bar
2. Open Advanced -> View Certificates -> Your Certificates
3. Click on Import
4. Locate the Baeldung tutorials folder and its subfolder spring-security-x509/keystore
5. Select the cid.p12 file and click OK
6. Input the password for your certificate and click OK