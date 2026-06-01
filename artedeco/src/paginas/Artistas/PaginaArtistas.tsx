import { listaArtistas } from '../../dados/artistas';
import Rodape from '../../componentes/Rodape/Rodape';
import estilos from './PaginaArtistas.module.css';

export default function PaginaArtistas() {
  return (
    <>
      <main className={estilos.pagina}>
        <div className={estilos.interior}>
          <h1 className={estilos.titulo}>Artistas</h1>
          <div className={estilos.grade}>
            {listaArtistas.map(artista => (
              <article key={artista.id} className={estilos.card}>
                <div className={estilos.wrapperImagem}>
                  <img src={artista.imagemUrl} alt={artista.nome} className={estilos.imagem} loading="lazy" />
                </div>
                <div className={estilos.informacoes}>
                  <p className={estilos.nome}>{artista.nome}</p>
                  <p className={estilos.nacionalidade}>{artista.nacionalidade}</p>
                  <p className={estilos.periodo}>{artista.periodo}</p>
                </div>
              </article>
            ))}
          </div>
        </div>
      </main>
      <Rodape />
    </>
  );
}
