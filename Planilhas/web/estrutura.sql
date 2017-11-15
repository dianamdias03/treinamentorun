alter table eventosparticipacoes add opcao varchar(32);

update eventosparticipacoes set opcao='5 km' where i_eventosParticipacoes > 0 and distancia=5; 
update eventosparticipacoes set opcao='10 km' where i_eventosParticipacoes > 0 and distancia=10; 
update eventosparticipacoes set opcao='Sim' where i_eventosParticipacoes > 0 and distancia=0 and confirmado=1; 
update eventosparticipacoes set opcao='Não' where i_eventosParticipacoes > 0 and distancia=0 and confirmado=0;