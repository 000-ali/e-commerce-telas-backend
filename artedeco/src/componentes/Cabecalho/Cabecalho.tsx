import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useCarrinho } from '../../contexto/ContextoCarrinho';
import { useAutenticacao } from '../../contexto/ContextoAutenticacao';
import { pinturas as imgPinturas } from '../../assets/img';
import { icones } from '../../assets/img';
import api from '../../api';
import estilos from './Cabecalho.module.css';

const linksNav = [
  { rotulo: 'Home', rota: '/' },
  { rotulo: 'Catálogo', rota: '/catalogo' },
  { rotulo: 'Artistas', rota: '/artistas' },
  { rotulo: 'Sobre a Loja', rota: '/sobre' },
  { rotulo: 'Contato', rota: '/#contato' },
];

interface ResultadoBusca {
  id: string;
  titulo: string;
  artista: string;
  imagemUrl: string;
}

export default function Cabecalho() {
  const { abrirCarrinho, quantidadeItens } = useCarrinho();
  const { autenticado, usuario, sair } = useAutenticacao();
  const localizacao = useLocation();
  const navegar = useNavigate();
  const [buscaAberta, setBuscaAberta] = useState(false);
  const [termoBusca, setTermoBusca] = useState('');
  const [resultados, setResultados] = useState<ResultadoBusca[]>([]);

  const imagensDisponiveis = Object.values(imgPinturas);

  const handleBusca = async (termo: string) => {
    setTermoBusca(termo);
    if (termo.trim().length < 2) { setResultados([]); return; }

    try {
      // Busca por nome da obra
      const respostaNome = await api.get(`/produtos/buscar?nome=${termo}`);
      // Busca por artista
      const respostaArtista = await api.get(`/produtos/artista?nome=${termo}`);

      // Junta os dois resultados e remove duplicatas pelo id
      const todos = [...respostaNome.data, ...respostaArtista.data];
      const unicos = todos.filter((p: any, index: number, self: any[]) =>
        index === self.findIndex((t: any) => t.id === p.id)
      );

      const mapeados: ResultadoBusca[] = unicos.map((p: any, index: number) => ({
        id: String(p.id),
        titulo: p.nome,
        artista: p.artista,
        imagemUrl: imagensDisponiveis[(p.id - 1) % imagensDisponiveis.length] as string,
      }));

      setResultados(mapeados);
    } catch {
      setResultados([]);
    }
  };

  const handleCarrinho = () => {
    if (!autenticado) { navegar('/login'); return; }
    abrirCarrinho();
  };

  return (
    <header className={estilos.cabecalho}>
      <div className={estilos.interior}>
        <Link to="/" className={estilos.logo}>ArteDeco</Link>

        <nav className={estilos.nav}>
          <ul className={estilos.listaNav}>
            {linksNav.map(link => (
              <li key={link.rota}>
                <Link
                  to={link.rota}
                  className={`${estilos.linkNav} ${localizacao.pathname === link.rota ? estilos.ativo : ''}`}
                >
                  {link.rotulo}
                </Link>
              </li>
            ))}
          </ul>
        </nav>

        <div className={estilos.acoes}>
          {/* Busca */}
          <div className={estilos.wrapperBusca}>
            <button className={estilos.botaoIcone} aria-label="Buscar" onClick={() => setBuscaAberta(v => !v)}>
              <img src={icones.busca} alt="buscar" className={estilos.icone} />
            </button>
            {buscaAberta && (
              <div className={estilos.dropdownBusca}>
                <input
                  autoFocus
                  type="text"
                  placeholder="Buscar pinturas, artistas..."
                  value={termoBusca}
                  onChange={e => handleBusca(e.target.value)}
                  className={estilos.campoBusca}
                />
                {resultados.length > 0 && (
                  <ul className={estilos.resultadosBusca}>
                    {resultados.map(p => (
                      <li key={p.id} className={estilos.itemResultado}
                        onClick={() => { navegar('/catalogo'); setBuscaAberta(false); setTermoBusca(''); }}>
                        <img src={p.imagemUrl} alt={p.titulo} className={estilos.miniaturaResultado} />
                        <div>
                          <p className={estilos.tituloResultado}>{p.titulo}</p>
                          <p className={estilos.artistaResultado}>{p.artista}</p>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
                {termoBusca.length >= 2 && resultados.length === 0 && (
                  <p className={estilos.semResultados}>Nenhum resultado encontrado</p>
                )}
              </div>
            )}
          </div>

          {/* Usuário */}
          {autenticado ? (
            <div className={estilos.menuUsuario}>
              <button className={estilos.botaoIcone} aria-label="Minha conta">
                <img src={icones.usuario} alt="usuário" className={estilos.icone} />
              </button>
              <div className={estilos.dropdownUsuario}>
                <p className={estilos.nomeUsuario}>{usuario?.nome}</p>
                <button onClick={sair} className={estilos.botaoSair}>Sair</button>
              </div>
            </div>
          ) : (
            <Link to="/login" className={estilos.botaoIcone} aria-label="Login">
              <img src={icones.usuario} alt="usuário" className={estilos.icone} />
            </Link>
          )}

          {/* Carrinho */}
          <button className={estilos.botaoIcone} aria-label="Carrinho" onClick={handleCarrinho}>
            <span className={estilos.wrapperCarrinho}>
              <img src={icones.carrinho} alt="carrinho" className={estilos.icone} />
              {quantidadeItens > 0 && <span className={estilos.contadorCarrinho}>{quantidadeItens}</span>}
            </span>
          </button>
        </div>
      </div>
    </header>
  );
}