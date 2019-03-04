/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('UsuariosCtrl', function ($scope, $rootScope, $location, $http, cadastros)
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

        $http.post("select.jsp", {params: {"tabela": "usuarios"}}).then(function (response) {
            $scope.dataParam = response.data;
            $scope.listaRegistros = response.data.registros;
        });

    };

    $scope.editar = function (item)
    {
        $scope.registro = item;
        $('#myModal').modal('show');

    };

    $scope.gravar = function (item, acaoGravar)
    {

        if (acaoGravar === 3) {
            if (!confirm('Confirma a exclusão do atleta?')) {
                return;
            }
        }

        $http.post("gravar.jsp", {params: {"tabela": "usuarios", acao: acaoGravar, "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                if (item.codigo === 0) {
                    item.codigo = response.data.novoCodigo;
                    $scope.listaRegistros.push(item);
                }
                if(acaoGravar===3){
                    $scope.load();
                }
                $('#myModal').modal('hide');
            }
        });
    };

    $scope.novo = function () {
        var lParams = {
            params:
                    {
                        "tabela": "usuarios",
                        "acao": 1
                    }
        };

        $http.post("gravar.jsp", lParams).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
//                lista.push(response.data.registro);
                $scope.registro = response.data.registro;
                $('#myModal').modal('show');
            }
        });
    }

    $scope.excluir = function (mct_dia, mct) {

        var lParams = {
            params: {
                "tabela": "usuarios",
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