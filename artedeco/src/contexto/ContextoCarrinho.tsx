import { createContext, useContext, useState, ReactNode } from 'react';
import { ItemCarrinho, Pintura } from '../tipos';
import { useAutenticacao } from './ContextoAutenticacao';
import api from '../api';

interface TipoContextoCarrinho {
  itens: ItemCarrinho[];
  aberto: boolean;
  adicionarAoCarrinho: (pintura: Pintura) => void;
  removerDoCarrinho: (id: string) => void;
  abrirCarrinho: () => void;
  fecharCarrinho: () => void;
  limparCarrinho: () => void;
  total: number;
  quantidadeItens: number;
  setItens: (itens: ItemCarrinho[]) => void;
}

const ContextoCarrinho = createContext<TipoContextoCarrinho | undefined>(undefined);

export function ProvedorCarrinho({ children }: { children: ReactNode }) {
  const [itens, setItens] = useState<ItemCarrinho[]>([]);
  const [aberto, setAberto] = useState(false);
  const { usuario } = useAutenticacao();
  
  // Adicionar ao carrinho
  // Se o usuário estiver autenticado, sincroniza com o backend
 
  const adicionarAoCarrinho = async (pintura: Pintura) => {
    // Atualiza estado local imediatamente 
    setItens(prev => {
      if (prev.find(i => i.pintura.id === pintura.id)) return prev;
      return [...prev, { pintura, quantidade: 1 }];
    });
    setAberto(true);

    // Sincroniza com o backend se autenticado
    if (usuario?.id) {
      try {
        await api.post(`/carrinho/${usuario.id}/adicionar`, {
          produtoId: Number(pintura.id),
          quantidade: 1,
        });
      } catch (err) {
        console.error('Erro ao sincronizar carrinho com backend:', err);
      }
    }
  };

  // Remover do carrinho
 
  const removerDoCarrinho = (id: string) =>
    setItens(prev => prev.filter(i => i.pintura.id !== id));
  // Limpar carrinho (chamado após finalizar compra)

  const limparCarrinho = () => setItens([]);

  const abrirCarrinho  = () => setAberto(true);
  const fecharCarrinho = () => setAberto(false);

  const total = itens.reduce((s, i) => s + i.pintura.preco * i.quantidade, 0);
  const quantidadeItens = itens.reduce((s, i) => s + i.quantidade, 0);

  return (
    <ContextoCarrinho.Provider value={{
      itens, aberto,
      adicionarAoCarrinho, removerDoCarrinho,
      abrirCarrinho, fecharCarrinho, limparCarrinho,
      total, quantidadeItens, setItens
    }}>
      {children}
    </ContextoCarrinho.Provider>
  );
}

export function useCarrinho() {
  const ctx = useContext(ContextoCarrinho);
  if (!ctx) throw new Error('useCarrinho deve ser usado dentro de ProvedorCarrinho');
  return ctx;
}