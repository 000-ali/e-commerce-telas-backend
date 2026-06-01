import Rodape from '../../componentes/Rodape/Rodape';
import estilos from './PaginaSobre.module.css';

const PARAGRAFOS = [
  "Bem-vindo à Acervo Clássico, o primeiro marketplace brasileiro dedicado exclusivamente à comercialização de pinturas clássicas autênticas e de alta qualidade. Nossa missão é conectar apreciadores, colecionadores e decoradores a verdadeiras obras de arte que atravessaram séculos de história.",
  "Diferente de galerias convencionais ou leilões genéricos, oferecemos um ambiente especializado onde cada tela é rigorosamente verificada, com procedência documentada e laudo de autenticidade.",
  "Em nosso acervo, você encontrará desde pinturas italianas do século XVIII, retratos barrocos, naturezas-mortas flamengas e paisagens românticas do século XIX, até reproduções autorizadas de museus para quem busca sofisticação com curadoria acessível.",
  "Trabalhamos com restauradores certificados e historiadores de arte para garantir que cada peça chegue até você nas melhores condições – seja para compor um ambiente clássico, iniciar uma coleção ou presentear com exclusividade.",
  "Na Acervo Clássico, a arte não é apenas decorativa: ela é memória, patrimônio e emoção. E acreditamos que ela merece um lugar especial no seu lar. Venda segura, envio mundial e atendimento com curadoria. Conheça nosso acervo e traga um pedaço da história da arte para perto de você.",
];

const DESTAQUES = [
  { numero: '500+', rotulo: 'Obras disponíveis' },
  { numero: '18°', rotulo: 'Séc. mais antigo no acervo' },
  { numero: '100%', rotulo: 'Autenticidade garantida' },
  { numero: 'Mundial', rotulo: 'Envio para o mundo' },
];

export default function PaginaSobre() {
  return (
    <>
      <main className={estilos.pagina}>
        <div className={estilos.interior}>
          <div className={estilos.cabecalho}>
            <p className={estilos.subtitulo}>Sobre a Loja</p>
            <h1 className={estilos.titulo}>Acervo Clássico</h1>
          </div>
          <div className={estilos.conteudo}>
            {PARAGRAFOS.map((p, i) => <p key={i} className={estilos.paragrafo}>{p}</p>)}
          </div>
          <div className={estilos.destaques}>
            {DESTAQUES.map(d => (
              <div key={d.numero} className={estilos.destaque}>
                <span className={estilos.numero}>{d.numero}</span>
                <span className={estilos.rotuloDestaque}>{d.rotulo}</span>
              </div>
            ))}
          </div>
        </div>
      </main>
      <Rodape />
    </>
  );
}
