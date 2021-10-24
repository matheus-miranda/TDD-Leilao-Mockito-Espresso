package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoTelaTest {

    private static final String ERRO_FALHA_LIMPEZA_DADOS_API = "Banco de dados nao foi limpo";
    private static final String LEILAO_NAO_FOI_SALVO = "Leilao nao foi salvo: ";
    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);
    private final TesteWebClient webClient = new TesteWebClient();

    @Before
    public void setup() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    @Test
    public void deveAparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));
        activity.launchActivity(new Intent());

        onView(withText("Casa")).check(matches(isDisplayed()));
    }

    @Test
    public void deveAparecerDoisLeiloes_QuandoCarregarDoisLeiloesDaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"), new Leilao("Computador"));

        activity.launchActivity(new Intent());

        onView(withText("Casa")).check(matches(isDisplayed()));
        onView(withText("Computador")).check(matches(isDisplayed()));
    }

    @After
    public void tearDrown() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    private void limpaBancoDeDadosDaApi() throws IOException {
        boolean bancoNaoFoiLimpo = !webClient.limpaBanco();
        if (bancoNaoFoiLimpo) {
            Assert.fail(ERRO_FALHA_LIMPEZA_DADOS_API);
        }
    }

    private void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes) {
            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                Assert.fail(LEILAO_NAO_FOI_SALVO + leilao.getDescricao());
            }
        }
    }
}