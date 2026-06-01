import { useNavigate } from 'react-router-dom';
import { Pintura } from '../../tipos';
import { useCarrinho } from '../../contexto/ContextoCarrinho';
import { useAutenticacao } from '../../contexto/ContextoAutenticacao';
import estilos from './CardPintura.module.css';

interface PropsCardPintura {
  pintura: Pintura;
}

export default function CardPintura({ pintura }: PropsCardPintura) {
  const { adicionarAoCarrinho } = useCarrinho();
  const { autenticado } = useAutenticacao();
  const navegar = useNavigate();

  const formatarPreco = (preco: number) =>
    preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 0 });

  const handleAdicionar = () => {
    if (!autenticado) { navegar('/login'); return; }
    adicionarAoCarrinho(pintura);
  };

  return (
    <article className={estilos.card}>
      <div className={estilos.wrapperImagem}>
        <img src={pintura.imagemUrl} alt={`${pintura.titulo} - ${pintura.artista}`} className={estilos.imagem} loading="lazy" />
        <button className={estilos.botaoAdicionar} onClick={handleAdicionar}>
          Adicionar ao carrinho
        </button>
      </div>
      <div className={estilos.informacoes}>
        <p className={estilos.artista}>{pintura.artista}</p>
        <p className={estilos.titulo}>{pintura.titulo}</p>
        <p className={estilos.procedencia}>{pintura.procedencia} · {pintura.ano}</p>
        <p className={estilos.preco}>{formatarPreco(pintura.preco)}</p>
      </div>
    </article>
  );
}
