/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute', 'ngSanitize']);

treinoApp.controller('TreinoVisual20Ctrl', function ($scope, $rootScope, $location, $http, cadastros, $sanitize)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();

    $scope.semana = [
        {
            "treinos": [
                {
                    "vezes": "1x",
                    "etapas": [
                        {"descricao":"2 km leve"}
                    ]
                },
                {
                    "vezes": "5x",
                    "etapas": [
                        {"descricao":"1 km para 4'20\""},
                        {"descricao":"descanso para 120bpm"}
                    ]
                },
                {
                    "vezes": "1x",
                    "etapas": [
                        {"descricao":"2 km leve"}
                    ]
                }
            ],
            "nome": "Segunda"
        },
        {
            "treinos": [
                {
                    "vezes": "1x",
                    "etapas": [
                        {"descricao":"2 km leve"}
                    ]
                },
                {
                    "vezes": "5x",
                    "etapas": [
                        {"descricao":"1 km para 4'20\""},
                        {"descricao":"descanso para 120bpm"}
                    ]
                },
                {
                    "vezes": "1x",
                    "etapas": [
                        {"descricao":"2 km leve"}
                    ]
                }
            ],
            "nome": "Terça"
        }
    ];

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
        
        $http.get("http://104.236.202.162/Planilhas/TempEnvioEmails.jsp?lista=2&i_clientes=1&i_usuarios=6&inicio=2017-10-16").then(function (response) {
            $scope.dados = response.data;
        });
    };



})