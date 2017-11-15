/* global angular */

var treinoApp = angular.module('TreinoApp', ['ngRoute', 'ngSanitize']);

treinoApp.controller('CriaEventosCtrl', function ($scope, $rootScope, $location, $http, cadastros, $sanitize)
{
    $scope.listaRegistros = [];
    $scope.opcoesMenu = [];
    $scope.nomeCliente = cadastros.getNomeCliente();
    $scope.convidados = [];
    $scope.convidadosAlterados = false;

//    $scope.opcaoParticipacao = [
//        {"codigo": 3, "descricao": "10 km", "classe": "", "optantes": [], "show": 1},
//        {"codigo": 4, "descricao": "5 km", "classe": "", "optantes": [], "show": 1},
//        {"codigo": 2, "descricao": "Não participarei", "classe": "", "optantes": [], "show": 1}/*,
//         {"codigo": 1, "descricao": "Sem confirmação", "classe": "active"}*/
//    ];

    $scope.opcaoParticipacao = [];

    $scope.opcaoAtleta = "";

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

        $http.post(
                "gravar.jsp", {
                    params: {
                        "acao-gravar": "consultaSQL",
                        "consulta": "criarEventos"
                    }
                }
        )
                .then(function (response) {
                    if (response.data.resultado === true) {
                        $scope.listaRegistros = response.data.dados;
                    }
                });


    };

    $scope.editar = function (item)
    {
        $scope.registro = item;
        $('#myModal').modal('show');

    };

    $scope.gravar = function (item)
    {
        $http.post("gravar.jsp", {params: {"tabela": "criarEventos", "dados": item}}).then(function (response) {
            $scope.retornoGravar = response.data;
            if ($scope.retornoGravar.resultado) {
                item.ctrl_status = 1;
                if (item.codigo === 0) {
                    item.codigo = response.data.novoCodigo;
                    $scope.listaRegistros.push(item);
                }
                $('#myModal').modal('hide');
            }
        });
    };

    $scope.novo = function () {
        var lParams = {
            params:
                    {
                        "tabela": "criarEventos",
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
                "tabela": "criarEventos",
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

    $scope.novoConvidado = function () {
        $scope.convidados.push({"nome": ""});
    }

    $scope.preparaOpcoesParticipacao = function (opcoes) {
        $scope.opcaoParticipacao = [];
        opcoes = opcoes.split(";");
        for (i = 0; i < opcoes.length; i++) {
            $scope.opcaoParticipacao.push(
                    {
                        "codigo": i + 1,
                        "descricao": opcoes[i].trim(),
                        "classe": "",
                        "optantes": [],
                        "show": 1
                    }
            )
        }
    }

    $scope.visualizar = function (item) {

        $scope.eventoSelecionado = (item !== null);
        $scope.eventoVisualizacao = item;

        if ($scope.eventoSelecionado) {

            lParams = {
                "consulta": "participantes",
                "i_eventos": $scope.eventoVisualizacao.codigo,
                "i_usuarios": $scope.dadosSessao.i_usuarios
            };

            $http.post("consulta.jsp", lParams).then(function (response) {
                $scope.convidados2 = response.data.dados;

                $scope.convidados = [];
                $scope.minhaParticipacao = 0;
                $scope.distancia = 0;

                $scope.preparaOpcoesParticipacao(item.opcoesParticipacao);

                for (i = 0; i < $scope.convidados2.length; i++) {
                    if ($scope.convidados2[i].i_usuarios_convidador === $scope.dadosSessao.i_usuarios) {
                        if ($scope.convidados2[i].i_usuarios !== $scope.convidados2[i].i_usuarios_convidador) {
                            $scope.convidados.push({
                                "nome": $scope.convidados2[i].nomeUsuarioConvidado,
                                "i_eventosParticipacoes": $scope.convidados2[i].i_eventosParticipacoes,
                                "distancia": $scope.convidados2[i].distancia,
                                "opcao": $scope.convidados2[i].opcao
                            });
                        } else {
                            $scope.minhaParticipacao = $scope.convidados2[i].confirmado;
                            $scope.i_eventosParticipacoes = $scope.convidados2[i].i_eventosParticipacoes;
                            $scope.distancia = $scope.convidados2[i].distancia;
                            $scope.opcaoAtleta = $scope.convidados2[i].opcao;
                        }
                    }

                    $scope.addOptantes(
                            $scope.convidados2[i].opcao,
                            $scope.convidados2[i].nomeUsuarioConvidado,
                            $scope.convidados2[i].nomeUsuario
                            );

                }
                $scope.convidados.push({"nome": ""});
                $scope.convidados.push({"nome": ""});
                $scope.selecionaOpcaoParticipacaoByTexto($scope.opcaoAtleta);

            });
        }
    }

    $scope.confirmacaoUsuario = function () {
        $scope.convidadosAlteradosGravar();
    }

    $scope.convidadosAlteradosGravar = function () {

        lParams = {
            "consulta": "participantesGravar",
            "i_eventos": $scope.eventoVisualizacao.codigo,
            "i_usuarios": $scope.dadosSessao.i_usuarios,
            "nomeConvidador": $scope.dadosSessao.nome,
            "i_eventosParticipacoes": $scope.i_eventosParticipacoes,
            "minhaParticipacao": $scope.minhaParticipacao,
            "distancia": $scope.distancia,
            "convidados": $scope.convidados,
            "opcaoAtleta": $scope.opcaoAtleta
        };

        $scope.jsonGravar = lParams;

        $http.post("consulta.jsp", lParams).then(function (response) {
            $scope.convidadosAlterados = false;
            $scope.visualizar($scope.eventoVisualizacao);
        });
    }

    $scope.convidadosAlteradosChange = function () {
        $scope.convidadosAlterados = true;
    }

    $scope.selecionaOpcaoParticipacaoByTexto = function (nomeOpcao) {
        for (i = 0; i < $scope.opcaoParticipacao.length; i++) {
            if ($scope.opcaoParticipacao[i].descricao === nomeOpcao) {
                $scope.opcaoParticipacao[i].classe = "active";
            } else {
                $scope.opcaoParticipacao[i].classe = "";
            }
        }
    }

    $scope.selecionaOpcaoParticipacao = function (item) {
        $scope.opcaoAtleta = item.descricao;
        for (i = 0; i < $scope.opcaoParticipacao.length; i++) {
            if (item === $scope.opcaoParticipacao[i]) {
                $scope.opcaoParticipacao[i].classe = "active";
            } else {
                $scope.opcaoParticipacao[i].classe = "";
            }
        }
        $scope.convidadosAlteradosGravar();
    }

    $scope.addOptantes = function (opcao, nome, convidador) {

        var achou = 0;

        if (opcao === '') {
            opcao = '--';
        }

        if (convidador !== nome) {
            title = "Conviado de " + convidador;
        } else {
            title = "";
        }

        for (j = 0; j < $scope.opcaoParticipacao.length; j++) {
            if (opcao === $scope.opcaoParticipacao[j].descricao) {
                $scope.opcaoParticipacao[j].optantes.push(
                        {
                            "nome": nome,
                            "conta": $scope.opcaoParticipacao[j].optantes.length + 1,
                            "title": title
                        }
                );
                achou = 1;
                break;
            }
        }

        if (achou === 0) {
            $scope.opcaoParticipacao.push(
                    {"codigo": $scope.opcaoParticipacao.length,
                        "descricao": opcao,
                        "classe": "",
                        "optantes": [{"nome": nome, "conta": 1, "title": title}],
                        "show": 0
                    }
            );
        }

    }

    $scope.visualizar(null);

})