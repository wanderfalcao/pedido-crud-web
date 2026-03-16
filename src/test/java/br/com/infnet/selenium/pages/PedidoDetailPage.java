package br.com.infnet.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PedidoDetailPage extends BasePage {

    @FindBy(css = ".sbadge")
    private WebElement badgeStatus;

    @FindBy(css = "h1.page-title")
    private WebElement titulo;

    public PedidoDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getStatus() {
        aguardarElemento(By.cssSelector(".sbadge"));
        return badgeStatus.getText();
    }

    public String getClasseBadgeStatus() {
        aguardarElemento(By.cssSelector(".sbadge"));
        return badgeStatus.getAttribute("class");
    }

    public String getDescricao() {
        aguardarElemento(By.cssSelector("h1.page-title"));
        return titulo.getText();
    }

    public PedidoListPage clicarVoltar() {
        WebElement link = driver.findElement(By.cssSelector("a[href='/pedidos']"));
        clicarComJs(link);
        return new PedidoListPage(driver);
    }

    public PedidoListPage clicarAvancarStatus(String statusAlvo) {
        By selector = By.cssSelector("input[name='novoStatus'][value='" + statusAlvo + "']");
        aguardarElemento(selector);
        WebElement input = driver.findElement(selector);
        WebElement form = input.findElement(By.xpath("./.."));
        WebElement btn = form.findElement(By.cssSelector("button[type='submit']"));
        clicarComJs(btn);
        wait.until(ExpectedConditions.stalenessOf(btn));
        return new PedidoListPage(driver);
    }

    public boolean formularioAdicionarItemVisivel() {
        return !driver.findElements(By.cssSelector("form[action*='/itens']")).isEmpty();
    }

    public PedidoDetailPage adicionarItem(String nomeProduto, String precoUnitario, int quantidade) {
        aguardarElemento(By.name("nomeProduto"));
        driver.findElement(By.name("nomeProduto")).sendKeys(nomeProduto);
        driver.findElement(By.name("precoUnitario")).clear();
        driver.findElement(By.name("precoUnitario")).sendKeys(precoUnitario);
        driver.findElement(By.name("quantidade")).clear();
        driver.findElement(By.name("quantidade")).sendKeys(String.valueOf(quantidade));
        WebElement btnAdicionar = driver.findElement(By.cssSelector("form[action*='/itens'] button[type='submit']"));
        clicarComJs(btnAdicionar);
        wait.until(ExpectedConditions.stalenessOf(btnAdicionar));
        return new PedidoDetailPage(driver);
    }

    public int contarItens() {
        return driver.findElements(By.cssSelector("table tbody tr")).size();
    }

    public boolean alertaSucessoVisivel() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
