
var treinoApp = angular.module('TreinoApp', ['ngRoute', 'ngSanitize']);

treinoApp.controller('PlanilhaSemanalCtrl', function ($scope, $rootScope, $location, $http, cadastros, $sanitize)
{
    $scope.planilhaSemanal = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();
    cadastros.setScope($scope);
    $scope.dia = "";

    $scope.loadPlanilhaSemana = function (navegacao)
    {
        $http.post(
                "gravar.jsp",
                {
                    params: {
                        "acao-gravar": "consultaSQL",
                        "consulta": "planilhaSemanal",
                        "i_clientes": $scope.dadosSessao.i_clientes,
                        "i_usuarios": $scope.dadosSessao.i_usuarios,
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

        $http.post("sessao.jsp", {}).then(function (response) {
            $scope.dadosSessao = response.data;
            if (!$scope.dadosSessao.logado) {
                document.location.href = './login.html';
            } else {
                $scope.opcoesMenu = cadastros.getMenus($scope.dadosSessao);
                $scope.loadPlanilhaSemana(0);
            }
        });

    };

    $scope.loadSessao();

});
