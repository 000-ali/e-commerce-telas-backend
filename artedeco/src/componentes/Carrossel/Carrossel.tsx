import { useRef } from 'react';
import estilos from './Carrossel.module.css';

interface ItemCarrossel {
  id: string;
  imagemUrl: string;
  rotulo: string;
  subRotulo?: string;
}

interface PropsCarrossel {
  titulo: string;
  itens: ItemCarrossel[];
  aoClicarItem?: (id: string) => void;
}

export default function Carrossel({ titulo, itens, aoClicarItem }: PropsCarrossel) {
  const trilhaRef = useRef<HTMLDivElement>(null);

  const rolar = (direcao: 'esquerda' | 'direita') => {
    if (!trilhaRef.current) return;
    trilhaRef.current.scrollBy({ left: direcao === 'direita' ? 320 : -320, behavior: 'smooth' });
  };

  return (
    <div className={estilos.carrossel}>
      <div className={estilos.cabecalho}>
        <h2 className={estilos.titulo}>{titulo}</h2>
        <div className={estilos.setas}>
          <button className={estilos.seta} onClick={() => rolar('esquerda')} aria-label="Anterior">‹</button>
          <button className={estilos.seta} onClick={() => rolar('direita')} aria-label="Próximo">›</button>
        </div>
      </div>
      <div className={estilos.trilha} ref={trilhaRef}>
        {itens.map(item => (
          <div key={item.id} className={estilos.card} onClick={() => aoClicarItem?.(item.id)}>
            <div className={estilos.wrapperImagem}>
              <img src={item.imagemUrl} alt={item.rotulo} className={estilos.imagem} loading="lazy" />
            </div>
            <p className={estilos.rotulo}>{item.rotulo}</p>
            {item.subRotulo && <p className={estilos.subRotulo}>{item.subRotulo}</p>}
          </div>
        ))}
      </div>
    </div>
  );
}
