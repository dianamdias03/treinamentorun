
var treinoApp = angular.module('TreinoApp', ['ngRoute']);

treinoApp.controller('PlanilhaSemanalCtrl', function ($scope, $rootScope, $location, $http, cadastros)
{
    $scope.planilhaSemanal = [];
    $scope.opcoesMenu = cadastros.getMenus();
    $scope.nomeCliente = cadastros.getNomeCliente();
    cadastros.setScope($scope);
    $scope.dia = "2017-10-09";

    $scope.loadPlanilhaSemana = function (navegacao)
    {
        $http.post(
                "gravar.jsp",
                {
                    params: {
                        "acao-gravar": "consultaSQL",
                        "consulta": "planilhaSemanal",
                        "i_clientes": $scope.sessaoUsuario.i_clientes,
                        "i_usuarios": $scope.sessaoUsuario.i_usuarios,
                        "navegacao": navegacao,
                        "dia": $scope.dia
                    }
                }
        )
                .then(function (response) {
                    $scope.planilhaSemanal = response.data.dados;
                    $scope.dia = response.data.dia;
                    $scope.resultado = response.data.dados.resultado;
                });
    };


    $scope.loadSessao = function ()
    {
        $http.post("sessao.jsp", {params: {}})
                .then(function (response) {
                    $scope.sessaoUsuario = response.data;
                    $scope.loadPlanilhaSemana(0);
                });
    };

    $scope.loadSessao();

});
