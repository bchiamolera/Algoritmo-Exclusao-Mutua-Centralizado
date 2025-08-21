# Especificação:
- a cada 1 minuto o coordenador morre
- quando o coordenador morre, a fila também morre (o novo coordenador pode ser escolhido de forma randomizada. Ou seja, não é necessário implementar um algoritmo de eleição)
- o tempo de processamento de um recurso é de 5 à 15 segundos
- os processos tentam consumir o(s) recurso(s) num intervalo de 10 à 25 segundos
- a cada 40 segundos um novo processo deve ser criado (ID randômico)
- dois processos não podem ter o mesmo ID
