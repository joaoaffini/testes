package br.com.jp.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import br.com.jp.dao.LocacaoDAO;
import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.runners.ParallelRunner;
import br.com.jp.servicos.builders.FilmeBuilder;
import br.com.jp.servicos.builders.LocacaoBuilder;
import br.com.jp.servicos.builders.UsuarioBuilder;
import br.com.jp.servicos.matchers.MatchersProprios;
import br.com.jp.utils.DataUtils;

@RunWith(ParallelRunner.class)
public class LocadoraTestTDD {
	
	@Spy
	@InjectMocks
	private LocacaoService service;
	
	
	@Mock
	private SPCService spcService;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService emailService;
	
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		System.out.println("Iniciando 2");
		
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDAO.class);
//		//LocacaoDAO dao = new LocacaoDAOFake();
//		service.setLocacaoDao(dao);
//		spcService = Mockito.mock(SPCService.class);
//		service.setSPCService(spcService);
//		emailService = Mockito.mock(EmailService.class);
//		service.setEmailService(emailService);
	}
	
	@After
	public void end() {
		System.out.println("Finalizando 2");
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		//Se o dia da semana for sabado, este teste nao sera executado
		//Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(service).obterData();
		
		//cenario
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().comValor(5.0).agora();
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		//acao
		Locacao locacao;
		locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
			
		//ErrorCollector
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
		
		//verificar quantidade de chamadas em metodos estaticos
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}
	
	@Test
	public void devePagar75PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(11.00));
	}
	
	@Test
	public void devePagar50PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(13.00));
	}
	
	@Test
	public void devePagar25PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		Filme filme5 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario  = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4, filme5);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.00));
	}
	
	@Test
	public void devePagar0PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		Filme filme5 = FilmeBuilder.umFilme().agora();
		Filme filme6 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.00));
	}
	
	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		
		//Se o dia da semana for sabado, este teste SERA executado
		//Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//utilizando o powermock para setar sempre sabado no date qdo executar este teste
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		//cenario
		Filme filme1 = FilmeBuilder.umFilme().agora();
		List<Filme> filmes = Arrays.asList(filme1);
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEmUmaSegunda());
		
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//Mockito.any(Usuario.class) = significa que o mock retorna true para qualquer usuario
		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		//retirado este codigo para tratar com try catch e poder continuar a execucao do teste apos o erro lancado
		//expectedException.expect(LocadoraException.class);
		//expectedException.expectMessage("Usuário Negativado");
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
			//caso o teste nao lance uma exception o teste deve falhar e nao retornar um falso positivo
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário Negativado"));
		}
		
		//verficacao
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailLocacoesAtrasadas() {
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Usuario3").agora();
		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario).agora(),
											   LocacaoBuilder.umaLocacao().comUsuario(usuario2).agora(),
											   LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora(),
											   LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora());
		
		//Quando o metodo dao.obterLocacoesPendentes() ele vai retornar a list locacoes
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		//Verifica se foi chamado 3 vezes para um usuario
		Mockito.verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		Mockito.verify(emailService, never()).notificarAtraso(usuario2);
		
		//significa que se algum usuario que nao estiver no verify acima entrou no metodo para enviar email
		//ele vai dar erro nos testes
		Mockito.verifyNoMoreInteractions(emailService);
		
	}
	
	@Test
	public void deveTratarErroSPC() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));
		
		//verificacao
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problemas com SPC, tente novamente");
		
		//acao
		service.alugarFilme(usuario, filmes);
		
		
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		//cenario
		Locacao locacao = LocacaoBuilder.umaLocacao().agora();
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		
		//neste momento o argCapt.capture() pega o parametro que foi enviado no metodo salvar
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), CoreMatchers.is(12.00));
		error.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
		
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//utilizando reflection do java para invocar um metodo privado
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valorLocacao = (Double) metodo.invoke(service, filmes);
		
		//verificacao
		Assert.assertThat(valorLocacao, CoreMatchers.is(4.0));
		
	}

}
