import { useEffect, useState } from 'react';
import CardPintura from '../../componentes/CardPintura/CardPintura';
import { Pintura } from '../../tipos';
import api from '../../api';
import estilos from './PaginaCatalogo.module.css';
import { pinturas as imgPinturas } from '../../assets/img';

export default function PaginaCatalogo() {
  const [pinturas, setPinturas] = useState<Pintura[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');
  const [busca, setBusca] = useState('');

 
  // Busca produtos do backend ao carregar a página
  useEffect(() => {
    buscarProdutos();
  }, []);

  const buscarProdutos = async () => {
    try {
      setCarregando(true);
      const resposta = await api.get('/produtos');
      // Mapeia o formato do backend para o formato do frontend

      const imagensDisponiveis = Object.values(imgPinturas);
      const pinturasMapeadas: Pintura[] = resposta.data.map((p: any, index: number) => ({
      id: String(p.id),
      artista: p.artista,
      titulo: p.nome,
      procedencia: 'Coleção ArteDeco',
      ano: new Date().getFullYear(),
      preco: p.preco,
      imagemUrl: imagensDisponiveis[index % imagensDisponiveis.length] as string,
}));
      setPinturas(pinturasMapeadas);
    } catch {
      setErro('Não foi possível carregar o catálogo. Tente novamente.');
    } finally {
      setCarregando(false);
    }
  };

  // Busca por nome ao digitar
  const buscarPorNome = async (termo: string) => {
    setBusca(termo);
    if (!termo.trim()) {
      buscarProdutos();
      return;
    }
    try {
      const imagensDisponiveis = Object.values(imgPinturas);
      const resposta = await api.get(`/produtos/buscar?nome=${termo}`);
      const pinturasMapeadas: Pintura[] = resposta.data.map((p: any, index: number) => ({
        id: String(p.id),
        artista: p.artista,
        titulo: p.nome,
        procedencia: 'Coleção ArteDeco',
        ano: new Date().getFullYear(),
        preco: p.preco,
        imagemUrl: imagensDisponiveis[index % imagensDisponiveis.length] as string,
      }));
      setPinturas(pinturasMapeadas);
    } catch {
      setErro('Erro ao buscar produtos.');
    }
  };

  if (carregando) return <div className={estilos.carregando}>Carregando catálogo...</div>;
  if (erro) return <div className={estilos.erro}>{erro}</div>;

  return (
    <div className={estilos.pagina}>
      <div className={estilos.grade}>
        {pinturas.length === 0
          ? <p>Nenhuma obra encontrada.</p>
          : pinturas.map(pintura => (
             <CardPintura
             key={pintura.id}
             pintura={pintura}
             />
            ))
        }
      </div>
    </div>
  );
}