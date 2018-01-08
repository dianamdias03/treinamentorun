alter table eventosparticipacoes add opcao varchar(32);
alter table eventos add opcoesParticipacao varchar(128);

update eventosparticipacoes set opcao='5 km' where i_eventosParticipacoes > 0 and distancia=5; 
update eventosparticipacoes set opcao='10 km' where i_eventosParticipacoes > 0 and distancia=10; 
update eventosparticipacoes set opcao='Participar' where i_eventosParticipacoes > 0 and distancia=0 and confirmado=1; 
update eventosparticipacoes set opcao='Não Participar' where i_eventosParticipacoes > 0 and distancia=0 and confirmado=0; 
update eventosparticipacoes set opcao='Convidados' where i_eventosParticipacoes > 0 and i_usuarios=0; 

alter table treinosPreCadastrados 
    add i_tipos_modalidades int, 
    add i_tipos_intensidades int, 
    add i_tipos_treinos int, 
    add i_tipos_percursos int;


alter table treinosPreCadastrados 
    add i_clientes int; 

alter table treinosPreCadastrados 
    add i_grupos_atletas int; 
