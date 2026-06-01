import { Pintura } from '../tipos';
import { pinturas as imgPinturas } from '../assets/img';

export const listaPinturas: Pintura[] = [
  { id: '1', artista: 'Ernest-Lucien Laurent', titulo: 'Au Jardin', procedencia: 'Dormé à 12 P. Jamot', ano: 1910, preco: 35000, imagemUrl: imgPinturas.mulher },
  { id: '2', artista: 'Émilie Charmy', titulo: 'Flowers', procedencia: 'Coleção Particular, Paris', ano: 1905, preco: 28000, imagemUrl: imgPinturas.vasoDePlantas },
  { id: '3', artista: 'Alessandro di Mariano', titulo: 'La Foresta', procedencia: "Musée d'Orsay, Paris", ano: 1898, preco: 52000, imagemUrl: imgPinturas.mulherNaMata },
  { id: '4', artista: 'Pierre Adolphe Valette', titulo: 'Vaso de Flores', procedencia: 'Coleção Durand-Ruel', ano: 1887, preco: 44000, imagemUrl: imgPinturas.vasoDePlantasDois },
  { id: '5', artista: 'Henri Manguin', titulo: 'Mulher na Mata', procedencia: 'Musée Marmottan Monet', ano: 1906, preco: 67000, imagemUrl: imgPinturas.mulherPensativa },
  { id: '6', artista: 'Ernest-Lucien Laurent', titulo: 'Portrait au Chapeau', procedencia: 'National Gallery, Londres', ano: 1912, preco: 39000, imagemUrl: imgPinturas.mulher },
  { id: '7', artista: 'Émilie Charmy', titulo: 'Nature Morte', procedencia: 'Centre Pompidou, Paris', ano: 1908, preco: 31000, imagemUrl: imgPinturas.vasoDePlantas },
  { id: '8', artista: 'Alessandro di Mariano', titulo: 'Paysage Forestier', procedencia: 'Tate Gallery, Londres', ano: 1901, preco: 58000, imagemUrl: imgPinturas.mulherNaMata },
];
