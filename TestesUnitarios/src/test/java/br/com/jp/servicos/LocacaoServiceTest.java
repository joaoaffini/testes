package br.com.jp.servicos;



import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.servicos.LocacaoService;
import br.com.jp.utils.DataUtils;

public class LocacaoServiceTest {
	
	private static Integer contTests;
	
	private LocacaoService service;
	
	//Esse ErrorCollector captura todos os erros que ocorreram.
	//sem ele o teste para no primeiro erro encontrado e n�o executa as proximas linhas
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		System.out.println("Before");
		service = new LocacaoService();
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
		System.out.println("N�mero de testes executados: "+contTests);
		
	}

	@Test
	public void teste() throws Exception {
		
		System.out.println("Teste");
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//acao
		Locacao locacao;
			locacao = service.alugarFilme(usuario, filme);
			
			//verificacao
//			assertEquals(5.0, locacao.getValor(), 0.01);
//			assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//			assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
//			
//			//assertThat
//			assertThat(locacao.getValor(), is(equalTo(5.0)));
//			assertThat(locacao.getValor(), is(not(6.0)));
//			assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
//			assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
			
			//ErrorCollector
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		
		
	}
	
	//teste esperando uma exception
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		service.alugarFilme(usuario, filme);
		
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
//			Assert.fail("Deveria ter lan�ado uma excecao");
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
		
		//acao
		try {
			service.alugarFilme(null, filme);
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