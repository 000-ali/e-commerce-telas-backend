import { useNavigate } from 'react-router-dom';
import { useCarrinho } from '../../contexto/ContextoCarrinho';
import estilos from './Carrinho.module.css';

const IconeFechar = () => (
  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
    <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
  </svg>
);
const IconeLixeira = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="3 6 5 6 21 6" /><path d="M19 6l-1 14H6L5 6" /><path d="M10 11v6M14 11v6" /><path d="M9 6V4h6v2" />
  </svg>
);

export default function Carrinho() {
  const { itens, aberto, fecharCarrinho, removerDoCarrinho, quantidadeItens } = useCarrinho();
  const navegar = useNavigate();

  const formatarPreco = (preco: number) =>
    preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 0 });

  const handleFinalizar = () => {
    fecharCarrinho();
    navegar('/finalizar');
  };

  return (
    <>
      <div className={`${estilos.sobreposicao} ${aberto ? estilos.sobreposicaoVisivel : ''}`} onClick={fecharCarrinho} aria-hidden="true" />
      <aside className={`${estilos.carrinho} ${aberto ? estilos.carrinhoAberto : ''}`} aria-label="Carrinho de compras">
        <div className={estilos.cabecalho}>
          <h2 className={estilos.titulo}>Carrinho</h2>
          <button className={estilos.botaoFechar} onClick={fecharCarrinho}><IconeFechar /></button>
        </div>
        <p className={estilos.contadorItens}>Itens: {quantidadeItens}</p>
        <div className={estilos.listaItens}>
          {itens.length === 0 ? (
            <div className={estilos.vazio}>
              <p>Seu carrinho está vazio.</p>
              <button className={estilos.botaoContinuar} onClick={fecharCarrinho}>Continuar comprando</button>
            </div>
          ) : (
            itens.map(({ pintura }) => (
              <div key={pintura.id} className={estilos.item}>
                <img src={pintura.imagemUrl} alt={pintura.titulo} className={estilos.imagemItem} />
                <div className={estilos.infoItem}>
                  <p className={estilos.artistaItem}>{pintura.artista}</p>
                  <p className={estilos.tituloItem}>{pintura.titulo}</p>
                  <p className={estilos.procedenciaItem}>{pintura.procedencia} · {pintura.ano}</p>
                  <p className={estilos.precoItem}>{formatarPreco(pintura.preco)}</p>
                </div>
                <button className={estilos.botaoRemover} onClick={() => removerDoCarrinho(pintura.id)}><IconeLixeira /></button>
              </div>
            ))
          )}
        </div>
        <div className={estilos.rodape}>
          {itens.length > 0
            ? <button className={estilos.botaoFinalizar} onClick={handleFinalizar}>Efetuar Compra</button>
            : <button className={estilos.botaoContinuarRodape} onClick={fecharCarrinho}>Continuar comprando</button>
          }
        </div>
      </aside>
    </>
  );
}
