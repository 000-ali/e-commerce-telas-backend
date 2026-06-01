import estilos from './Rodape.module.css';

export default function Rodape() {
  return (
    <footer className={estilos.rodape} id="contato">
      <div className={estilos.interior}>
        <div className={estilos.marca}>
          <span className={estilos.logo}>ArteDeco</span>
          <p className={estilos.slogan}>O primeiro marketplace brasileiro de pinturas clássicas autênticas.</p>
        </div>
        <div className={estilos.coluna}>
          <h4 className={estilos.tituloColuna}>Contato</h4>
          <ul className={estilos.listaColuna}>
            <li>contato@artedeco.com.br</li>
            <li>+55 (62) 99999-0000</li>
            <li>Goiânia – GO, Brasil</li>
          </ul>
        </div>
        <div className={estilos.coluna}>
          <h4 className={estilos.tituloColuna}>Navegação</h4>
          <ul className={estilos.listaColuna}>
            <li><a href="/">Home</a></li>
            <li><a href="/catalogo">Catálogo</a></li>
            <li><a href="/artistas">Artistas</a></li>
            <li><a href="/sobre">Sobre a Loja</a></li>
          </ul>
        </div>
        <div className={estilos.coluna}>
          <h4 className={estilos.tituloColuna}>Informações</h4>
          <ul className={estilos.listaColuna}>
            <li>Venda segura</li>
            <li>Envio mundial</li>
            <li>Laudo de autenticidade</li>
            <li>Restauradores certificados</li>
          </ul>
        </div>
      </div>
      <div className={estilos.fundo}>
        <p>© {new Date().getFullYear()} ArteDeco · Todos os direitos reservados</p>
      </div>
    </footer>
  );
}
