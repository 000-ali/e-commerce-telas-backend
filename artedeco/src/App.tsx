import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ProvedorCarrinho } from './contexto/ContextoCarrinho';
import { ProvedorAutenticacao } from './contexto/ContextoAutenticacao';
import Cabecalho from './componentes/Cabecalho/Cabecalho';
import Carrinho from './componentes/Carrinho/Carrinho';
import Inicio from './paginas/Inicio/Inicio';
import PaginaCatalogo from './paginas/Catalogo/PaginaCatalogo';
import PaginaArtistas from './paginas/Artistas/PaginaArtistas';
import PaginaSobre from './paginas/Sobre/PaginaSobre';
import PaginaFinalizar from './paginas/Finalizar/PaginaFinalizar';
import PaginaLogin from './paginas/Login/PaginaLogin';

export default function App() {
  return (
    <BrowserRouter>
      <ProvedorAutenticacao>
        <ProvedorCarrinho>
          <Cabecalho />
          <Carrinho />
          <Routes>
            <Route path="/" element={<Inicio />} />
            <Route path="/catalogo" element={<PaginaCatalogo />} />
            <Route path="/artistas" element={<PaginaArtistas />} />
            <Route path="/sobre" element={<PaginaSobre />} />
            <Route path="/finalizar" element={<PaginaFinalizar />} />
            <Route path="/login" element={<PaginaLogin />} />
          </Routes>
        </ProvedorCarrinho>
      </ProvedorAutenticacao>
    </BrowserRouter>
  );
}
