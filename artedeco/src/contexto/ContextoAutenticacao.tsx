import { createContext, useContext, useState, ReactNode } from 'react';
import { Usuario } from '../tipos';
import api from '../api';

interface TipoContextoAutenticacao {
  usuario: Usuario | null;
  token: string | null;
  entrar: (email: string, senha: string) => Promise<boolean>;
  cadastrar: (nome: string, email: string, senha: string) => Promise<boolean>;
  sair: () => void;
  autenticado: boolean;
}

const ContextoAutenticacao = createContext<TipoContextoAutenticacao | undefined>(undefined);

const CHAVE_SESSAO = 'artedeco_sessao';
const CHAVE_TOKEN  = 'artedeco_token';

export function ProvedorAutenticacao({ children }: { children: ReactNode }) {

  const [usuario, setUsuario] = useState<Usuario | null>(() => {
    try { return JSON.parse(localStorage.getItem(CHAVE_SESSAO) || 'null'); } catch { return null; }
  });

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem(CHAVE_TOKEN);
  });

  const entrar = async (email: string, senha: string): Promise<boolean> => {
    try {
      const resposta = await api.post('/auth/login', { email, senha });
      console.log('Resposta do backend:', resposta.data); 
      const token = resposta.data.token;
      const nome = resposta.data.nome;
      const emailRetornado = resposta.data.email;
      const perfil = resposta.data.perfil;
      const id = resposta.data.id;

      const u: Usuario = { id, email: emailRetornado, nome, perfil };

      setUsuario(u);
      setToken(token);
      localStorage.setItem(CHAVE_SESSAO, JSON.stringify(u));
      localStorage.setItem(CHAVE_TOKEN, token);
      return true;
    } catch {
      return false;
    }
  };

  // 
  // Cadastro — chama POST /auth/cadastro no backend
  // 
  const cadastrar = async (nome: string, email: string, senha: string): Promise<boolean> => {
    try {
      const resposta = await api.post('/auth/cadastro', { nome, email, senha });
      const token = resposta.data.token;
      const nomeRetornado = resposta.data.nome;
      const emailRetornado = resposta.data.email;
      const perfil = resposta.data.perfil;
      const id = resposta.data.id;

      const u: Usuario = { id, email: emailRetornado, nome: nomeRetornado, perfil };
      setUsuario(u);
      setToken(token);
      localStorage.setItem(CHAVE_SESSAO, JSON.stringify(u));
      localStorage.setItem(CHAVE_TOKEN, token);
      return true;
    } catch {
      return false;
    }
  };

 
  // Logout — limpa sessão local

  const sair = () => {
    setUsuario(null);
    setToken(null);
    localStorage.removeItem(CHAVE_SESSAO);
    localStorage.removeItem(CHAVE_TOKEN);
  };

  return (
    <ContextoAutenticacao.Provider value={{ usuario, token, entrar, cadastrar, sair, autenticado: !!usuario }}>
      {children}
    </ContextoAutenticacao.Provider>
  );
}

export function useAutenticacao() {
  const ctx = useContext(ContextoAutenticacao);
  if (!ctx) throw new Error('useAutenticacao deve ser usado dentro de ProvedorAutenticacao');
  return ctx;
}