
var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('TreinadorCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $rootScope.activetab = $location.path();
    $scope.pageActive = 'selAtleta';

    $scope.page = 'Pagina do treinador';
    $scope.listaRegistros = [];
    $scope.microCicloAtleta = [];

    $scope.tiposTreino = [{"i_clientes": 1, "i_tipos_treinos": 1, "descricao": "Não informado"}, {"i_clientes": 1, "i_tipos_treinos": 2, "descricao": "Continuo"}, {"i_clientes": 1, "i_tipos_treinos": 3, "descricao": "Intervalado"}, {"i_clientes": 1, "i_tipos_treinos": 4, "descricao": "Fartlek"}, {"i_clientes": 1, "i_tipos_treinos": 5, "descricao": "Regenerativo"}, {"i_clientes": 1, "i_tipos_treinos": 6, "descricao": "Ritmo"}, {"i_clientes": 1, "i_tipos_treinos": 7, "descricao": "Longo"}, {"i_clientes": 1, "i_tipos_treinos": 8, "descricao": "Prova"}];

    $scope.execAcao = function (acao, item) {
        $scope.Acao = 'Clicou: ' + acao + '>' + item.nome;
        $scope.Atleta = item;
    }

    $scope.selectPage = function (page, item) {
        $scope.Atleta = item;
        $scope.pageActive = page;

        if ($scope.pageActive === 'criaTreinos') {
            $scope.loadMicroCicloAtleta(0);
        }
    }

    $scope.loadMicroCicloAtleta = function (navegacao)
    {
        var diaInicio = "2017-09-18";
        if ($scope.microCicloAtleta.length > 0) {
            diaInicio = $scope.microCicloAtleta[0].inicio;
        }

        $http.post(
                "select.jsp",
                {
                    params: {
                        "tab": "atleta_micro_ciclo",
                        "i_usuarios": $scope.Atleta.i_usuarios,
                        "dia": diaInicio,
                        "navegacao": navegacao
                    }
                }
        )
                .then(function (response) {
                    if (response.data.resultado) {
                        $scope.microCicloAtleta = response.data.registros;
                        $scope.loadMicroCicloTreinosAtleta($scope.microCicloAtleta[0].i_micro_ciclo);
                    }
                });
    };

    $scope.loadMicroCicloTreinosAtleta = function (i_micro_ciclo)
    {
        $http.post(
                "select.jsp",
                {
                    params: {
                        "tab": "atleta_micro_ciclo_treinos",
                        "i_usuarios": $scope.Atleta.i_usuarios,
                        "i_micro_ciclo": i_micro_ciclo,
                    }
                }
        )
                .then(function (response) {
                    $scope.microCicloTreinosAtleta = response.data.registros;
                });
    };

    $scope.load = function ()
    {
        $http.post("select.jsp", {params: {"tab": "atletas"}}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.registros;
        });

        $http.post("select.jsp", {params: {"tab": "tipos_modalidades"}}).then(function (response) {
            $scope.tiposModalidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_treinos"}}).then(function (response) {
            $scope.tiposTreinos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_intensidades"}}).then(function (response) {
            $scope.tiposIntensidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_percursos"}}).then(function (response) {
            $scope.tiposPercursos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tab": "tipos_distancias"}}).then(function (response) {
            $scope.tiposDistancias = response.data.registros;
        });
    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tab": "tipos_modalidades", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                item.codigo = response.data.novoCodigo;
            }
        });
    };

    $scope.novo = function (mc, mct) {
        var lParams = {
            params:
                    {
                        "tab": "tabela",
                        "acao": 1,
                        "dia": mct.dia,
                        "i_clientes": mc.i_clientes,
                        "i_usuarios": mc.i_usuarios,
                        "i_micro_ciclo": mc.i_micro_ciclo

                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                mct.Itens.push(response.data.registro);
            }
        });
    }

    $scope.excluir = function (mct_dia, mct) {

        var lParams = {
            params: {
                "tab": "tabela",
                "codigo": mct_dia.codigo,
                "acao": 2,
                "dados": mct_dia
            }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                cadastros.excluirDaLista(mct_dia, mct.Itens);
            }
        });
    }

})