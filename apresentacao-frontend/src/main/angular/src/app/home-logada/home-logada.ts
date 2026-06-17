import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home-logada',
  imports: [RouterLink, FormsModule],
  templateUrl: './home-logada.html'
})
export class HomeLogada {
  editando = false;
  nomeEdit = 'Lucas Lima';
  emailEdit = 'lucas@email.com';

  meusTimes = [
    { id: 1, nome: 'Unidos do Bairro', papel: 'Responsavel' },
    { id: 2, nome: 'Resenha FC', papel: 'Tecnico' }
  ];

  candidaturas = [
    { torneio: 'Liga Amigos', status: 'Pendente' },
    { torneio: 'Copa da Amizade', status: 'Aprovada' },
    { torneio: 'Super Copa', status: 'Cancelada' }
  ];

  torneios = [
    { id: 1, nome: 'Copa Bairro 2024', status: 'Em andamento' },
    { id: 2, nome: 'Liga Amigos', status: 'Em andamento' },
    { id: 3, nome: 'Super Copa', status: 'Inscrito' }
  ];

  atividades = [
    { icon: 'bi-trophy', texto: 'Seu time foi aceito na Copa Bairro 2024.', tempo: '1h' },
    { icon: 'bi-dribbble', texto: 'Pedro Santos marcou 1 gol pelo Resenha FC.', tempo: '2h' },
    { icon: 'bi-megaphone', texto: 'Novo torneio aberto: Copa da Amizade.', tempo: '4h' }
  ];
}
