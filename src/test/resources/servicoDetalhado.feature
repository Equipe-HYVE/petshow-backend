    Cadastro de serviço com adicionais
    Eu, como prestador de serviço autônomo ou administrador da empresa,
    Devo poder cadastrar os serviços que ofereço com adicionais
    Para permitir aos clientes me encontrar através da busca.

        Acesso a tela de cadastro de serviços
        Dado que estou na tela de perfil
        Quando apertar no botão com texto "Adicionar serviço"
        Então devo ver formulário de cadastro de serviço

        Cadastro de serviço bem sucedido
        Dado que estou na tela de cadastro de serviço
        Quando preencher todos os dados corretamente
        E apertar no botão com texto "Adicionar serviço"
        Então devo ser redirecionado para a tela de perfil
        E devo ver o serviço novo na minha lista de serviços cadastrados

        Cadastro de serviço mal sucedido
        Dado que estou na tela de cadastro de serviço
        Quando preencher algum dado incorretamente
        E apertar no botão com texto "Adicionar serviço"
        Então devo ver uma mensagem de erro

    Atualização de serviços
    Eu, como prestador de serviços
    Devo conseguir atualizar meus serviços
    Para adicionar ou remover informações do mesmo.

        Botão de edição
        Dado que estou na tela de serviços
        Quando apertar no botão com texto "Editar"
        Então devo conseguir modificar os dados daquele serviço específico

        Deletar serviço
        Dado que selecionei o serviço
        Quando apertar no botão com texto "Remover serviço"
        Então devo ver lista de serviços sem o serviço removido
        
        Atualização de preço
        Dado que selecionei o serviço
        Quando modificar o preço
        E apertar no botão com texto "Salvar alterações"
        Então devo ver o novo preço

        Atualização de nome
        Dado que selecionei o serviço
        Quando modificar o nome
        E apertar no botão com texto "Salvar alterações"
        Então devo ver o serviço com o novo nome

        Modificação mal sucedida por remoção de campos
        Dado que selecionei o serviço
        Quando remover algum dos campos obrigatórios
        E apertar no botão com texto "Salvar alterações"
        Então devo ver mensagem de erro


        Acrescentar adicional
        Dado que selecionei o serviço
        Quando acrescentar um novo adicional
        E apertar no botão com texto "Salvar alterações"
        Então devo ver o serviço com o novo adicional

        Alterar adicional
        Dado que selecionei o serviço
        Quando modificar algum dos adicionais
        E apertar no botão com texto "Salvar alterações"
        Então devo ver o adicional modificado

        Deletar adicional
        Dado que selecionei o serviço
        Quando remover algum dos adicionais 
        E apertar no botão com texto "Salvar alterações"
        Então devo ver o serviço sem o adicional removido