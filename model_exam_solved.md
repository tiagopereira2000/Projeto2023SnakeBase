
## Parte 1.
1. R: Num livelock existe um bloqueamento simultâneo no processamento de duas ou mais threads. Ou seja não existe uma espera para que se libertem de modo geral os recursos utilizados por uma thread que podem ser necessários por outra thread, e o contrário. Ou seja, elas empatam-se uma à outra.
2. R: Num deadlock existe uma espera ativa da thread1 para desbloquear um cadeado que está bloqueado por outra thread2 e a mesma está à espera ativa para desbloquear um lock bloquado por thread1.
Ou seja, não existe uma configuração certa dos Locks com uma sequência para ambas as threads.
3. R: Starvation acontece quando uma thread não consegue ter acesso a um recurso partilhado, apesar de não estar tecnicamente bloqueada. Verifica-se frequentemente em seccções críticas compridas e demoradas, o que causa uma grande espera e baixo nível de concorrência.
Acontece geralmente com processos "relaxados" que após uma tentativa de acesso falhado dos recursos realiza outras tarefas.

