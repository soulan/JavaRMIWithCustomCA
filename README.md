# JavaRMIWithCustomCA
an example to create a virtual keystore and use own certificates generated with easyrsa or openssl to encrypt java rmi (the same technique could be used for any kind of ssl socket)

Environment variables can be set to control the used certificates in this example

* CA_CERT - ca cert should be the same for client and server
* CLIENT_KEY - used as key for HelloClient
* CLIENT_CERT - used as cert for HelloClient


* CA_CERT - ca cert should be the same for client and server
* SERVER_KEY - used as key for RMIRegistry
* SERVER_CERT - used as cert for RMIRegistry