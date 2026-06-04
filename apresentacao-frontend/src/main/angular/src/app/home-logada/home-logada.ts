import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({ selector: 'app-home-logada', imports: [RouterLink, FormsModule], templateUrl: './home-logada.html' })
export class HomeLogada {
  editando=false; nomeEdit=''; emailEdit='';
  meusTimes=[{id:1,nome:'Unidos do Bairro'},{id:2,nome:'Resenha FC'}];
  candidaturas=[{id:1,torneio:'Liga Amigos',status:'Pendente'},{id:2,torneio:'Copa Amizade',status:'Aprovada'},{id:3,torneio:'Super Copa',status:'Cancelada'}];
  torneios=[{id:1,nome:'Copa Bairro 2024',status:'Em andamento'},{id:2,nome:'Liga Amigos',status:'Em andamento'},{id:3,nome:'Super Copa',status:'Inscrito'}];
  atividades=[
    {id:1,ico:'⚽',texto:'Unidos do Bairro venceu Real Esperança por 2x1',tempo:'1h'},
    {id:2,ico:'👤',texto:'Pedro Santos marcou um gol pela Liga Amigos',tempo:'2h'},
    {id:3,ico:'🏆',texto:'Novo torneio aberto: Copa da Amizade',tempo:'4h'}
  ];
}
