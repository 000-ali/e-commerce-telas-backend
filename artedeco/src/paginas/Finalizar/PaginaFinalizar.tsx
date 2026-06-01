import { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCarrinho } from '../../contexto/ContextoCarrinho';
import { useAutenticacao } from '../../contexto/ContextoAutenticacao';
import { MetodoPagamento, Endereco } from '../../tipos';
import api from '../../api';
import estilos from './PaginaFinalizar.module.css';

const ESTADOS_BR = ['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'];

// Mapeia forma de pagamento do frontend para o backend
function mapearFormaPagamento(metodo: MetodoPagamento): string {
  const mapa: Record<MetodoPagamento, string> = {
    pix: 'PIX',
    credito: 'CARTAO_CREDITO',
    debito: 'BOLETO',
  };
  return mapa[metodo];
}

export default function PaginaFinalizar() {
  const { itens, quantidadeItens, total, limparCarrinho } = useCarrinho();
  const { usuario } = useAutenticacao();
  const navegar = useNavigate();

  const [pagamento, setPagamento] = useState<MetodoPagamento>('pix');
  const [etapa, setEtapa] = useState<'resumo' | 'endereco' | 'confirmado'>('resumo');
  const [endereco, setEndereco] = useState<Endereco>({ rua: '', numero: '', complemento: '', bairro: '', cidade: '', estado: '', cep: '' });
  const [erroEndereco, setErroEndereco] = useState('');
  const [finalizando, setFinalizando] = useState(false);
  const [numeroPedido, setNumeroPedido] = useState<number | null>(null);

  const formatarPreco = (preco: number) =>
    preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL', minimumFractionDigits: 0 });

  const handleCampoEndereco = (campo: keyof Endereco, valor: string) =>
    setEndereco(prev => ({ ...prev, [campo]: valor }));


  // Finalizar compra — chama o backend
  const handleValidarEndereco = async (e: FormEvent) => {
    e.preventDefault();
    const { rua, numero, bairro, cidade, estado, cep } = endereco;
    if (!rua || !numero || !bairro || !cidade || !estado || !cep) {
      setErroEndereco('Por favor, preencha todos os campos obrigatórios.');
      return;
    }
    setErroEndereco('');
    setFinalizando(true);

    try {
      // Se usuário autenticado, finaliza pelo backend
      if (usuario?.id) {
        const resposta = await api.post(`/carrinho/${usuario.id}/finalizar`, {
          clienteEmail: usuario.email,
          formaPagamento: mapearFormaPagamento(pagamento),
        });
        setNumeroPedido(resposta.data.pedidoId);
      }
      // Limpa carrinho local e vai para tela de confirmação
      limparCarrinho();
      setEtapa('confirmado');
    } catch {
      setErroEndereco('Erro ao finalizar compra. Tente novamente.');
    } finally {
      setFinalizando(false);
    }
  };

  if (itens?.length === 0 && etapa !== 'confirmado') {
    return (
      <div className={estilos.paginaVazia}>
        <p>Seu carrinho está vazio.</p>
        <button className={estilos.botaoVoltar} onClick={() => navegar('/')}>Voltar à loja</button>
      </div>
    );
  }

  if (etapa === 'confirmado') {
    return (
      <div className={estilos.pagina}>
        <div className={estilos.container}>
          <div className={estilos.confirmacao}>
            <div className={estilos.iconeConfirmacao}>✓</div>
            <h1 className={estilos.tituloConfirmacao}>Pedido Confirmado!</h1>
            {numeroPedido && <p className={estilos.textoConfirmacao}>Pedido #{numeroPedido}</p>}
            <p className={estilos.textoConfirmacao}>
              Obrigado pela sua compra. Você receberá um e-mail com os detalhes do pedido e informações de entrega.
            </p>
            <div className={estilos.resumoEndereco}>
              <p className={estilos.labelEndereco}>Endereço de entrega:</p>
              <p>{endereco.rua}, {endereco.numero}{endereco.complemento ? `, ${endereco.complemento}` : ''}</p>
              <p>{endereco.bairro} – {endereco.cidade}/{endereco.estado}</p>
              <p>CEP: {endereco.cep}</p>
            </div>
            <button className={estilos.botaoContinuar} onClick={() => navegar('/')}>Continuar comprando</button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={estilos.pagina}>
      <div className={estilos.container}>
        <div className={estilos.etapas}>
          <span className={`${estilos.etapa} ${etapa === 'resumo' ? estilos.etapaAtiva : estilos.etapaConcluida}`}>
            1. Resumo
          </span>
          <span className={estilos.separadorEtapa}>›</span>
          <span className={`${estilos.etapa} ${etapa === 'endereco' ? estilos.etapaAtiva : ''}`}>
            2. Endereço
          </span>
        </div>

        {etapa === 'resumo' && (
          <>
            <h1 className={estilos.titulo}>Resumo da Compra</h1>
            <p className={estilos.contadorItens}>Itens: {quantidadeItens}</p>
            <div className={estilos.listaItens}>
              {itens.map(({ pintura }: any) => (
                <div key={pintura.id} className={estilos.item}>
                  {pintura.imagemUrl && (
                    <img src={pintura.imagemUrl} alt={pintura.titulo} className={estilos.imagemItem} />
                  )}
                  <div className={estilos.infoItem}>
                    <p className={estilos.artistaItem}>{pintura.artista}</p>
                    <p className={estilos.tituloItem}>{pintura.titulo}</p>
                    <p className={estilos.precoItem}>{formatarPreco(pintura.preco)}</p>
                  </div>
                </div>
              ))}
            </div>

            <div className={estilos.secaoPagamento}>
              <h2 className={estilos.tituloSecao}>Método de pagamento</h2>
              <div className={estilos.opcoesPagamento}>
                {(['pix', 'credito', 'debito'] as MetodoPagamento[]).map(metodo => (
                  <label key={metodo} className={estilos.opcaoPagamento} onClick={() => setPagamento(metodo)}>
                    <span className={estilos.nomePagamento}>
                      {metodo === 'pix' ? 'Pix' : metodo === 'credito' ? 'Crédito' : 'Débito'}
                    </span>
                    <span className={`${estilos.radio} ${pagamento === metodo ? estilos.radioSelecionado : ''}`} />
                  </label>
                ))}
              </div>
            </div>

            <div className={estilos.linhaTotal}>
              <span>Total</span>
              <span className={estilos.valorTotal}>{formatarPreco(total)}</span>
            </div>

            <button className={estilos.botaoProsseguir} onClick={() => setEtapa('endereco')}>
              Prosseguir para Entrega
            </button>
          </>
        )}

        {etapa === 'endereco' && (
          <>
            <h1 className={estilos.titulo}>Endereço de Entrega</h1>
            <p className={estilos.subtituloEndereco}>Informe onde deseja receber sua obra de arte.</p>
            <form onSubmit={handleValidarEndereco} className={estilos.formularioEndereco}>
              <div className={estilos.fileira}>
                <div className={`${estilos.campo} ${estilos.campoGrande}`}>
                  <label className={estilos.rotulo}>Rua / Avenida *</label>
                  <input className={estilos.entrada} type="text" placeholder="Ex: Rua das Flores" value={endereco.rua} onChange={e => handleCampoEndereco('rua', e.target.value)} required />
                </div>
                <div className={`${estilos.campo} ${estilos.campoPequeno}`}>
                  <label className={estilos.rotulo}>Número *</label>
                  <input className={estilos.entrada} type="text" placeholder="Ex: 123" value={endereco.numero} onChange={e => handleCampoEndereco('numero', e.target.value)} required />
                </div>
              </div>
              <div className={estilos.campo}>
                <label className={estilos.rotulo}>Complemento</label>
                <input className={estilos.entrada} type="text" placeholder="Apto, bloco, sala (opcional)" value={endereco.complemento} onChange={e => handleCampoEndereco('complemento', e.target.value)} />
              </div>
              <div className={estilos.campo}>
                <label className={estilos.rotulo}>Bairro *</label>
                <input className={estilos.entrada} type="text" placeholder="Ex: Centro" value={endereco.bairro} onChange={e => handleCampoEndereco('bairro', e.target.value)} required />
              </div>
              <div className={estilos.fileira}>
                <div className={`${estilos.campo} ${estilos.campoGrande}`}>
                  <label className={estilos.rotulo}>Cidade *</label>
                  <input className={estilos.entrada} type="text" placeholder="Ex: Goiânia" value={endereco.cidade} onChange={e => handleCampoEndereco('cidade', e.target.value)} required />
                </div>
                <div className={`${estilos.campo} ${estilos.campoPequeno}`}>
                  <label className={estilos.rotulo}>Estado *</label>
                  <select className={estilos.entrada} value={endereco.estado} onChange={e => handleCampoEndereco('estado', e.target.value)} required>
                    <option value="">UF</option>
                    {ESTADOS_BR.map(uf => <option key={uf} value={uf}>{uf}</option>)}
                  </select>
                </div>
              </div>
              <div className={estilos.campo}>
                <label className={estilos.rotulo}>CEP *</label>
                <input className={estilos.entrada} type="text" placeholder="Ex: 74000-000" maxLength={9} value={endereco.cep} onChange={e => handleCampoEndereco('cep', e.target.value.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2'))} required />
              </div>
              {erroEndereco && <p className={estilos.mensagemErro}>{erroEndereco}</p>}
              <div className={estilos.botoesEndereco}>
                <button type="button" className={estilos.botaoVoltar} onClick={() => setEtapa('resumo')}>
                  ← Voltar
                </button>
                <button type="submit" className={estilos.botaoFinalizar} disabled={finalizando}>
                  {finalizando ? 'Finalizando...' : 'Finalizar Compra'}
                </button>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
}