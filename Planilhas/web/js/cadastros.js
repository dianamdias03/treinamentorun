treinoApp.service('cadastros', function () {

    this.setBackEndFile = function (nome) {
        this.backEndFile = nome;
    }

    this.setHttp = function (parm) {
        this.myHttp = parm;
    }
    this.setScope = function (parm) {
        this.myScope = parm;
    }

    this.criarCloneObjeto = function (original)
    {
        var clone = Object.create(Object.getPrototypeOf(original));
        var i, keys = Object.getOwnPropertyNames(original);
        for (i = 0; i < keys.length; i++) {
            Object.defineProperty(clone, keys[ i ], Object.getOwnPropertyDescriptor(original, keys[ i ]));
        }
        return clone;
    }

    this.alterarConteudoObjeto = function (original, clone)
    {
        var i, keys = Object.getOwnPropertyNames(original);
        for (i = 0; i < keys.length; i++) {
            Object.defineProperty(clone, keys[ i ], Object.getOwnPropertyDescriptor(original, keys[ i ]));
        }
        return clone;
    }

    this.editar = function (parm) {
        this.myScope.dados = this.criarCloneObjeto(parm);
        this.sendoAlterado = parm;
    }

    this.novo = function (item) {
        this.myScope.dados = item;
        this.sendoAlterado = null;
    }

    this.gravar = function (item) {
//        $('#myModal').modal('hide');
        if (this.sendoAlterado === null) {
            this.myScope.listaRegistros.push(item);
        } else {
            this.alterarConteudoObjeto(item, this.sendoAlterado);
        }
    }

    this.excluir = function (item) {
        var arr = this.myScope.listaRegistros;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] === item) {
                arr.splice(i, 1);
                break;
            }
        }
    }

    this.excluirDaLista = function (item, lista) {
        console.log('excluirDaLista - inicio - ' + lista.length);
        for (var i = 0; i < lista.length; i++) {
            console.log('Codigo: ' + item.codigo);
            if (lista[i] === item) {
                lista.splice(i, 1);
                console.log('Entrou');
                break;
            }
        }
    }

    this.load = function ()
    {
        this.myHttp.post(
                this.backEndFile,
                {
                    params: {},
                    headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='},
                    acao: 4
                }
        )
                .then(function (response) {
                    this.myScope.listaRegistros = response.data.registros;
                });
    };

    this.load_params = function (parans)
    {
//        item.status = 4;
        return {
            params: parans,
            headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='},
            acao: 4
        };
    }

    this.gravar_params = function (item, acao)
    {
        item.status = 9;
        return {
            params: {},
            headers: {Authorization: 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='},
            acao: acao,
            dados: item
        };
    }

    this.gravar_inicio = function (item, acao)
    {
        item.status = 9;
        if (acao === 1) {
            this.gravar(item);
        }
    }

    this.gravar_acao_padrao = function (response, item)
    {
        var retornoGravar = response.data;
        this.myScope.retornoGravar = retornoGravar;
        item.status = 1;
        if (retornoGravar.acao === 1) {
            item.id = retornoGravar.codigoGerado;
        }
        if (retornoGravar.acao === 3) {
            this.excluir(item);
        }
        if (retornoGravar.acao !== 3) {
            $('#myModal').modal('hide');
        }
    }

    this.login = function (parm) {
        $('#myModal3').modal('show');
//         $('#myModal3').modal('hide');
    }

    this.getEstadosCidades = function () {
        return [
            {"sigla": "SC", "nome": "Santa Catarina"},
            {"sigla": "AC", "nome": "Acre"},
            {"sigla": "AL", "nome": "Alagoas"},
            {"sigla": "AP", "nome": "Amapá"},
            {"sigla": "AM", "nome": "Amazonas"},
            {"sigla": "BA", "nome": "Bahia"},
            {"sigla": "CE", "nome": "Ceará"},
            {"sigla": "DF", "nome": "Distrito Federal"},
            {"sigla": "ES", "nome": "Espírito Santo"},
            {"sigla": "GO", "nome": "Goiás"},
            {"sigla": "MA", "nome": "Maranhão"},
            {"sigla": "MT", "nome": "Mato Grosso"},
            {"sigla": "MS", "nome": "Mato Grosso do Sul"},
            {"sigla": "MG", "nome": "Minas Gerais"},
            {"sigla": "PA", "nome": "Pará"},
            {"sigla": "PB", "nome": "Paraíba"},
            {"sigla": "PR", "nome": "Paraná"},
            {"sigla": "PE", "nome": "Pernambuco"},
            {"sigla": "PI", "nome": "Piauí"},
            {"sigla": "RJ", "nome": "Rio de Janeiro"},
            {"sigla": "RN", "nome": "Rio Grande do Norte"},
            {"sigla": "RS", "nome": "Rio Grande do Sul"},
            {"sigla": "RO", "nome": "Rondônia"},
            {"sigla": "RR", "nome": "Roraima"},
            {"sigla": "SP", "nome": "São Paulo"},
            {"sigla": "SE", "nome": "Sergipe"},
            {"sigla": "TO", "nome": "Tocantins"},
        ];
    }

});