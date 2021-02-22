    Listagem de serviços
    Eu, como usuário
    Devo conseguir visualizar uma lista de todos os prestadores de serviços cadastrados na plataforma,
    A fim de iniciar minha pesquisa pelo prestadores de serviços  que melhor me convém.

        Busca sem filtro
        Dado que estou na lista de serviços
        Quando não selecionar nenhum filtro
        Então devo ver a lista de prestadores completa

        Busca com filtro
        Dado que estou na lista de serviços
        Quando selecionar o filtro <filtro>
        Então devo ver lista que contém os prestadores com <filtro>

        begin{ize}

         {Exemplos}:
                | filtro                |
                | Avaliação             |
                | Faixa de preço        |
                | Prestadores autônomos |
                | Prestador empresa     |
                | Bairro                |
                | Proximidade           |
                | Adicionais            |
                | Prestador             |
        end{ize}

        Busca com mais de um filtro
        Dado que estou na lista de serviços
        Quando selecionar mais de um filtro
        Então devo ver a lista de prestadores filtrada por todos os filtros selecionados

        Ordenação de serviços
        Dado que estou na lista de serviços
        Quando selecionar ordenação por <ordenacao>
        Então devo ver a lista ordenada por <ordenacao>

        begin{ize}
             {Exemplos}:
                | ordenacao                  |
                | Avaliação                  |
                | Preço (Menor para maior)   |
                | Preço (Maior para menor)   |
                | Ordem alfabética prestador |
                | Proximidade                |
        end{ize}

        Ordenação e filtro
        Dado que estou na lista de serviços
        Quando selecionar um filtro 
        E uma ordenação
        Então devo ver a lista ordenada e filtrada
