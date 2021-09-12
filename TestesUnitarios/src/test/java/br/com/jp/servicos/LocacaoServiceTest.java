package br.com.jp.servicos;



import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.com.jp.dao.LocacaoDAO;
import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.servicos.matchers.MatchersProprios;
import br.com.jp.utils.DataUtils;

public class LocacaoServiceTest {
	
	private static Integer contTests;
	
	private LocacaoService service;
	
	//Esse ErrorCollector captura todos os erros que ocorreram.
	//sem ele o teste para no primeiro erro encontrado e não executa as proximas linhas
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		System.out.println("Before");
		service = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDao(dao);
		SPCService spcService = Mockito.mock(SPCService.class);
		service.setSPCService(spcService);
	}
	
	@After
	public void tearDown() {
		System.out.println("After");
		contTests++;
	}
	
	@BeforeClass
	public static void setupClass() {
		System.out.println("Before Class");
		contTests = 0;
		//service = new LocacaoService();
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
		System.out.println("Número de testes executados: "+contTests);
		
	}

	@Test
	public void teste() throws Exception {
		
		System.out.println("Teste");
		
		//cenario
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		//acao
		Locacao locacao;
		locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
			
		//ErrorCollector
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}
	
	//teste esperando uma exception
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		service.alugarFilme(usuario, filmes);
		
	}
	
//	@Test
//	public void testLocacao_filmeSemEstoque2() {
//		LocacaoService service = new LocacaoService();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 0, 5.0);
//		
//		try {
//			service.alugarFilme(usuario, filme);
//			//Se nao lancar uma exception o teste nao ocorreu como o esperado
//			//portando ele deve falhar
//			Assert.fail("Deveria ter lançado uma excecao");
//		} catch (Exception e) {
//			Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
//		}
//	}
//	
//	@Test
//	public void testLocacao_filmeSemEstoque3() throws Exception {
//		LocacaoService service = new LocacaoService();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 0, 5.0);
//		//a expectedException deve ser declarada antes de executar a acao que lanca a exception
//		expectedException.expect(Exception.class);
//		expectedException.expectMessage("Filme sem estoque");
//		
//		service.alugarFilme(usuario, filme);
//		
//	}
	
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
		
		//cenario
		Filme filme = new Filme("Filme 1", 1, 5.0);
		//Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		//acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		
		//cenario
		//Filme filme = new Filme("Filme 1", 1, 5.0);
		Usuario usuario = new Usuario("Usuario 1");
		
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");
		
		//acao
		service.alugarFilme(usuario, null);
	}
}
