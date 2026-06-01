export interface Pintura {
  id: string;
  artista: string;
  titulo: string;
  procedencia: string;
  ano: number;
  preco: number;
  imagemUrl: string;
}

export interface ItemCarrinho {
  pintura: Pintura;
  quantidade: number;
}

export type MetodoPagamento = 'pix' | 'credito' | 'debito';

export interface Artista {
  id: string;
  nome: string;
  nacionalidade: string;
  periodo: string;
  imagemUrl: string;
}

// Usuario agora inclui id e perfil vindos do backend
export interface Usuario {
  id?: number;
  email: string;
  nome: string;
  perfil?: 'CLIENTE' | 'ADMINISTRADOR';
}

export interface Endereco {
  rua: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  estado: string;
  cep: string;
}