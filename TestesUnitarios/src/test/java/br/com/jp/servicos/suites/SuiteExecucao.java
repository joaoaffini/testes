package br.com.jp.servicos.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.jp.servicos.CalculadoraLocadoraTest;
import br.com.jp.servicos.CalculadoraTest;
import br.com.jp.servicos.LocacaoServiceTest;
import br.com.jp.servicos.LocadoraTestTDD;

@RunWith(Suite.class)
@SuiteClasses({
	LocacaoServiceTest.class,
	LocadoraTestTDD.class,
	CalculadoraLocadoraTest.class,
	CalculadoraTest.class
})
public class SuiteExecucao {
	//aqui pode usar apensa beforeclass e afterclass
	
	@BeforeClass
	public static void before() {
		System.out.println("Before suite");
	}
	
	@AfterClass
	public static void after() {
		System.out.println("After suite");
	}

}
