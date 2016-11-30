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

# Nginx Client Authentication

experiment environment:

```sh
    /> uname -a
    Linux git.cecdat.com 3.10.0-327.22.2.el7.x86_64 #1 SMP Thu Jun 23 17:05:11 UTC 2016 x86_64 x86_64 x86_64 GNU/Linux
    /> yum info nginx
    Installed Packages
    Name        : nginx
    Arch        : x86_64
    Epoch       : 1
    Version     : 1.10.2
    Release     : 1.el7
```

nginx.conf

```sh
# For more information on configuration, see:
#   * Official English Documentation: http://nginx.org/en/docs/
#   * Official Russian Documentation: http://nginx.org/ru/docs/

user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

# Load dynamic modules. See /usr/share/nginx/README.dynamic.
include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;

    include             /etc/nginx/mime.types;
    default_type        application/octet-stream;

    # Load modular configuration files from the /etc/nginx/conf.d directory.
    # See http://nginx.org/en/docs/ngx_core_module.html#include
    # for more information.
    include /etc/nginx/conf.d/*.conf;


    server {
        listen       443 ssl http2 default_server;
        listen       [::]:443 ssl http2 default_server;
        server_name  _;
        root         /usr/share/nginx/html;

        ssl_certificate "/home/vagrant/keystore/localhost.crt";
        ssl_certificate_key "/home/vagrant/keystore/localhost.key";
        ssl_client_certificate "/home/vagrant/keystore/ca.crt";
        ssl_verify_client on;

        # Load configuration files for the default server block.
        include /etc/nginx/default.d/*.conf;

        location / {
            if ($ssl_client_verify != SUCCESS) {
                return 403;
            }
            auth_basic "Restricted";
            auth_basic_user_file /etc/nginx/.htpasswd;
            root /opt/public;
        }

        error_page 404 /404.html;
            location = /40x.html {
        }

        error_page 500 502 503 504 /50x.html;
            location = /50x.html {
        }
    }

}
```

curl commands:
```sh
curl -k -v -s -i -u user:password --key ./cid.key --cert ./cid.crt  https://localhost/index.html
```