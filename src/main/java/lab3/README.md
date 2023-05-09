**Sarcina pentru lab3**
- Inainte de a rula aplicatia din IpDNSApp trebuie modificata urmatorul rand din clasa Utils:
  String[] command = new String[]{"netsh", "interface", "ipv4", "set", "dns", "name=", "**Wi-Fi OR Ethernet**", "static", newDns};
- Odata ce s-a rulat, meniul sugestiv va aparea in consola. Pentru a rula o comanda, se va introduce numarul corespunzator comenzii dorite.