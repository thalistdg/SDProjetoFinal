# SDProjetoFinal
Sistema para verificação se números são primos utilizado na disciplina de Sistemas distribuidos - projeto final.

Para executar bastar rodar o arquivo 'script.sh' com o comando $ ./script.sh

O sistema conta com 3 clientes, 3 servidores, 1 balanceador e 1 coordenador. Os clientes Geram aleatóriamente operações de leitura ou escrita. Os clientes enviam
a operação para o balanceador. O balanceador armazena as operações pendentes em fila e envia para um dos servidores. Cada servidor pode processar a requisição e
calcular se um número é primo de forma paralela. A consistência dos 3 arquivos é mantida pelo coordenador que mantém a ordem das operações de escrita que ainda não
foram replicadas para outros arquivos.
