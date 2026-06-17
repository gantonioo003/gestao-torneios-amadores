import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-partida-detalhes',
  imports: [FormsModule, RouterLink],
  templateUrl: './partida-detalhes.html'
})
export class PartidaDetalhes {
  golsCasa = 2;
  golsVisitante = 1;
  esquemaCasa = '4-4-2';
  esquemaVisitante = '4-2-3-1';

  jogadoresCasa = [
    { n: '1 Alisson', x: 8, y: 50, nota: 6.3 },
    { n: '4 Marquinhos', x: 23, y: 62, nota: 5.9 },
    { n: '8 B. Guimaraes', x: 37, y: 56, nota: 8.1 },
    { n: '7 Vinicius Jr.', x: 44, y: 38, nota: 6.9 },
    { n: '11 Raphinha', x: 31, y: 24, nota: 6.5 },
    { n: '16 D. Santos', x: 18, y: 24, nota: 6.7 }
  ];

  jogadoresVisitante = [
    { n: '23 M. Shobeir', x: 92, y: 50, nota: 7.2 },
    { n: '11 M. Ziko', x: 64, y: 48, nota: 7.1 },
    { n: '19 M. Attia', x: 74, y: 64, nota: 7.0 },
    { n: '17 M. Lasheen', x: 76, y: 35, nota: 6.8 },
    { n: '3 M. Hany', x: 86, y: 24, nota: 6.1 }
  ];

  classificacao = [
    { pos: 1, nome: 'Unidos do Bairro', pts: 9 },
    { pos: 2, nome: 'Resenha FC', pts: 7 },
    { pos: 3, nome: 'Vila FC', pts: 5 },
    { pos: 4, nome: 'Real Esperanca', pts: 3 }
  ];
}
