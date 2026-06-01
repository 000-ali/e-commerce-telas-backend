import { imagens } from '../../assets/img';
import estilos from './Hero.module.css';

export default function Hero() {
  return (
    <section className={estilos.hero}>
      <img src={imagens.galeria} alt="Galeria de arte clássica" className={estilos.imagem} />
      <div className={estilos.sobreposicao} />
      <div className={estilos.conteudo}>
        <p className={estilos.subtitulo}>Marketplace de Pinturas Clássicas</p>
        <h1 className={estilos.titulo}>Acervo Clássico</h1>
        <p className={estilos.descricao}>Arte autêntica com procedência documentada</p>
      </div>
      <div className={estilos.indicadorScroll}>
        <span className={estilos.linhaScroll} />
        <span className={estilos.textoScroll}>Explorar</span>
      </div>
    </section>
  );
}
