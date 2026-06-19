import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-identity-editor',
  imports: [FormsModule],
  templateUrl: './identity-editor.html',
  styleUrl: './identity-editor.css'
})
export class IdentityEditor implements OnInit, OnChanges {
  @Input() value = '';
  @Input() entityName = '';
  @Input() kind: 'time' | 'torneio' | 'pessoa' = 'time';
  @Input() required = false;
  @Output() valueChange = new EventEmitter<string>();

  primary = '#078246';
  secondary = '#f7d154';
  shape: 'shield' | 'round' | 'hex' = 'shield';
  symbol: 'initials' | 'ball' | 'star' | 'cup' = 'initials';
  error = '';

  ngOnInit() {
    if (this.required && !this.value) this.generate();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['entityName'] && this.required && !this.value) this.generate();
  }

  async chooseFile(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    input.value = '';
    if (!file) return;
    if (file.type !== 'image/png') {
      this.error = 'Escolha uma imagem PNG.';
      return;
    }
    if (file.size > 1_500_000) {
      this.error = 'A imagem deve ter no maximo 1,5 MB.';
      return;
    }
    this.error = '';
    this.update(await this.readFile(file));
  }

  generate() {
    this.error = '';
    this.update(this.svgDataUrl(this.svg()));
  }

  remove() {
    if (this.required) {
      this.generate();
      return;
    }
    this.update('');
  }

  private update(value: string) {
    this.value = value;
    this.valueChange.emit(value);
  }

  private readFile(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(String(reader.result ?? ''));
      reader.onerror = () => reject(reader.error);
      reader.readAsDataURL(file);
    });
  }

  private svgDataUrl(svg: string): string {
    const bytes = new TextEncoder().encode(svg);
    let binary = '';
    bytes.forEach(byte => binary += String.fromCharCode(byte));
    return `data:image/svg+xml;base64,${btoa(binary)}`;
  }

  private svg(): string {
    const initials = this.initials();
    const shape = this.shapeMarkup();
    const symbol = this.symbolMarkup(initials);
    return `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 160 160">
      <defs><linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
        <stop stop-color="${this.primary}"/><stop offset="1" stop-color="${this.secondary}"/>
      </linearGradient></defs>
      ${shape}
      <path d="M24 42 136 118" stroke="rgba(255,255,255,.16)" stroke-width="15"/>
      ${symbol}
    </svg>`;
  }

  private shapeMarkup(): string {
    if (this.shape === 'round') {
      return `<circle cx="80" cy="80" r="72" fill="url(#g)"/><circle cx="80" cy="80" r="59" fill="none" stroke="white" stroke-opacity=".7" stroke-width="5"/>`;
    }
    if (this.shape === 'hex') {
      return `<path d="M80 5 145 42v76l-65 37-65-37V42Z" fill="url(#g)"/><path d="M80 19 132 49v62l-52 30-52-30V49Z" fill="none" stroke="white" stroke-opacity=".72" stroke-width="5"/>`;
    }
    return `<path d="M80 4 145 27v57c0 39-26 62-65 73C41 146 15 123 15 84V27Z" fill="url(#g)"/><path d="M80 20 130 38v45c0 29-18 47-50 58-32-11-50-29-50-58V38Z" fill="none" stroke="white" stroke-opacity=".72" stroke-width="5"/>`;
  }

  private symbolMarkup(initials: string): string {
    if (this.symbol === 'ball') {
      return `<circle cx="80" cy="79" r="31" fill="white"/><path d="m80 58 13 9-5 15H72l-5-15Zm-24 18 11-9m37 9-11-9M61 99l11-17m27 17L88 82M80 110V92" fill="none" stroke="${this.primary}" stroke-width="5" stroke-linecap="round"/>`;
    }
    if (this.symbol === 'star') {
      return `<path d="m80 42 10 23 25 2-19 17 6 25-22-13-22 13 6-25-19-17 25-2Z" fill="white"/>`;
    }
    if (this.symbol === 'cup') {
      return `<path d="M55 48h50v18c0 22-10 35-25 42-15-7-25-20-25-42Zm0 8H42c0 17 6 27 20 30m43-30h13c0 17-6 27-20 30M80 108v15m-20 0h40" fill="none" stroke="white" stroke-width="8" stroke-linecap="round" stroke-linejoin="round"/>`;
    }
    return `<text x="80" y="92" text-anchor="middle" fill="white" font-family="Arial,sans-serif" font-size="${initials.length > 2 ? 36 : 46}" font-weight="900">${initials}</text>`;
  }

  private initials(): string {
    const parts = (this.entityName || (this.kind === 'torneio' ? 'Novo Torneio' : 'Novo Time'))
      .trim().split(/\s+/).filter(Boolean);
    return parts.slice(0, 2).map(part => part[0]).join('').toUpperCase();
  }
}
