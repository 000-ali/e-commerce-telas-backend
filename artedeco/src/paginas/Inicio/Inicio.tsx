import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Hero from '../../componentes/Hero/Hero';
import Carrossel from '../../componentes/Carrossel/Carrossel';
import Rodape from '../../componentes/Rodape/Rodape';
import { listaArtistas } from '../../dados/artistas';
import { pinturas as imgPinturas } from '../../assets/img';
import api from '../../api';
import estilos from './Inicio.module.css';

const TEXTO_SOBRE = [
  "Bem-vindo à Acervo Clássico, o primeiro marketplace brasileiro dedicado exclusivamente à comercialização de pinturas clássicas autênticas e de alta qualidade. Nossa missão é conectar apreciadores, colecionadores e decoradores a verdadeiras obras de arte que atravessaram séculos de história.",
  "Diferente de galerias convencionais ou leilões genéricos, oferecemos um ambiente especializado onde cada tela é rigorosamente verificada, com procedência documentada e laudo de autenticidade.",
  "Trabalhamos com restauradores certificados e historiadores de arte para garantir que cada peça chegue até você nas melhores condições. Na Acervo Clássico, a arte não é apenas decorativa: ela é memória, patrimônio e emoção.",
];

export default function Inicio() {
  const navegar = useNavigate();
  const [itensPinturas, setItensPinturas] = useState<any[]>([]);

  useEffect(() => {
    const imagensDisponiveis = Object.values(imgPinturas);
    api.get('/produtos').then(resposta => {
      const itens = resposta.data.map((p: any, index: number) => ({
        id: String(p.id),
        imagemUrl: imagensDisponiveis[index % imagensDisponiveis.length] as string,
        rotulo: p.nome,
        subRotulo: p.artista,
      }));
      setItensPinturas(itens);
    }).catch(() => {
      // Se falhar, deixa vazio
      setItensPinturas([]);
    });
  }, []);

  const itensArtistas = listaArtistas.map(a => ({
    id: a.id,
    imagemUrl: a.imagemUrl,
    rotulo: a.nome,
    subRotulo: a.periodo
  }));

  return (
    <main>
      <Hero />
      <div className={estilos.secoes}>
        <section className={estilos.secao}>
          <Carrossel titulo="Catálogo" itens={itensPinturas} aoClicarItem={() => navegar('/catalogo')} />
          <div className={estilos.ctaSecao}>
            <button className={estilos.botaoCta} onClick={() => navegar('/catalogo')}>Ver catálogo completo</button>
          </div>
        </section>
        <section className={estilos.secao}>
          <Carrossel titulo="Artistas" itens={itensArtistas} aoClicarItem={() => navegar('/artistas')} />
          <div className={estilos.ctaSecao}>
            <button className={estilos.botaoCta} onClick={() => navegar('/artistas')}>Conhecer todos os artistas</button>
          </div>
        </section>
        <section className={estilos.sobre}>
          <div className={estilos.interiorSobre}>
            <h2 className={estilos.tituloSobre}>Sobre a Loja</h2>
            <div className={estilos.textoSobre}>
              {TEXTO_SOBRE.map((p, i) => <p key={i}>{p}</p>)}
            </div>
            <button className={estilos.botaoCta} onClick={() => navegar('/sobre')}>Saiba mais</button>
          </div>
        </section>
      </div>
      <Rodape />
    </main>
  );
}