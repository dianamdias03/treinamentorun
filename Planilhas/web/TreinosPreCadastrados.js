/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute', 'ngSanitize']);

treinoApp.controller('TreinosPreCadastradosCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();

    $scope.load = function ()
    {

        $http.post("sessao.jsp", {}).then(function (response) {
            $scope.dadosSessao = response.data;
            if (!$scope.dadosSessao.logado) {
                document.location.href = './login.html';
            } else {
                $scope.opcoesMenu = cadastros.getMenus($scope.dadosSessao);
            }
        });

        $http.post("select.jsp", {params: {"tabela": "tipos_modalidades"}}).then(function (response) {
            $scope.tiposModalidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_treinos"}}).then(function (response) {
            $scope.tiposTreinos = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_intensidades"}}).then(function (response) {
            $scope.tiposIntensidades = response.data.registros;
        });
        $http.post("select.jsp", {params: {"tabela": "tipos_percursos"}}).then(function (response) {
            $scope.tiposPercursos = response.data.registros;
        });

        $http.post("consulta.jsp", {"consulta": "treinosPreCadastrados"}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.dados;
        });

    };

    $scope.editar = function (item)
    {
        $scope.registro = item;
        $('#modalTreinoPreCadastrados').modal('show');

    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tabela": "treinosPreCadastrados", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                if (item.codigo === 0) {
                    item.codigo = response.data.novoCodigo;
                    $scope.listaRegistros.push(item);
                }
                item.descricaoF = response.data.descricaoF;
                $('#modalTreinoPreCadastrados').modal('hide');
            }
        });
    };

    $scope.novo = function () {
        var lParams = {
            params:
                    {
                        "tabela": "treinosPreCadastrados",
                        "acao": 1
                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                $scope.registro = response.data.registro;
                $('#modalTreinoPreCadastrados').modal('show');
            }
        });
    }

    $scope.excluir = function (item) {

        var lParams = {
            params: {
                "tabela": "treinosPreCadastrados",
                "codigo": item.codigo,
                "acao": 2,
                "dados": item
            }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                cadastros.excluirDaLista(item, $scope.listaRegistros);
                $('#modalTreinoPreCadastrados').modal('hide');
            }
        });
    }

})