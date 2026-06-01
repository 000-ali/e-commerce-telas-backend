import { useState, FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAutenticacao } from '../../contexto/ContextoAutenticacao';
import estilos from './PaginaLogin.module.css';

export default function PaginaLogin() {
  const [modo, setModo] = useState<'entrar' | 'cadastrar'>('entrar');
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const { entrar, cadastrar } = useAutenticacao();
  const navegar = useNavigate();

  const handleEnviar = async (e: FormEvent) => {
    e.preventDefault();
    setErro('');
    setCarregando(true);

    if (modo === 'entrar') {
      const ok = await entrar(email, senha);
      if (ok) { navegar('/'); }
      else { setErro('E-mail ou senha incorretos.'); }
    } else {
      if (!nome.trim()) { setErro('Informe seu nome.'); setCarregando(false); return; }
      if (senha.length < 6) { setErro('A senha deve ter no mínimo 6 caracteres.'); setCarregando(false); return; }
      const ok = await cadastrar(nome, email, senha);
      if (ok) { navegar('/'); }
      else { setErro('Este e-mail já está cadastrado.'); }
    }
    setCarregando(false);
  };

  return (
    <div className={estilos.pagina}>
      <div className={estilos.cartao}>
        <Link to="/" className={estilos.logo}>ArteDeco</Link>
        <h1 className={estilos.titulo}>{modo === 'entrar' ? 'Entrar' : 'Criar conta'}</h1>
        <p className={estilos.subtitulo}>
          {modo === 'entrar'
            ? 'Faça login para adicionar obras ao carrinho e finalizar compras.'
            : 'Crie sua conta para começar a colecionar.'}
        </p>

        <form onSubmit={handleEnviar} className={estilos.formulario}>
          {modo === 'cadastrar' && (
            <div className={estilos.campo}>
              <label className={estilos.rotulo}>Nome</label>
              <input type="text" className={estilos.entrada} placeholder="Seu nome completo" value={nome} onChange={e => setNome(e.target.value)} required />
            </div>
          )}
          <div className={estilos.campo}>
            <label className={estilos.rotulo}>E-mail</label>
            <input type="email" className={estilos.entrada} placeholder="seu@email.com" value={email} onChange={e => setEmail(e.target.value)} required />
          </div>
          <div className={estilos.campo}>
            <label className={estilos.rotulo}>Senha</label>
            <input type="password" className={estilos.entrada} placeholder={modo === 'cadastrar' ? 'Mínimo 6 caracteres' : '••••••••'} value={senha} onChange={e => setSenha(e.target.value)} required />
          </div>
          {erro && <p className={estilos.mensagemErro}>{erro}</p>}
          <button type="submit" className={estilos.botaoEnviar} disabled={carregando}>
            {carregando ? 'Aguarde...' : modo === 'entrar' ? 'Entrar' : 'Criar conta'}
          </button>
        </form>

        <p className={estilos.alternar}>
          {modo === 'entrar' ? 'Ainda não tem conta?' : 'Já tem uma conta?'}{' '}
          <button className={estilos.botaoAlternar} onClick={() => { setModo(modo === 'entrar' ? 'cadastrar' : 'entrar'); setErro(''); }}>
            {modo === 'entrar' ? 'Cadastre-se' : 'Entrar'}
          </button>
        </p>
      </div>
    </div>
  );
}
