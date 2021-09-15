package br.com.jp.servicos;

import static br.com.jp.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.jp.dao.LocacaoDAO;
import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO locacaoDao;
	private SPCService spcService;
	private EmailService emailService;
	
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		if(filmes ==  null || filmes.size() == 0) {
			throw new LocadoraException("Filme vazio");
		}
		
		for (Filme f : filmes) {
			
			if(f.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
		boolean negativado;
		
		try {
			
			negativado = spcService.possuiNegativacao(usuario);
			
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}
		
		if(negativado) {
			throw new LocadoraException("Usuário Negativado");
		}
		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		Double precoLocacao = 0.0;
		for(int i = 0; i < filmes.size(); i++) {
			
			double vlrLocacaoFilme = filmes.get(i).getPrecoLocacao();
			
			//aplica 25% desconto para a terceira locacao
			if(i+1 == 3) {
				double desconto = vlrLocacaoFilme * 0.25;
				precoLocacao += vlrLocacaoFilme - desconto;
				
			} else if (i+1 == 4) {
				double desconto = vlrLocacaoFilme * 0.5;
				precoLocacao += vlrLocacaoFilme - desconto;
				
			} else if (i+1 == 5) {
				double desconto = vlrLocacaoFilme * 0.75;
				precoLocacao += vlrLocacaoFilme - desconto;
			} else if (i+1 == 6) {
				double desconto = vlrLocacaoFilme * 1;
				precoLocacao += vlrLocacaoFilme - desconto;
			}
			else {
				precoLocacao += vlrLocacaoFilme;
			}
			
		}
		
		locacao.setValor(precoLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		//Se a entrega cair num domingo, deve adicionar +1 dia
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		locacaoDao.salvar(locacao);
		
		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = locacaoDao.obterLocacoesPendentes();
		
		for (Locacao locacao : locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {				
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}
	
//	public void setLocacaoDao(LocacaoDAO dao) {
//		this.locacaoDao = dao;
//	}
//	
//	public void setSPCService(SPCService spcService) {
//		this.spcService = spcService;
//	}
//
//	public void setEmailService(EmailService emailService) {
//		this.emailService = emailService;
//	}

}