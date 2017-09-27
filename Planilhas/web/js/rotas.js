pawork_modulo.config(function ($routeProvider, $locationProvider)
{
    // remove o # da url
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
    $routeProvider

            // para a rota '/', carregaremos o template home.html e o controller 'HomeCtrl'
            .when('/', {
                templateUrl: 'app/views/home.html',
                controller: 'HomeCtrl',
            })

            // para a rota '/sobre', carregaremos o template sobre.html e o controller 'SobreCtrl'
            .when('/clientes', {
                templateUrl: 'app/views/clientes.html',
                controller: 'ClientesCtrl'
            })

            // para a rota '/sobre', carregaremos o template sobre.html e o controller 'SobreCtrl'
            .when('/usuarios', {
                templateUrl: 'app/views/usuarios.html',
                controller: 'UsuariosCtrl',
            })

            // para a rota '/sobre', carregaremos o template sobre.html e o controller 'SobreCtrl'
            .when('/atividades', {
                templateUrl: 'app/views/atividades.html',
                controller: 'AtividadesCtrl',
            })

            // para a rota '/contato', carregaremos o template contato.html e o controller 'ContatoCtrl'
            .when('/logAtividades', {
                templateUrl: 'app/views/logAtividades.html',
                controller: 'LogAtividadesCtrl',
            })

            .when('/graficos', {
                templateUrl: 'app/views/graficos.html',
                controller: 'GraficosCtrl',
            })

            .when('/relatorios', {
                templateUrl: 'app/views/relatorios.html',
                controller: 'RelatoriosCtrl',
            })

            .when('/treinador', {
                templateUrl: './app/views/treinador.html',
                controller: 'TreinadorCtrl'
            })

            // para a rota '/sobre', carregaremos o template sobre.html e o controller 'SobreCtrl'
            .when('/sobre', {
                templateUrl: 'app/views/sobre.html',
                controller: 'AtividadesCtrl',
            })

            .when('/login', {
                templateUrl: 'app/views/login.html',
                controller: 'pawork_ctrl',
            })

            .when('/logoff', {
                templateUrl: 'app/views/logoff.html',
                controller: 'pawork_logoff_ctrl',
            })

            // caso não seja nenhum desses, redirecione para a rota '/'
            .otherwise({redirectTo: '/'});
});